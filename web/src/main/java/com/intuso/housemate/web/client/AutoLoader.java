package com.intuso.housemate.web.client;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.ChildData;
import com.intuso.housemate.object.proxy.AvailableChildrenListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.object.*;
import com.intuso.utilities.object.Object;

import java.util.Map;

public class AutoLoader
        implements ObjectListener<ProxyObject<?, ?, ?, ?, ?, ?, ?>>,
        AvailableChildrenListener<ProxyObject<?, ?, ?, ?, ?, ?, ?>> {

    private Map<String, ListenerRegistration> listenerRegistrations = Maps.newHashMap();

    @Override
    public void childObjectAdded(String childId, ProxyObject<?, ?, ?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void childObjectRemoved(String childId, ProxyObject<?, ?, ?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, Object<?, ?, ?, ?> ancestor) {
        if(ancestor instanceof ProxyObject) {
            ProxyObject<?, ?, ?, ?, ?, ?, ?> proxyObject = (ProxyObject<?, ?, ?, ?, ?, ?, ?>) ancestor;
            listenerRegistrations.put(ancestorPath, proxyObject.addAvailableChildrenListener(this, true));
        }
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, com.intuso.utilities.object.Object<?, ?, ?, ?> ancestor) {
        if(listenerRegistrations.containsKey(ancestorPath));
            listenerRegistrations.remove(ancestorPath).removeListener();
    }

    @Override
    public void childAdded(ProxyObject<?, ?, ?, ?, ?, ?, ?> object, ChildData childData) {
        object.load(new SimpleLoadManager(childData.getId()));
    }

    @Override
    public void childRemoved(ProxyObject<?, ?, ?, ?, ?, ?, ?> object, ChildData childData) {
        // do nothing
    }

    private static class SimpleLoadManager extends LoadManager {

        private SimpleLoadManager(String... toLoad) {
            super(toLoad);
        }

        @Override
        protected void failed(String id) {
            // do nothing
        }

        @Override
        protected void allLoaded() {
            // do nothing
        }
    }
}
