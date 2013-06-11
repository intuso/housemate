package com.intuso.housemate.broker;

import com.google.common.collect.Lists;
import com.intuso.housemate.annotations.processor.AnnotationProcessor;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.broker.client.LocalClient;
import com.intuso.housemate.broker.comms.MainRouter;
import com.intuso.housemate.broker.factory.ConditionFactory;
import com.intuso.housemate.broker.factory.ConsequenceFactory;
import com.intuso.housemate.broker.factory.DeviceFactory;
import com.intuso.housemate.broker.object.BrokerProxyResourcesImpl;
import com.intuso.housemate.broker.object.BrokerRealResourcesImpl;
import com.intuso.housemate.broker.object.LifecycleHandlerImpl;
import com.intuso.housemate.broker.object.bridge.BrokerBridgeResources;
import com.intuso.housemate.broker.object.bridge.RootObjectBridge;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.general.BrokerGeneralRootObject;
import com.intuso.housemate.broker.plugin.MainPlugin;
import com.intuso.housemate.broker.storage.BrokerObjectStorage;
import com.intuso.housemate.broker.storage.Storage;
import com.intuso.housemate.broker.storage.impl.SjoerdDB;
import com.intuso.housemate.comms.transport.socket.server.SocketServer;
import com.intuso.housemate.object.broker.proxy.BrokerProxyFactory;
import com.intuso.housemate.object.broker.proxy.BrokerProxyRootObject;
import com.intuso.housemate.object.broker.real.BrokerRealRootObject;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;
import com.intuso.utilities.log.writer.FileWriter;
import com.intuso.utilities.log.writer.StdOutWriter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.JarResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

/**
 * Platform implementation for a broker. Works the same was as the PC implementation in terms of getting properties
 * and overriding based on command line arguments, Main difference is that the Comms implementation is different
 * and some methods are unsupported as they should not be used by the broker.
 * @author tclabon
 *
 */
public class BrokerServerEnvironment {

    public final static String HOUSEMATE_CONFIG_DIR = "HOUSEMATE_CONFIG_DIR";
    public final static String HOUSEMATE_LOG_DIR = "HOUSEMATE_LOG_DIR";
    public final static String HOUSEMATE_PROPS_FILE = "housemate.props";
    public final static String PLUGINS_DIR_NAME= "plugins";
    public final static String LOG_LEVEL = "log.level";
    public final static String BROKER_NAME = "broker.name";
    public final static String RUN_WEBAPP = "webapp.run";
    public final static String WEBAPP_PORT = "webapp.port";

    private final static String WEBAPP_FOLDER = "webapp";
    private final static String WEBAPP_NAME = "housemate";

    public final File config_dir;

	private final Map<String, String> properties;
    private final Log log;
    private final MainRouter comms;
    private final Storage storage;
    private final DeviceFactory deviceFactory;
    private final ConditionFactory conditionFactory;
    private final ConsequenceFactory consequenceFactory;

    private final BrokerGeneralResources generalResources;
    private final BrokerRealResourcesImpl realResources;
    private final BrokerBridgeResources bridgeResources;
    private final BrokerProxyResourcesImpl<BrokerProxyFactory.All> proxyResources;
    private final RealResources clientResources;

