package com.intuso.housemate.pkg.server.jar;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.intuso.housemate.pkg.server.jar.ioc.JarServerModule;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.plugin.host.internal.PluginHost;
import com.intuso.housemate.server.object.real.FactoryPluginListener;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import jssc.SerialPortList;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Platform implementation for a server. Works the same was as the PC implementation in terms of getting properties
 * and overriding based on command line arguments, Main difference is that the Comms implementation is different
 * and some methods are unsupported as they should not be used by the server.
 *
 */
public class ServerEnvironment {

    public final static String PLUGINS_DIR_NAME= "plugins";
    public final static String RUN_WEBAPP = "webapp.run";
    public final static String WEBAPP_PORT = "webapp.port";

    private final static Logger logger = LoggerFactory.getLogger(ServerEnvironment.class);

    private final Map<String, String> pluginIds = Maps.newHashMap();

    /**
	 * Create a environment instance
	 * @param args the command line args that the server was run with
	 */
	public ServerEnvironment(String args[]) {

        ListenersFactory listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
        PropertyRepository properties = Properties.create(listenersFactory, defaultProperties, args);
        setExtraDefaults(defaultProperties);

        Injector injector = Guice.createInjector(new JarServerModule(defaultProperties, properties));

        logger.debug("Starting server");
        injector.getInstance(com.intuso.housemate.server.Server.class).start();
        logger.debug("Started server");

        // force factory listener to be created and register itself for new plugins
        injector.getInstance(FactoryPluginListener.class);

        logger.debug("Loading plugins");
        loadPlugins(injector, properties);
        logger.debug("Loaded plugins");

        logger.debug("Starting webapp");
        startWebapp(injector, properties);
        logger.debug("Started webapp");

        logger.debug("Finished startup, accepting external client connections");
        injector.getInstance(BrokerService.class);
    }

    private void setExtraDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(RUN_WEBAPP, "true");
    }

    private void loadPlugins(Injector injector, PropertyRepository properties) {

        loadSharedJNILibs();

        PluginHost pluginHost = injector.getInstance(PluginHost.class);

        // discover plugins from local dir
        File pluginDirectory = new File(properties.get(Properties.HOUSEMATE_CONFIG_DIR) + File.separator + PLUGINS_DIR_NAME);
        if(!pluginDirectory.exists())
            pluginDirectory.mkdir();
        if(pluginDirectory.isFile())
            logger.warn("Plugin path is not a directory");
        else {
            logger.debug("Loading plugins from " + pluginDirectory.getAbsolutePath());
            for(File pluginFile : pluginDirectory.listFiles(new PluginFileFilter())) {
                try {
                    pluginIds.put(pluginFile.getAbsolutePath(), pluginHost.addPlugin(createPluginInjector(injector, pluginFile, logger)));
                } catch(Throwable t) {
                    logger.warn("Failed to add plugin for file " + pluginFile.getAbsolutePath(), t);
                }
            }
        }
    }

    private void loadSharedJNILibs() {
        SerialPortList.getPortNames();
        //CommPortIdentifier.getPortIdentifiers();
    }

    private Injector createPluginInjector(Injector injector, File file, Logger logger) {
        return injector.createChildInjector(getPluginModules(file, logger));
    }

    private List<Module> getPluginModules(File file, Logger logger) {
        logger.debug("Loading plugins from " + file.getAbsolutePath());
        try {
            ClassLoader cl = new URLClassLoader(new URL[] {file.toURI().toURL()}, getClass().getClassLoader());
            return Lists.newArrayList(ServiceLoader.load(Module.class, cl));
        } catch(MalformedURLException e) {
            logger.error("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not get URL for file");
            return Lists.newArrayList();
        } catch(IOException e) {
            logger.error("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not load mainifest file");
            return Lists.newArrayList();
        }
    }

    private class PluginFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.isFile() && file.getName().endsWith(".jar");
        }
    }

    private void startWebapp(Injector injector, PropertyRepository properties) {

        try {

            if (properties.get(RUN_WEBAPP) != null
                    && properties.get(RUN_WEBAPP).equalsIgnoreCase("false")) {
                logger.debug("Not starting webapp");
                return;
            }

            int port = 46874;
            try {
                if (properties.keySet().contains(WEBAPP_PORT))
                    port = Integer.parseInt(properties.get(WEBAPP_PORT));
            } catch (Throwable t) {
                logger.warn("Failed to parse property " + WEBAPP_PORT + ". Using default of " + port + " instead");
            }

            startJetty(injector, properties, port);
        } catch(Throwable t) {
            logger.error("Failed to start web server", t);
        }
    }

    private void startJetty(Injector injector, PropertyRepository properties, int port) {
// todo when web interface reenabled
        /*ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(CommsServiceImpl.class, "/Housemate/comms");
        servletHandler.addServletWithMapping(StaticFilesServlet.class, "*//*");

        ServletContextHandler handler = new ServletContextHandler(null, "/housemate");
        handler.setServletHandler(servletHandler);
        handler.setSessionHandler(new SessionHandler());
        try {
            // cannot simply use getResource("/") as this returns the url for the main class loader. Need to instead
            // find the url for index.html and get the directory it's contained in
            URL url = CommsServiceImpl.class.getClassLoader().getResource("index.html");
            if(url != null)
                handler.setBaseResource(URLResource.newResource(new URL(url, ".")));
            else
                throw new IOException("Could not find index.html location");
        } catch (IOException e) {
            throw new HousemateCommsException("Failed to register housemate static web resources", e);
        }
        handler.addEventListener(injector.getInstance(ContextListener.class));

        Server server = new Server(port);
        server.setHandler(handler);
        server.setStopAtShutdown(true);

        // Start the server
        try {
            server.start();
        } catch(Exception e) {
            throw new HousemateCommsException("Failed to start internal webserver", e);
        }*/
    }
}
