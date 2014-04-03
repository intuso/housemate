package com.intuso.housemate.pkg.server.pc;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.plugin.api.PluginModule;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.storage.ServerObjectLoader;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.JarResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.*;
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
    private final static String WEBAPP_PATH = "webapp.path";

    private final static String WEBAPP_FOLDER = "webapp";
    private final static String WEBAPP_NAME = "housemate";
    private final static String DEFAULT_WEBAPP_PATH = "/housemate";

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

        injector.getInstance(com.intuso.housemate.server.Server.class).start();

        loadPlugins(injector, properties);
        injector.getInstance(ServerObjectLoader.class).loadObjects();
        startWebapp(injector, properties);
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

        if(properties.get(RUN_WEBAPP) != null
                && properties.get(RUN_WEBAPP).equalsIgnoreCase("false")) {
            log.d("Not starting webapp");
            return;
        }
        File webappDirectory = new File(properties.get(Properties.HOUSEMATE_CONFIG_DIR) + File.separator + WEBAPP_FOLDER);
        if(!webappDirectory.exists())
            webappDirectory.mkdir();
        File webappFile = new File(webappDirectory, WEBAPP_NAME + ".war");
        if(webappFile.isDirectory())
            webappFile.delete();
        if(!webappFile.exists()) {
            URL url = getClass().getResource("/" + webappFile.getName());
            if(url == null) {
                log.e("Could not find existing webapp and could not find it in jar. Cannot start web interface");
                return;
            }
            copyWebapp(url, webappFile, log);
        }
        File webappDir = new File(webappDirectory, WEBAPP_NAME);
        if(webappDir.isFile())
            webappDir.delete();
        if(!webappDir.exists()) {
            webappDir.mkdir();
            unpackWar(webappFile, webappDir);
        }
        int port = 46874;
        try {
            if(properties.keySet().contains(WEBAPP_PORT))
                port = Integer.parseInt(properties.get(WEBAPP_PORT));
        } catch(Throwable t) {
            log.w("Failed to parse property " + WEBAPP_PORT + ". Using default of " + port + " instead");
        }
        startJetty(injector, properties, port, webappDir);
    }

    private void copyWebapp(URL fromUrl, File toFile, Log log) throws HousemateException {
        InputStream is = null;
        OutputStream os = null;
        try {
            try {
                is = fromUrl.openStream();
            } catch(IOException e) {
                throw new HousemateException("Failed to open stream to copy webapp from");
            }
            try {
                os = new FileOutputStream(toFile);
            } catch(IOException e) {
                throw new HousemateException("Failed to open stream to write webapp to");
            }
            byte[] buffer = new byte[1024];
            int read;
            try {
                while((read = is.read(buffer)) > 0)
                    os.write(buffer, 0, read);
            } catch (IOException e) {
                throw new HousemateException("Failed to copy webapp", e);
            }
        } finally {
            try {
                if(is != null)
                    is.close();
            } catch(IOException e) {
                log.e("Failed to close input stream when copying webapp", e);
            }
            try {
                if(os != null)
                    os.close();
            } catch(IOException e) {
                log.e("Failed to close input stream when copying webapp", e);
            }
        }
    }

    private void unpackWar(File webappFile, File webappDir) throws HousemateException {
        try {
            Resource jarWebApp = JarResource.newJarResource(FileResource.newResource(webappFile));
            jarWebApp.copyTo(webappDir);
        } catch(IOException e) {
            throw new HousemateException("Error unpacking webapp", e);
        }
    }

    private void startJetty(Injector injector, PropertyRepository properties, int port, File warFile)
            throws HousemateException {
        Server server = new Server(port);

        // Configure webapp provided as external WAR
        WebAppContext webapp = new WebAppContext();
        webapp.getServletContext().setAttribute("INJECTOR", injector);
        webapp.setContextPath(properties.get(WEBAPP_PATH) != null ? properties.get(WEBAPP_PATH) : DEFAULT_WEBAPP_PATH);
        webapp.setWar(warFile.getAbsolutePath());
        server.setHandler(webapp);

        // Start the server
        try {
            server.start();
        } catch(Exception e) {
            throw new HousemateException("Failed to start internal webserver", e);
        }
    }
}