	/**
	 * Create a environment instance
	 * @param args the command line args that the broker was run with
	 * @throws HousemateException
	 */
	public BrokerServerEnvironment(String args[]) throws HousemateException {

		// convert the command line args into a map of values that are set
		Map<String, String> overrides = parseArgs(args);

		String dir;
		// get the base housemate config directory. If overridden, use that, else use env var value. If that not set then quit
		if(overrides.get(HOUSEMATE_CONFIG_DIR) != null) {
			System.out.println("Overriding " + HOUSEMATE_CONFIG_DIR + " to " + overrides.get(HOUSEMATE_CONFIG_DIR));
			dir = overrides.get(HOUSEMATE_CONFIG_DIR);
			overrides.remove(HOUSEMATE_CONFIG_DIR);
		} else {
			dir = System.getenv(HOUSEMATE_CONFIG_DIR);
			if(dir == null)
				dir = System.getProperty("user.home") + File.separator + ".housemate";
		}
		config_dir = new File(dir);

        // create the directory if it does not exist
        if(!config_dir.exists())
            config_dir.mkdirs();

		// get the base housemate log directory. If overridden, use that, else use env var value. If that not set then quit
		if(overrides.get(HOUSEMATE_LOG_DIR) != null) {
			System.out.println("Overriding " + HOUSEMATE_LOG_DIR + " to " + overrides.get(HOUSEMATE_LOG_DIR));
			dir = overrides.get(HOUSEMATE_LOG_DIR);
			overrides.remove(HOUSEMATE_LOG_DIR);
		} else {
			dir = System.getenv(HOUSEMATE_LOG_DIR);
			if(dir == null)
				dir = System.getProperty("user.home") + File.separator + ".housemate" + File.separator + "log";
		}
		File top_log_dir = new File(dir);

        // create it if it does not exist
        if(!top_log_dir.exists())
            top_log_dir.mkdirs();

        // for the broker we use the broker subdir
        File log_dir = new File(top_log_dir, "broker");

        // create it if it does not exist
        if(!log_dir.exists())
            log_dir.mkdirs();

		// init the properties
		properties = new HashMap<String, String>();

		// get the props file
		File props_file = new File(config_dir, HOUSEMATE_PROPS_FILE);
		if(!props_file.exists()) {
			System.out.println("Could not find broker properties file \"" + props_file.getAbsolutePath() + "\". Creating a new one with default settings");
            createDefaultPropsFile(props_file);
		}

		// load the props from the file
		try {
            Properties fileProps = new Properties();
			fileProps.load(new FileInputStream(props_file));
            for(String key : fileProps.stringPropertyNames())
                properties.put(key, fileProps.getProperty(key));
		} catch (FileNotFoundException e) {
			// Would have logged above!
			System.err.println("Could not find broker properties file \"" + props_file.getAbsolutePath() + "\"");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not read broker properties from file");
			System.exit(0);
		}

		// override any properties from the props file that are specified on the command line
		for(String prop_name : overrides.keySet()) {
			if(properties.get(prop_name) != null)
				System.out.println("Overriding prop file setting of " + prop_name + " to " + overrides.get(prop_name));
			else
				System.out.println("Setting custom property " + prop_name + " to " + overrides.get(prop_name));
			properties.put(prop_name, overrides.get(prop_name));
		}

        try {
            FileWriter fileWriter = new FileWriter(LogLevel.valueOf(properties.get(LOG_LEVEL)),
                    log_dir.getAbsolutePath() + File.separator + "housemate.log");
            List<LogWriter> logWriters = Arrays.asList(fileWriter, new StdOutWriter(LogLevel.DEBUG));
            log = new Log("Housemate", logWriters);
        } catch(IOException e) {
            throw new HousemateException("Failed to create main app log", e);
        }

        storage = new SjoerdDB(config_dir.getAbsolutePath() + File.separator + "database");

        generalResources = new BrokerGeneralResources(log, properties);
        generalResources.setRoot(new BrokerGeneralRootObject(generalResources));

        realResources = new BrokerRealResourcesImpl(generalResources);
        bridgeResources = new BrokerBridgeResources(generalResources);
        proxyResources = new BrokerProxyResourcesImpl<BrokerProxyFactory.All>(generalResources, new BrokerProxyFactory.All());

        comms = new MainRouter(generalResources);
        generalResources.setMainRouter(comms);

        clientResources = new RealResources(log, properties, comms);
        generalResources.setClientResources(clientResources);

        deviceFactory = new DeviceFactory(generalResources);
        conditionFactory = new ConditionFactory(generalResources);
        consequenceFactory = new ConsequenceFactory(generalResources);

        generalResources.setLifecycleHandler(new LifecycleHandlerImpl(generalResources));

        initGeneralResources();

        generalResources.setClient(new LocalClient(generalResources));

        initResources();
        comms.start();

        generalResources.setAnnotationParser(new AnnotationProcessor(generalResources.getLog(),
                getGeneralResources().getClient().getRoot().getTypes()));
        loadPlugins();

        startWebapp();
    }

    public BrokerGeneralResources getGeneralResources() {
        return generalResources;
    }

