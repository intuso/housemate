package com.intuso.housemate.broker;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.comms.transport.rest.RestServer;
import com.intuso.housemate.comms.transport.socket.server.SocketServer;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyResources;

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
        return new SimpleProxyResources<SimpleProxyFactory.All>(environment.getGeneralResources().getLog(),
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
