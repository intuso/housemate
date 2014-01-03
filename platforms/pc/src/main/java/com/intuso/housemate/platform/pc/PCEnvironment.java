package com.intuso.housemate.platform.pc;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.comms.transport.socket.client.SocketClient;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;
import com.intuso.utilities.log.writer.StdOutWriter;
import com.intuso.utilities.properties.api.PropertyContainer;
import com.intuso.utilities.properties.reader.commandline.CommandLineReader;
import com.intuso.utilities.properties.reader.file.FileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Platform implementation for PCs
 *
 */
public class PCEnvironment {

    public final static String LOG_LEVEL = "log.level";

    private final static String HOUSEMATE_CONFIG_DIR = "HOUSEMATE_CONFIG_DIR";
    private final static String HOUSEMATE_LOG_DIR = "HOUSEMATE_LOG_DIR";
    private final static String HOUSEMATE_PROPS_FILE = "housemate.props";

    private final File configDirectory;
    private final File logDirectory;
    private final Log appLog;
    private final SocketClient comms;

    /**
     * System Properties
     */
    private final PropertyContainer properties = new PropertyContainer();
    private final ClientResources resources;

    /**
     * Construct the environment instance
     * @param args the args that will be used to configure the environment instance
     * @throws HousemateException thrown if an instance already exists
     */
    public PCEnvironment(String args[]) throws HousemateException {

        // convert the command line args into a map of values that are set
        properties.read(new CommandLineReader("commandLine", 1, args));

        String dir;
        // get the base housemate config directory. If overridden, use that, else use env var value. If that not set then quit
        if(properties.get(HOUSEMATE_CONFIG_DIR) != null) {
            System.out.println("Overriding " + HOUSEMATE_CONFIG_DIR + " to " + properties.get(HOUSEMATE_CONFIG_DIR));
            dir = properties.get(HOUSEMATE_CONFIG_DIR);
        } else {
            dir = System.getenv(HOUSEMATE_CONFIG_DIR);
            if(dir == null)
                dir = System.getProperty("user.home") + File.separator + ".housemate";
        }
        configDirectory = new File(dir);

        // create the directory if it does not exist
        if(!configDirectory.exists())
            configDirectory.mkdirs();

        // get the base housemate log directory. If overridden, use that, else use env var value. If that not set then quit
        if(properties.get(HOUSEMATE_LOG_DIR) != null) {
            System.out.println("Overriding " + HOUSEMATE_LOG_DIR + " to " + properties.get(HOUSEMATE_LOG_DIR));
            dir = properties.get(HOUSEMATE_LOG_DIR);
        } else {
            dir = System.getenv(HOUSEMATE_LOG_DIR);
            if(dir == null)
                dir = System.getProperty("user.home") + File.separator + ".housemate" + File.separator + "log";
        }
        logDirectory = new File(dir);

        // create it if it does not exist
        if(!logDirectory.exists())
            logDirectory.mkdirs();

        // create it if it does not exist
        if(!logDirectory.exists())
            logDirectory.mkdirs();

        // get the props file
        File props_file = new File(configDirectory, HOUSEMATE_PROPS_FILE);
        if(!props_file.exists()) {
            System.out.println("Could not find server properties file \"" + props_file.getAbsolutePath() + "\". Creating a new one with default settings");
            createDefaultPropsFile(props_file);
        }

        // load the props from the file
        try {
            properties.read(new FileReader("propertiesFile", 1, props_file));
        } catch (FileNotFoundException e) {
            // Would have logged above!
            System.err.println("Could not find server properties file \"" + props_file.getAbsolutePath() + "\"");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not read server properties from file");
            System.exit(0);
        }

        try {
            com.intuso.utilities.log.writer.FileWriter fileWriter = new com.intuso.utilities.log.writer.FileWriter(LogLevel.valueOf(properties.get(LOG_LEVEL)), logDirectory.getAbsolutePath() + File.separator + "housemate.log");
            StdOutWriter stdOutWriter = new StdOutWriter(LogLevel.DEBUG);
            List<LogWriter> logWriters = Arrays.asList(new LogWriter[]{fileWriter, stdOutWriter});
            appLog = new Log("Housemate", logWriters);
        } catch(IOException e) {
            throw new HousemateException("Failed to create main app log", e);
        }

        resources = new ClientResources() {

            @Override
            public Router getRouter() {
                return comms;
            }

            @Override
            public Log getLog() {
                return appLog;
            }

            @Override
            public PropertyContainer getProperties() {
                return properties;
            }
        };
        comms = new SocketClient(resources);
    }

    public ClientResources getResources() {
        return resources;
    }

    /**
     * Convert the command line arguments into a map of properties and values
     * @param args the properties as an arg array. Even elements must start with a "-" followed by the prop name. Odd elements are the value for the preceding even element. (NB starts at 0)
     * @return a map of the properties
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

    public static class RM implements RegexMatcher {

        private final Pattern pattern;

        public RM(String regexPattern) {
            pattern = Pattern.compile(regexPattern);
        }

        @Override
        public boolean matches(String value) {
            return pattern.matcher(value).matches();
        }
    }

    private void createDefaultPropsFile(File file) throws HousemateException {
        try {
            BufferedWriter out = new BufferedWriter(new java.io.FileWriter(file));
            out.write(LOG_LEVEL + "=DEBUG\n");
            out.write(SocketClient.SERVER_HOST + "=localhost\n");
            out.write(SocketClient.SERVER_PORT + "=46873\n");
            out.close();
        } catch(IOException e) {
            throw new HousemateException("Could not create default props file", e);
        }
    }
}