    private void initGeneralResources() {
        generalResources.setStorage(new BrokerObjectStorage(storage, generalResources));
        generalResources.setRemoteClientManager(new RemoteClientManager(generalResources));
        generalResources.setDeviceFactory(deviceFactory);
        generalResources.setConditionFactory(conditionFactory);
        generalResources.setConsequenceFactory(consequenceFactory);
        generalResources.setRealResources(realResources);
        generalResources.setProxyResources(proxyResources);
        generalResources.setBridgeResources(bridgeResources);
        generalResources.setClientResources(clientResources);
    }

    protected void initResources() {
        realResources.setRoot(new BrokerRealRootObject(realResources));
        proxyResources.setRoot(new BrokerProxyRootObject(proxyResources));
        bridgeResources.setRoot(new RootObjectBridge(bridgeResources));
    }

    /**
	 * Parse the command line arguments into a map of properties that are set and their values
	 * @param args the command line arguments
	 * @return a map of properties that are set and their values
	 * @throws HousemateException
	 */
	private final Map<String, String> parseArgs(String args[]) throws HousemateException {
		if(args.length % 2 == 1)
			throw new HousemateException("Odd number of arguments to parse - must be even");

		Map<String, String> properties = new HashMap<String, String>(args.length / 2);

		for(int i = 0; i < args.length; i+=2) {
			if(!args[i].startsWith("-"))
				throw new HousemateException("Property name must start with \"-\"");
			properties.put(args[i].substring(1), args[i + 1]);
		}

		return properties;
	}

    private void createDefaultPropsFile(File file) throws HousemateException {
        try {
            BufferedWriter out = new BufferedWriter(new java.io.FileWriter(file));
            out.write(LOG_LEVEL + "=DEBUG\n");
            out.write(BROKER_NAME + "=My Broker\n");
            out.write(SocketServer.BROKER_PORT + "=46873\n");
            out.close();
        } catch(IOException e) {
            throw new HousemateException("Could not create default props file", e);
        }
    }

    private void loadPlugins() {
        // add the default plugin
        generalResources.addPlugin(new MainPlugin(generalResources));

        // discover plugins from local dir
        File pluginDirectory = new File(this.config_dir, PLUGINS_DIR_NAME);
        if(!pluginDirectory.exists())
            pluginDirectory.mkdir();
        if(pluginDirectory.isFile())
            log.w("Plugin path is not a directory");
        else {
            generalResources.getLog().d("Loading plugins from " + pluginDirectory.getAbsolutePath());
            for(File pluginFile : pluginDirectory.listFiles(new PluginFileFilter())) {
                for(PluginDescriptor plugin : loadPlugin(pluginFile))
                    generalResources.addPlugin(plugin);
            }
        }
    }

