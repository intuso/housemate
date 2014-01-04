package com.intuso.housemate.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.api.resources.ResourcesImpl;
import com.intuso.housemate.web.client.object.GWTProxyFactory;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeatureFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 */
public class GWTEnvironment {
    
    private final Log log;
    private final GWTComms comms;

    private final ResourcesImpl resources;
    private final GWTResources gwtResources;

    public GWTEnvironment(PropertyContainer properties, GWTProxyFeatureFactory featureFactory, AsyncCallback<Void> connectCallback) {
        super();

        GWTLogWriter logWriter = new GWTLogWriter(LogLevel.DEBUG);
        log = new Log("Housemate");
        log.addWriter(logWriter);

        resources = new ResourcesImpl(log, properties);

        this.comms = new GWTComms(resources, connectCallback);

        gwtResources = new GWTResources(log, properties, comms, new GWTProxyFactory.All(),
                featureFactory, new RegexMatcherFactory() {
            @Override
            public RegexMatcher createRegexMatcher(String pattern) {
                return new RM(pattern);
            }
        });
    }

    public GWTResources getGwtResources() {
        return gwtResources;
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
        protected void _write(LogLevel level, String message, Throwable t) {
            GWT.log(level + ": " + message, t);
        }
    }
}
