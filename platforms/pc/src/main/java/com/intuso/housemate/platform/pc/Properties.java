package com.intuso.housemate.platform.pc;

import com.intuso.utilities.properties.api.PropertyContainer;
import com.intuso.utilities.properties.reader.commandline.CommandLineReader;
import com.intuso.utilities.properties.reader.file.FileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class Properties {

    public final static String HOUSEMATE_CONFIG_DIR = "HOUSEMATE_CONFIG_DIR";
    public final static String HOUSEMATE_PROPS_FILE = "housemate.props";

    private Properties() {}

    public static PropertyContainer init(String[] args) {

        PropertyContainer properties = new PropertyContainer();

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

        File configDirectory = new File(dir);

        // create the directory if it does not exist
        if(!configDirectory.exists())
            configDirectory.mkdirs();

        // get the props file
        File props_file = new File(configDirectory, HOUSEMATE_PROPS_FILE);
        if(props_file.exists()) {
            try {
                properties.read(new FileReader("propertiesFile", 1, props_file));
            } catch (FileNotFoundException e) {
                System.out.println("WARN: Could not find server properties file \"" + props_file.getAbsolutePath() + "\"");
            } catch (IOException e) {
                System.err.println("ERROR: Could not read server properties from file");
                e.printStackTrace();
            }
        }

        return properties;
    }
}
