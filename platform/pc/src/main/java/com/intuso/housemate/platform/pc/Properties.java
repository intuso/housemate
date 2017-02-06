package com.intuso.housemate.platform.pc;

import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.reader.commandline.CommandLinePropertyRepository;
import com.intuso.utilities.properties.reader.file.FilePropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public final static String HOUSEMATE_CONFIG_DIR = "conf.dir";
    public final static String HOUSEMATE_PROPS_FILE = "conf.file";
    public final static String APPLICATION_CONFIG_DIR = "application.conf.dir";
    public final static String APPLICATION_PROPS_FILE = "application.conf.file";

    private Properties() {}

    public static PropertyRepository create(ManagedCollectionFactory managedCollectionFactory, PropertyRepository defaultProperties, String[] args) {

        defaultProperties.set(HOUSEMATE_CONFIG_DIR, System.getProperty("user.home") + File.separator + ".housemate");
        defaultProperties.set(HOUSEMATE_PROPS_FILE, "housemate.props");
        defaultProperties.set(APPLICATION_CONFIG_DIR, "./");
        defaultProperties.set(APPLICATION_PROPS_FILE, "housemate.props");

        // read the command lines args now so we can use them to setup the file properties
        CommandLinePropertyRepository clProperties = new CommandLinePropertyRepository(managedCollectionFactory, defaultProperties, args);

        // read system file properties
        File configDirectory = new File(clProperties.get(HOUSEMATE_CONFIG_DIR));
        // create the directory if it does not exist
        if(!configDirectory.exists())
            configDirectory.mkdirs();

        Logback.configure(configDirectory);

        Logger logger = LoggerFactory.getLogger(Properties.class);

        // get the props file
        File props_file = new File(configDirectory, clProperties.get(HOUSEMATE_PROPS_FILE));
        FilePropertyRepository systemProperties = null;
        try {
            if(!props_file.exists())
                props_file.createNewFile();
            systemProperties = new FilePropertyRepository(managedCollectionFactory, defaultProperties, props_file);
        } catch (FileNotFoundException e) {
            logger.warn("Could not find system properties file \"" + props_file.getAbsolutePath() + "\"");
        } catch (IOException e) {
            logger.error("Could not read system properties from file");
            e.printStackTrace();
        }

        configDirectory = new File(clProperties.get(APPLICATION_CONFIG_DIR));
        // create the directory if it does not exist
        if(!configDirectory.exists())
            configDirectory.mkdirs();
        // get the props file
        props_file = new File(configDirectory, clProperties.get(APPLICATION_PROPS_FILE));
        FilePropertyRepository applicationProperties = null;
        try {
            if(!props_file.exists())
                props_file.createNewFile();
            applicationProperties = new FilePropertyRepository(managedCollectionFactory,
                    systemProperties != null ? systemProperties : defaultProperties, props_file);
        } catch (FileNotFoundException e) {
            System.out.println("WARN: Could not find application properties file \"" + props_file.getAbsolutePath() + "\"");
        } catch (IOException e) {
            System.err.println("ERROR: Could not read application properties from file");
            e.printStackTrace();
        }

        // wrap the defaults and file properties with the command line properties
        return new CommandLinePropertyRepository(managedCollectionFactory,
                applicationProperties != null ? applicationProperties :
                    systemProperties != null ? systemProperties : defaultProperties, args);
    }
}
