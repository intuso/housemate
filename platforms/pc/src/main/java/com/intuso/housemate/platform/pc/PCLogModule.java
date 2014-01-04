package com.intuso.housemate.platform.pc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;
import com.intuso.utilities.log.writer.FileWriter;
import com.intuso.utilities.log.writer.StdOutWriter;
import com.intuso.utilities.properties.api.PropertyContainer;
import com.intuso.utilities.properties.api.PropertyValue;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public class PCLogModule extends AbstractModule {

    private final static String HOUSEMATE_LOG_STDOUT = "log.stdout";
    private final static String HOUSEMATE_LOG_STDOUT_LEVEL = "log.stdout.level";
    private final static String HOUSEMATE_LOG_FILE = "log.file";
    private final static String HOUSEMATE_LOG_FILE_LEVEL = "log.file.level";
    private final static String HOUSEMATE_LOG_FILE_PATH = "log.file.path";

    private final PropertyContainer properties;

    public PCLogModule(PropertyContainer properties, String defaultLogName) {
        this.properties = properties;
        properties.set(HOUSEMATE_LOG_STDOUT, new PropertyValue("default", 0, "true"));
        properties.set(HOUSEMATE_LOG_STDOUT_LEVEL, new PropertyValue("default", 0, LogLevel.ERROR.name()));
        properties.set(HOUSEMATE_LOG_FILE, new PropertyValue("default", 0, "true"));
        properties.set(HOUSEMATE_LOG_FILE_LEVEL, new PropertyValue("default", 0, LogLevel.DEBUG.name()));
        properties.set(HOUSEMATE_LOG_FILE_PATH, new PropertyValue("default", 0, System.getProperty("user.home") + File.separator + ".housemate" + File.separator + "log" + File.separator + defaultLogName));
    }

    @Override
    protected void configure() {

        Multibinder<LogWriter> logWriters = Multibinder.newSetBinder(binder(), LogWriter.class);

        // stdout writer
        if(Boolean.parseBoolean(properties.get(HOUSEMATE_LOG_STDOUT)))
            logWriters.addBinding().toInstance(new StdOutWriter(LogLevel.valueOf(properties.get(HOUSEMATE_LOG_STDOUT_LEVEL))));

        // file writer
        if(Boolean.parseBoolean(properties.get(HOUSEMATE_LOG_FILE))) {
            try {
                logWriters.addBinding().toInstance(new FileWriter(LogLevel.valueOf(properties.get(HOUSEMATE_LOG_FILE_LEVEL)),
                        properties.get(HOUSEMATE_LOG_FILE_PATH)));
            } catch(IOException e) {
                System.err.println("Failed to create log file writer");
                e.printStackTrace();
            }
        }
    }

    @Provides
    @Singleton
    public Log getLog(Set<LogWriter> logWriters) {
        // return the log
        return new Log(logWriters);
    }
}
