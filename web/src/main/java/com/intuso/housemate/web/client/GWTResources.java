package com.intuso.housemate.web.client;

import com.intuso.housemate.core.comms.Router;
import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.resources.RegexMatcherFactory;
import com.intuso.housemate.proxy.ProxyObject;
import com.intuso.housemate.proxy.ProxyResources;
import com.intuso.housemate.web.client.object.GWTProxyRootObject;
import com.intuso.utils.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 25/02/13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class GWTResources<F extends HousemateObjectFactory<? extends ProxyResources<?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>> extends ProxyResources<F> {

    private GWTProxyRootObject root;
    private final LoginManager loginManager = new LoginManager();

    public GWTResources(Log log, Map<String, String> properties, Router router, F objectFactory, RegexMatcherFactory regexMatcherFactory) {
        super(log, properties, router, objectFactory, regexMatcherFactory);
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
