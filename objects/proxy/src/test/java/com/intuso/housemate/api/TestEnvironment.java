package com.intuso.housemate.api;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.api.comms.ProxyRouterImpl;
import com.intuso.housemate.api.comms.RealRouterImpl;
import com.intuso.housemate.api.object.TestProxyRoot;
import com.intuso.housemate.api.object.TestRealRoot;
import com.intuso.housemate.api.resources.RegexMatcher;
import org.junit.Ignore;

import java.util.regex.Pattern;

/**
 */
@Ignore
public class TestEnvironment {

    public final static TestEnvironment TEST_INSTANCE = new TestEnvironment();

    private final Injector injector;
    private final TestRealRoot realRoot;
    private final TestProxyRoot proxyRoot;

    public TestEnvironment() {

        injector = Guice.createInjector(new TestModule());

        realRoot = injector.getInstance(TestRealRoot.class);
        proxyRoot = injector.getInstance(TestProxyRoot.class);
        injector.getInstance(RealRouterImpl.class).setProxyRoot(proxyRoot);
        injector.getInstance(ProxyRouterImpl.class).setRealRoot(realRoot);
        realRoot.init();
    }

    public Injector getInjector() {
        return injector;
    }

    public TestRealRoot getRealRoot() {
        return realRoot;
    }

    public TestProxyRoot getProxyRoot() {
        return proxyRoot;
    }

    private class RM implements RegexMatcher {

        Pattern pattern;

        public RM(String regexPattern) {
            pattern = Pattern.compile(regexPattern);
        }

        @Override
        public boolean matches(String value) {
            return pattern.matcher(value).matches();
        }
    }
}
