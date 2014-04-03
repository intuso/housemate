package com.intuso.housemate.web.client;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/01/14
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */
public class GWTLog extends Log {

    @Inject
    public GWTLog() {
        super(new GWTLogWriter(LogLevel.DEBUG));
    }

    private static class GWTLogWriter extends LogWriter {

        /**
         * Create a new log writer
         *
         * @param level the level to filter at
         */
        protected GWTLogWriter(LogLevel level) {
            super(level);
        }

        @Override
        protected void _write(LogLevel level, String message, Throwable t) {
            GWT.log(level + ": " + message, t);
        }
    }
}
