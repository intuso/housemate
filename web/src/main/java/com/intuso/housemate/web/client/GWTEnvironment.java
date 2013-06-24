package com.intuso.housemate.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.api.resources.SimpleResources;
import com.intuso.housemate.web.client.object.GWTProxyFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;

import java.util.Map;

/**
 */
public class GWTEnvironment {
    
    private final Log log;
    private final Map<String, String> properties;
    private final GWTComms comms;

    private final SimpleResources simpleResources;
    private final GWTResources resources;

    public GWTEnvironment(Map<String, String> properties, AsyncCallback<Void> connectCallback) {
        super();
        this.properties = properties;
        GWTLogWriter logWriter = new GWTLogWriter(LogLevel.DEBUG);
        log = new Log("Housemate");
        log.addWriter(logWriter);

        simpleResources = new SimpleResources(log, properties);

        this.comms = new GWTComms(simpleResources, connectCallback);

        resources = new GWTResources(log, properties, comms, new GWTProxyFactory.All(), new RegexMatcherFactory() {
            @Override
            public RegexMatcher createRegexMatcher(String pattern) {
                return new RM(pattern);
            }
        });
    }

    public GWTResources getResources() {
        return resources;
    }

    private class RM implements RegexMatcher {

        RegExp pattern;
        
        public RM(String regexPattern) {
            pattern = RegExp.compile(regexPattern);
        }
        
        @Override
        public boolean matches(String value) {
            return pattern.test(value);
        }
    }

    private class GWTLogWriter extends LogWriter {

        /**
         * Create a new log writer
         *
         * @param level the level to filter at
         */
        protected GWTLogWriter(LogLevel level) {
            super(level);
        }

        @Override
        protected void _write(LogLevel level, String message) {
            GWT.log(level + ": " + message);
        }
    }
}
