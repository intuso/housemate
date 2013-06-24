package com.intuso.housemate.broker.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.general.BrokerResourcesImpl;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.proxy.BrokerProxyObject;
import com.intuso.housemate.object.broker.proxy.BrokerProxyResources;
import com.intuso.housemate.object.broker.proxy.BrokerProxyRootObject;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
public class BrokerProxyResourcesImpl<F extends HousemateObjectFactory<?, ?, ?>>
        extends BrokerResourcesImpl<BrokerProxyRootObject> implements BrokerProxyResources<F> {
    
    private F factory;
    
    public BrokerProxyResourcesImpl(BrokerGeneralResources generalResources, F factory) {
        super(generalResources);
        this.factory = factory;
    }

    @Override
    public F getFactory() {
        return factory;
    }

    @Override
    public <NF extends HousemateObjectFactory<? extends BrokerProxyResources<?>, ?, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>> BrokerProxyResources<NF> cloneForNewFactory(NF newFactory) {
        return new Clone<NF>(this, newFactory);
    }

    private class Clone<F extends HousemateObjectFactory<?, ?, ?>> implements BrokerProxyResources<F> {

        private final BrokerProxyResources<?> original;
        private final F factory;

        private Clone(BrokerProxyResources<?> original, F factory) {
            this.original = original;
            this.factory = factory;
        }

        @Override
        public F getFactory() {
            return factory;
        }

        @Override
        public <NF extends HousemateObjectFactory<? extends BrokerProxyResources<?>, ?, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>> BrokerProxyResources<NF> cloneForNewFactory(NF newFactory) {
            return new Clone<NF>(this, newFactory);
        }

        @Override
        public BrokerRealResources getBrokerRealResources() {
            return original.getBrokerRealResources();
        }

        @Override
        public RealResources getRealResources() {
            return original.getRealResources();
        }

        @Override
        public LifecycleHandler getLifecycleHandler() {
            return original.getLifecycleHandler();
        }

        @Override
        public BrokerProxyRootObject getRoot() {
            return original.getRoot();
        }

        @Override
        public Log getLog() {
            return original.getLog();
        }

        @Override
        public Map<String, String> getProperties() {
            return original.getProperties();
        }
    }
}
