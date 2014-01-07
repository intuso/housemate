package com.intuso.housemate.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.AbstractModule;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeatureFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 */
public class GWTEnvironment extends AbstractModule {

    private final PropertyContainer properties;
    private final Log log;
    private final GWTProxyFeatureFactory featureFactory;
    private final GWTRouter router;

    public GWTEnvironment(PropertyContainer properties, GWTProxyFeatureFactory featureFactory, AsyncCallback<Void> connectCallback) {
        super();

        this.properties = properties;
        this.featureFactory = featureFactory;

        GWTLogWriter logWriter = new GWTLogWriter(LogLevel.DEBUG);
        log = new Log();
        log.addWriter(logWriter);

        this.router = new GWTRouter(log, connectCallback);
    }

    @Override
    protected void configure() {
        bind(PropertyContainer.class).toInstance(properties);
        bind(ProxyFeatureFactory.class).toInstance(featureFactory);
        bind(Log.class).toInstance(log);
        bind(Router.class).toInstance(router);
        bind(RegexMatcherFactory.class).to(RMF.class);
    }

    private class RMF implements RegexMatcherFactory {

        @Override
        public RegexMatcher createRegexMatcher(String pattern) {
            return new RM(pattern);
        }
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
