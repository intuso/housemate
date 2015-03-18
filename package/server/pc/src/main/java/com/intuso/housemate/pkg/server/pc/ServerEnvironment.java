package com.intuso.housemate.pkg.server.pc;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.plugin.api.PluginModule;
import com.intuso.housemate.plugin.host.PluginManager;
import com.intuso.housemate.server.storage.ServerObjectLoader;
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
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

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

	/**
	 * Create a environment instance
	 * @param args the command line args that the server was run with
	 * @throws HousemateException
	 */
	public ServerEnvironment(String args[]) throws HousemateException {

        ListenersFactory listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<LISTENER>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
        PropertyRepository properties = Properties.create(listenersFactory, defaultProperties, args);
        setExtraDefaults(defaultProperties);

        Injector injector = Guice.createInjector(new PCServerModule(defaultProperties, properties));

        Log log = injector.getInstance(Log.class);

        log.d("Starting server");
        injector.getInstance(com.intuso.housemate.server.Server.class).start();
        log.d("Started server");

        log.d("Loading plugins");
        loadPlugins(injector, properties);
        log.d("Loaded plugins");

        log.d("Loading objects");
        injector.getInstance(ServerObjectLoader.class).loadObjects();
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
                for(Class<? extends PluginModule> pluginModuleClass : getPluginModuleClasses(pluginFile, log))
                    pluginManager.addPlugin(pluginModuleClass);
            }
        }
    }

    private void loadSharedJNILibs() {
        SerialPortList.getPortNames();
        //CommPortIdentifier.getPortIdentifiers();
    }

    private List<Class<? extends PluginModule>> getPluginModuleClasses(File file, Log log) {
        log.d("Loading plugins from " + file.getAbsolutePath());
        try {
            ClassLoader cl = new URLClassLoader(new URL[] {file.toURI().toURL()}, PluginModule.class.getClassLoader());
            Enumeration<URL> urls = cl.getResources("META-INF/MANIFEST.MF");
            List<Class<? extends PluginModule>> result = Lists.newArrayList();
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Manifest mf = new Manifest(url.openStream());
                for(Map.Entry<Object, Object> attrEntry : mf.getMainAttributes().entrySet()) {
                    try {
                        result.addAll(processManifestAttribute(url, attrEntry.getKey(), attrEntry.getValue(), cl, log));
                    } catch(HousemateException e) {
                        log.e("Failed to load plugin descriptor", e);
                    }
                }
                for(Map.Entry<String, Attributes> mfEntry : mf.getEntries().entrySet()) {
                    Attributes attrs = mfEntry.getValue();
                    for(Map.Entry<Object, Object> attrEntry : attrs.entrySet()) {
                        try {
                            result.addAll(processManifestAttribute(url, attrEntry.getKey(), attrEntry.getValue(), cl, log));
                        } catch(HousemateException e) {
                            log.e("Failed to load plugin descriptor", e);
                        }
                    }
                }
            }
            return result;
        } catch(MalformedURLException e) {
            log.e("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not get URL for file");
            return Lists.newArrayList();
        } catch(IOException e) {
            log.e("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not load mainifest file");
            return Lists.newArrayList();
        }
    }

    private List<Class<? extends PluginModule>> processManifestAttribute(URL url, Object key, Object value, ClassLoader cl, Log log)
            throws HousemateException {
        List<Class<? extends PluginModule>> result = Lists.newArrayList();
        if(key != null && key.toString().equals(PluginModule.MANIFEST_ATTRIBUTE)
                && value instanceof String) {
            log.d("Found " + PluginModule.MANIFEST_ATTRIBUTE + " attribute in "+ url.toExternalForm());
            String[] classNames = ((String)value).split(",");
            for(String className : classNames) {
                try {
                    log.d("Loading plugin class " + className);
                    Class<?> clazz = Class.forName(className, true, cl);
                    if(PluginModule.class.isAssignableFrom(clazz)) {
                        log.d("Successfully loaded plugin class " + className);
                        result.add((Class<? extends PluginModule>) clazz);
                    } else
                        throw new HousemateException(clazz.getName() + " is not assignable to " + PluginModule.class.getName());
                } catch(ClassNotFoundException e) {
                    throw new HousemateException("Could not load plugin module class " + className + " from " + url.toExternalForm(), e);
                }
            }
        }
        return result;
    }

    private class PluginFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && pathname.getName().endsWith(".jar");
        }
    }

    private void startWebapp(Injector injector, PropertyRepository properties) throws HousemateException {

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

    private void startJetty(Injector injector, PropertyRepository properties, int port)
            throws HousemateException {

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
            throw new HousemateException("Failed to register housemate static web resources", e);
        }
        handler.addEventListener(injector.getInstance(ContextListener.class));

        Server server = new Server(port);
        server.setHandler(handler);
        server.setStopAtShutdown(true);

        // Start the server
        try {
            server.start();
        } catch(Exception e) {
            throw new HousemateException("Failed to start internal webserver", e);
        }
    }
}
