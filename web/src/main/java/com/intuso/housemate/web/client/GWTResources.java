package com.intuso.housemate.web.client;

import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.web.client.object.GWTProxyRootObject;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeatureFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
public class GWTResources<F extends HousemateObjectFactory<? extends ProxyResources<?, ?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
        extends ProxyResources<F, GWTProxyFeatureFactory> {

    private GWTProxyRootObject root;
    private final LoginManager loginManager = new LoginManager();

    public GWTResources(Log log, Map<String, String> properties, Router router, F objectFactory,
                        GWTProxyFeatureFactory featureFactory, RegexMatcherFactory regexMatcherFactory) {
        super(log, properties, router, objectFactory, featureFactory, regexMatcherFactory);
    }

    public GWTProxyRootObject getRoot() {
        return root;
    }

    public void setRoot(GWTProxyRootObject root) {
        this.root = root;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }
}
