package com.intuso.housemate.api;

import com.intuso.housemate.api.comms.ProxyCommsImpl;
import com.intuso.housemate.api.comms.RealCommsImpl;
import com.intuso.housemate.api.object.TestProxyRoot;
import com.intuso.housemate.api.object.TestRealRoot;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyResources;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.writer.StdOutWriter;
import com.intuso.utilities.properties.api.PropertyContainer;
import org.junit.Ignore;

import java.util.regex.Pattern;

/**
 */
@Ignore
public class TestEnvironment {

    public final static TestEnvironment TEST_INSTANCE = new TestEnvironment();

    private final PropertyContainer properties = new PropertyContainer();
    private final Log log = new Log();

    private final RealResources realResources;
    private final Resources genericResources;
    private final SimpleProxyResources<SimpleProxyFactory.All> proxyResources;
    private final SimpleProxyResources<NoChildrenProxyObjectFactory> proxyNoChildrenResources;

    private final TestRealRoot realRoot;
    private final TestProxyRoot proxyRoot;

    public TestEnvironment() {

        // init the log
        log.addWriter(new StdOutWriter(LogLevel.DEBUG));

        // set up the generic resources
        genericResources = new TestResources(log, properties);

        // create things needed for real/proxy resources
        RealCommsImpl realComms = new RealCommsImpl(genericResources);
        ProxyCommsImpl proxyComms = new ProxyCommsImpl(genericResources);

        // create real/proxy resources
        realResources = new RealResources(log, properties, realComms);
        proxyResources = new SimpleProxyResources<SimpleProxyFactory.All>(log, properties, proxyComms,
                new SimpleProxyFactory.All(),
                new RegexMatcherFactory() {
                    @Override
                    public RegexMatcher createRegexMatcher(String pattern) {
                        return new RM(pattern);
                    }
        });

        proxyNoChildrenResources = SimpleProxyFactory.noFactoryType(proxyResources);
        realRoot = new TestRealRoot(realResources);
        proxyRoot = new TestProxyRoot(proxyResources, proxyResources);
        realComms.setProxyRoot(proxyRoot);
        proxyComms.setRealRoot(realRoot);
        realRoot.init();
    }

    public RealResources getRealResources() {
        return realResources;
    }

    public SimpleProxyResources<SimpleProxyFactory.All> getProxyResources() {
        return proxyResources;
    }

    public SimpleProxyResources<NoChildrenProxyObjectFactory> getProxyNoChildrenResources() {
        return proxyNoChildrenResources;
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
