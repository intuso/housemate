package com.intuso.housemate.platform.pc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.intuso.housemate.api.HousemateException;
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
public class PCPropertiesModule extends AbstractModule {

    private final static String HOUSEMATE_CONFIG_DIR = "HOUSEMATE_CONFIG_DIR";
    private final static String HOUSEMATE_PROPS_FILE = "housemate.props";

    private final String[] args;

    public PCPropertiesModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() {}

    @Provides
    public void getPropertyContainer() throws HousemateException {

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
                // Would have logged above!
                throw new HousemateException("Could not find server properties file \"" + props_file.getAbsolutePath() + "\"", e);
            } catch (IOException e) {
                throw new HousemateException("Could not read server properties from file", e);
            }
        }
    }
}
