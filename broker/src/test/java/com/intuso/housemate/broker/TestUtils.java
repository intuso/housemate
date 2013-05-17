package com.intuso.housemate.broker;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.resources.RegexMatcher;
import com.intuso.housemate.core.resources.RegexMatcherFactory;
import com.intuso.housemate.proxy.ProxyResources;
import com.intuso.housemate.proxy.simple.SimpleProxyFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 15/05/13
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class TestUtils {

    public static BrokerServerEnvironment startBroker(int port) throws HousemateException {
        return App.start(new String[]{"-broker.port", Integer.toString(port)});
    }

    public static ProxyResources<SimpleProxyFactory.All> createProxyRootResources(BrokerServerEnvironment environment) {
        return new ProxyResources<SimpleProxyFactory.All>(environment.getGeneralResources().getLog(),
                environment.getGeneralResources().getProperties(),
                environment.getGeneralResources().getComms(),
                new SimpleProxyFactory.All(),
                new RegexMatcherFactory() {
                    @Override
                    public RegexMatcher createRegexMatcher(String pattern) {
                        return new BrokerServerEnvironment.RM(pattern);
                    }
                });
    }
}
