package com.intuso.housemate.platform.pc;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.common.base.Preconditions;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by tomc on 03/03/16.
 */
public class Logback {

    private static final String LOGBACK_FILE = "logback.xml";

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                cleanup();
            }
        });
    }

    public static void configure(File configDir) {
        File logbackFile = new File(configDir, LOGBACK_FILE);
        Preconditions.checkState(logbackFile.exists(), "Could not find logback configuration at %s", logbackFile.getAbsoluteFile());
        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        final JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(context);
        context.reset();
        try {
            joranConfigurator.doConfigure(logbackFile);
        } catch (JoranException ignored) {}
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }

    public static void cleanup() {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
    }
}
