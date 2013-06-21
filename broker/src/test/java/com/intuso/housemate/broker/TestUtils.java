package com.intuso.housemate.broker;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 15/05/13
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class TestUtils {

    public static BrokerServerEnvironment startBroker(int port) {
        try {
            return App.start(new String[]{"-broker.port", Integer.toString(port), "-webapp.run", "false"});
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Failed to start broker", e);
        }
    }

    public static ProxyResources<SimpleProxyFactory.All> createProxyRootResources(BrokerServerEnvironment environment) {
        return new ProxyResources<SimpleProxyFactory.All>(environment.getGeneralResources().getLog(),
                environment.getGeneralResources().getProperties(), environment.getGeneralResources().getMainRouter(),
                new SimpleProxyFactory.All(),
                new RegexMatcherFactory() {
                    @Override
                    public RegexMatcher createRegexMatcher(String pattern) {
                        return new BrokerServerEnvironment.RM(pattern);
                    }
                });
    }
}
