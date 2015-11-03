package com.intuso.housemate.pkg.server.jar;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.pkg.server.jar.ioc.JarServerModule;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.housemate.server.object.real.FactoryPluginListener;
import com.intuso.housemate.server.object.real.persist.RealObjectWatcher;
import com.intuso.housemate.web.server.ContextListener;
import com.intuso.housemate.web.server.service.CommsServiceImpl;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import jssc.SerialPortList;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.resource.URLResource;

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

        Log log = injector.getInstance(Log.class);

        log.d("Starting server");
        injector.getInstance(com.intuso.housemate.server.Server.class).start();
        log.d("Started server");

        // force factory listener to be created and register itself for new plugins
        injector.getInstance(FactoryPluginListener.class);

        log.d("Loading plugins");
        loadPlugins(injector, properties);
        log.d("Loaded plugins");

        log.d("Loading/watching objects");
        injector.getInstance(RealObjectWatcher.class).start();
        log.d("Loaded objects");

        log.d("Starting webapp");
        startWebapp(injector, properties);
        log.d("Started webapp");

        log.d("Finished startup, accepting external client requests");
        injector.getInstance(com.intuso.housemate.server.Server.class).acceptClients();
    }

    private void setExtraDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(RUN_WEBAPP, "true");
    }

    private void loadPlugins(Injector injector, PropertyRepository properties) {

        loadSharedJNILibs();

        Log log = injector.getInstance(Log.class);
        PluginManager pluginManager = injector.getInstance(PluginManager.class);

        // discover plugins from local dir
        File pluginDirectory = new File(properties.get(Properties.HOUSEMATE_CONFIG_DIR) + File.separator + PLUGINS_DIR_NAME);
        if(!pluginDirectory.exists())
            pluginDirectory.mkdir();
        if(pluginDirectory.isFile())
            log.w("Plugin path is not a directory");
        else {
            log.d("Loading plugins from " + pluginDirectory.getAbsolutePath());
            for(File pluginFile : pluginDirectory.listFiles(new PluginFileFilter())) {
                try {
                    pluginIds.put(pluginFile.getAbsolutePath(), pluginManager.addPlugin(createPluginInjector(injector, pluginFile, log)));
                } catch(Throwable t) {
                    log.w("Failed to add plugin for file " + pluginFile.getAbsolutePath(), t);
                }
            }
        }
    }

    private void loadSharedJNILibs() {
        SerialPortList.getPortNames();
        //CommPortIdentifier.getPortIdentifiers();
    }

    private Injector createPluginInjector(Injector injector, File file, Log log) {
        return injector.createChildInjector(getPluginModules(file, log));
    }

    private List<Module> getPluginModules(File file, Log log) {
        log.d("Loading plugins from " + file.getAbsolutePath());
        try {
            ClassLoader cl = new URLClassLoader(new URL[] {file.toURI().toURL()}, getClass().getClassLoader());
            return Lists.newArrayList(ServiceLoader.load(Module.class, cl));
        } catch(MalformedURLException e) {
            log.e("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not get URL for file");
            return Lists.newArrayList();
        } catch(IOException e) {
            log.e("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not load mainifest file");
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

        Log log = injector.getInstance(Log.class);

        try {

            if (properties.get(RUN_WEBAPP) != null
                    && properties.get(RUN_WEBAPP).equalsIgnoreCase("false")) {
                log.d("Not starting webapp");
                return;
            }

            int port = 46874;
            try {
                if (properties.keySet().contains(WEBAPP_PORT))
                    port = Integer.parseInt(properties.get(WEBAPP_PORT));
            } catch (Throwable t) {
                log.w("Failed to parse property " + WEBAPP_PORT + ". Using default of " + port + " instead");
            }

            startJetty(injector, properties, port);
        } catch(Throwable t) {
            log.e("Failed to start web server", t);
        }
    }

    private void startJetty(Injector injector, PropertyRepository properties, int port) {

        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(CommsServiceImpl.class, "/Housemate/comms");
        servletHandler.addServletWithMapping(StaticFilesServlet.class, "/*");

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
        }
    }
}
