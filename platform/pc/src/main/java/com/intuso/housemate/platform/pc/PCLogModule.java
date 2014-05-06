package com.intuso.housemate.platform.pc;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;
import com.intuso.utilities.log.writer.FileWriter;
import com.intuso.utilities.log.writer.StdOutWriter;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

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

    public PCLogModule(WriteableMapPropertyRepository defaultProperties, String defaultLogName) {
        defaultProperties.set(HOUSEMATE_LOG_STDOUT, "true");
        defaultProperties.set(HOUSEMATE_LOG_STDOUT_LEVEL, LogLevel.ERROR.name());
        defaultProperties.set(HOUSEMATE_LOG_FILE, "true");
        defaultProperties.set(HOUSEMATE_LOG_FILE_LEVEL, LogLevel.DEBUG.name());
        defaultProperties.set(HOUSEMATE_LOG_FILE_PATH, System.getProperty("user.home") + File.separator + ".housemate" + File.separator + "log" + File.separator + defaultLogName);
    }

    @Override
    protected void configure() {}

    @Provides
    @Singleton
    public Log getLog(PropertyRepository properties) {

        Set<LogWriter> logWriters = Sets.newHashSet();

        // stdout writer
        if(Boolean.parseBoolean(properties.get(HOUSEMATE_LOG_STDOUT)))
            logWriters.add(new StdOutWriter(LogLevel.valueOf(properties.get(HOUSEMATE_LOG_STDOUT_LEVEL))));

        // file writer
        if(Boolean.parseBoolean(properties.get(HOUSEMATE_LOG_FILE))) {
            try {
                logWriters.add(new FileWriter(LogLevel.valueOf(properties.get(HOUSEMATE_LOG_FILE_LEVEL)),
                        properties.get(HOUSEMATE_LOG_FILE_PATH)));
            } catch(IOException e) {
                System.err.println("Failed to create log file writer");
                e.printStackTrace();
            }
        }

        // return the log
        return new Log(logWriters);
    }
}
