package com.intuso.housemate.server;

/**
 */
public class TestUtils {

    /*public static ServerEnvironment startServer(int socketPort, int restPort) {
        try {
            return App.start(new String[]{"-" + SocketServer.PORT, Integer.toString(socketPort),
                    "-" + RestServer.PORT, Integer.toString(restPort),
                    "-webapp.run", "false"});
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Failed to start server", e);
        }
    }

    public static SimpleProxyResources<SimpleProxyFactory.All> createProxyRootResources(ServerEnvironment environment) {
        Injector injector = environment.getInjector();
        return new SimpleProxyResources<SimpleProxyFactory.All>(injector.getInstance(com.intuso.utilities.log.Log.class),
                injector.getInstance(new Key<Map<String, String>>() {}), injector.getInstance(Router.class),
                new SimpleProxyFactory.All(),
                new RegexMatcherFactory() {
                    @Override
                    public RegexMatcher createRegexMatcher(String pattern) {
                        return new ServerEnvironment.RM(pattern);
                    }
                });
    }*/
}
