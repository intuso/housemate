package com.intuso.housemate.platform.pc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.intuso.housemate.api.HousemateException;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;
import com.intuso.utilities.log.writer.StdOutWriter;
import com.intuso.utilities.properties.api.PropertyContainer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public class PCLogModule extends AbstractModule {

    private final static String HOUSEMATE_LOG_DIR = "HOUSEMATE_LOG_DIR";
    public final static String LOG_LEVEL = "log.level";

    @Override
    protected void configure() {}

    @Provides
    public Log getLog(PropertyContainer properties) throws HousemateException {

        String dir = null;

        // get the base housemate log directory. If overridden, use that, else use env var value. If that not set then quit
        if(properties.get(HOUSEMATE_LOG_DIR) != null) {
            System.out.println("Overriding " + HOUSEMATE_LOG_DIR + " to " + properties.get(HOUSEMATE_LOG_DIR));
            dir = properties.get(HOUSEMATE_LOG_DIR);
        } else {
            dir = System.getenv(HOUSEMATE_LOG_DIR);
            if(dir == null)
                dir = System.getProperty("user.home") + File.separator + ".housemate" + File.separator + "log";
        }

        File logDirectory = new File(dir);

        // create it if it does not exist
        if(!logDirectory.exists())
            logDirectory.mkdirs();

        // create it if it does not exist
        if(!logDirectory.exists())
            logDirectory.mkdirs();

        try {
            com.intuso.utilities.log.writer.FileWriter fileWriter = new com.intuso.utilities.log.writer.FileWriter(LogLevel.valueOf(properties.get(LOG_LEVEL)), logDirectory.getAbsolutePath() + File.separator + "housemate.log");
            StdOutWriter stdOutWriter = new StdOutWriter(LogLevel.DEBUG);
            List<LogWriter> logWriters = Arrays.asList(new LogWriter[]{fileWriter, stdOutWriter});
            return new Log("Housemate", logWriters);
        } catch(IOException e) {
            throw new HousemateException("Failed to create main app log", e);
        }
    }
}
