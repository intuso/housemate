package com.intuso.housemate.broker;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.comms.transport.rest.RestServer;
import com.intuso.housemate.comms.transport.socket.server.SocketServer;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyResources;

import java.util.Map;

/**
 */
public class TestUtils {

    public static BrokerServerEnvironment startBroker(int socketPort, int restPort) {
        try {
            return App.start(new String[]{"-" + SocketServer.PORT, Integer.toString(socketPort),
                    "-" + RestServer.PORT, Integer.toString(restPort),
                    "-webapp.run", "false"});
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Failed to start broker", e);
        }
    }

    public static SimpleProxyResources<SimpleProxyFactory.All> createProxyRootResources(BrokerServerEnvironment environment) {
        Injector injector = environment.getInjector();
        return new SimpleProxyResources<SimpleProxyFactory.All>(injector.getInstance(com.intuso.utilities.log.Log.class),
                injector.getInstance(new Key<Map<String, String>>() {}), injector.getInstance(Router.class),
                new SimpleProxyFactory.All(),
                new RegexMatcherFactory() {
                    @Override
                    public RegexMatcher createRegexMatcher(String pattern) {
                        return new BrokerServerEnvironment.RM(pattern);
                    }
                });
    }
}
