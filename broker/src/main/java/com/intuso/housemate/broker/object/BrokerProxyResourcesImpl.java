package com.intuso.housemate.broker.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.general.BrokerResourcesImpl;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.ServerComms;
import com.intuso.housemate.object.broker.proxy.BrokerProxyObject;
import com.intuso.housemate.object.broker.proxy.BrokerProxyResources;
import com.intuso.housemate.object.broker.proxy.BrokerProxyRootObject;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 22/05/13
 * Time: 07:17
 * To change this template use File | Settings | File Templates.
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
        public ServerComms getComms() {
            return original.getComms();
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
