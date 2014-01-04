package com.intuso.housemate.server.object;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.server.proxy.ServerProxyObject;
import com.intuso.housemate.object.server.proxy.ServerProxyResources;
import com.intuso.housemate.object.server.proxy.ServerProxyRootObject;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.server.object.general.ServerResourcesImpl;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

public class ServerProxyResourcesImpl<F extends HousemateObjectFactory<?, ?, ?>>
        extends ServerResourcesImpl<ServerProxyRootObject> implements ServerProxyResources<F> {

    private final ServerRealResources serverRealResources;
    private final RealResources realResources;
    private final F factory;

    @Inject
    public ServerProxyResourcesImpl(Log log, PropertyContainer properties, Injector injector,
                                    ServerRealResources serverRealResources, RealResources realResources, F factory) {
        super(log, properties, injector);
        this.serverRealResources = serverRealResources;
        this.realResources = realResources;
        this.factory = factory;
    }

    @Override
    public ServerRealResources getServerRealResources() {
        return serverRealResources;
    }

    @Override
    public RealResources getRealResources() {
        return realResources;
    }

    @Override
    public F getFactory() {
        return factory;
    }

    @Override
    public <NF extends HousemateObjectFactory<? extends ServerProxyResources<?>, ?, ? extends ServerProxyObject<?, ?, ?, ?, ?>>> ServerProxyResources<NF> cloneForNewFactory(NF newFactory) {
        return new Clone<NF>(this, newFactory);
    }

    private class Clone<F extends HousemateObjectFactory<?, ?, ?>> implements ServerProxyResources<F> {

        private final ServerProxyResources<?> original;
        private final F factory;

        private Clone(ServerProxyResources<?> original, F factory) {
            this.original = original;
            this.factory = factory;
        }

        @Override
        public ServerRealResources getServerRealResources() {
            return original.getServerRealResources();
        }

        @Override
        public RealResources getRealResources() {
            return original.getRealResources();
        }

        @Override
        public F getFactory() {
            return factory;
        }

        @Override
        public <NF extends HousemateObjectFactory<? extends ServerProxyResources<?>, ?, ? extends ServerProxyObject<?, ?, ?, ?, ?>>> ServerProxyResources<NF> cloneForNewFactory(NF newFactory) {
            return new Clone<NF>(this, newFactory);
        }

        @Override
        public ServerProxyRootObject getRoot() {
            return original.getRoot();
        }

        @Override
        public Log getLog() {
            return original.getLog();
        }

        @Override
        public PropertyContainer getProperties() {
            return original.getProperties();
        }

        @Override
        public Injector getInjector() {
            return original.getInjector();
        }
    }
}