    private List<PluginDescriptor> loadPlugin(File file) {
        generalResources.getLog().d("Loading plugins from " + file.getAbsolutePath());
        try {
            ClassLoader cl = new URLClassLoader(new URL[] {file.toURI().toURL()}, PluginDescriptor.class.getClassLoader());
            Enumeration<URL> urls = cl.getResources("META-INF/MANIFEST.MF");
            List<PluginDescriptor> result = new ArrayList<PluginDescriptor>();
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Manifest mf = new Manifest(url.openStream());
                for(Map.Entry<Object, Object> attrEntry : mf.getMainAttributes().entrySet()) {
                    try {
                        result.addAll(processManifestAttribute(url, attrEntry.getKey(), attrEntry.getValue(), cl));
                    } catch(HousemateException e) {
                        generalResources.getLog().e("Failed to load plugin descriptor");
                        generalResources.getLog().st(e);
                    }
                }
                for(Map.Entry<String, Attributes> mfEntry : mf.getEntries().entrySet()) {
                    Attributes attrs = mfEntry.getValue();
                    for(Map.Entry<Object, Object> attrEntry : attrs.entrySet()) {
                        try {
                            result.addAll(processManifestAttribute(url, attrEntry.getKey(), attrEntry.getValue(), cl));
                        } catch(HousemateException e) {
                            generalResources.getLog().e("Failed to load plugin descriptor");
                            generalResources.getLog().st(e);
                        }
                    }
                }
            }
            return result;
        } catch(MalformedURLException e) {
            generalResources.getLog().e("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not get URL for file");
            return Lists.newArrayList();
        } catch(IOException e) {
            generalResources.getLog().e("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not load mainifest file");
            return Lists.newArrayList();
        }
    }

    private List<PluginDescriptor> processManifestAttribute(URL url, Object key, Object value, ClassLoader cl) throws HousemateException {
        List<PluginDescriptor> result = Lists.newArrayList();
        if(key != null && key.toString().equals(PluginDescriptor.MANIFEST_ATTRIBUTE)
                && value instanceof String) {
            generalResources.getLog().d("Found " + PluginDescriptor.MANIFEST_ATTRIBUTE + " attribute in "+ url.toExternalForm());
            String[] classNames = ((String)value).split(",");
            for(String className : classNames) {
                PluginDescriptor descriptor = null;
                try {
                    generalResources.getLog().d("Loading plugin class " + className);
                    Class<?> clazz = Class.forName(className, true, cl);
                    if(PluginDescriptor.class.isAssignableFrom(clazz)) {
                        descriptor = (PluginDescriptor) clazz.getConstructor().newInstance();
                        generalResources.getLog().d("Successfully loaded plugin class " + className);
                    } else
                        throw new HousemateException(clazz.getName() + " is not assignable to " + PluginDescriptor.class.getName());
                } catch(ClassNotFoundException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                } catch(InvocationTargetException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                } catch(InstantiationException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                } catch(IllegalAccessException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                }
                if(descriptor != null) {
                    try {
                        result.add(descriptor);
                    } catch(Throwable t) {
                        throw new HousemateException("Failed to initialise plugin descriptor " + className + " from " + url.toExternalForm(), t);
                    }
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

    private void startWebapp() throws HousemateException {
        if(generalResources.getProperties().get(RUN_WEBAPP) != null
                && generalResources.getProperties().get(RUN_WEBAPP).equalsIgnoreCase("false")) {
            generalResources.getLog().d("Not starting webapp");
            return;
        }
        File webappDirectory = new File(config_dir, WEBAPP_FOLDER);
        if(!webappDirectory.exists())
            webappDirectory.mkdir();
        File webappFile = new File(webappDirectory, WEBAPP_NAME + ".war");
        if(webappFile.isDirectory())
            webappFile.delete();
        if(!webappFile.exists()) {
            URL url = getClass().getResource("/" + webappFile.getName());
            if(url == null) {
                generalResources.getLog().e("Could not find existing webapp and could not find it in jar. Cannot start web interface");
                return;
            }
            copyWebapp(url, webappFile);
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
            if(generalResources.getProperties().containsKey(WEBAPP_PORT))
                port = Integer.parseInt(generalResources.getProperties().get(WEBAPP_PORT));
        } catch(Throwable t) {
            generalResources.getLog().w("Failed to parse property " + WEBAPP_PORT + ". Using default of " + port + " instead");
        }
        startJetty(port, webappDir);
    }

    private void copyWebapp(URL fromUrl, File toFile) throws HousemateException {
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
                generalResources.getLog().e("Failed to close input stream when copying webapp");
                generalResources.getLog().st(e);
            }
            try {
                if(os != null)
                    os.close();
            } catch(IOException e) {
                generalResources.getLog().e("Failed to close input stream when copying webapp");
                generalResources.getLog().st(e);
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

    private void startJetty(int port, File warFile) throws HousemateException {
        Server server = new Server(port);

        // Configure webapp provided as external WAR
        WebAppContext webapp = new WebAppContext();
        webapp.getServletContext().setAttribute("RESOURCES", clientResources);
        webapp.setContextPath("/");
        webapp.setWar(warFile.getAbsolutePath());
        server.setHandler(webapp);

        // Start the server
        try {
            server.start();
        } catch(Exception e) {
            throw new HousemateException("Failed to start internal webserver", e);
        }
    }

    public static class RM implements RegexMatcher {

        Pattern pattern;

        public RM(String regexPattern) {
            pattern = Pattern.compile(regexPattern);
        }

        @Override
        public boolean matches(String value) {
            return pattern.matcher(value).matches();
        }
    }
}
