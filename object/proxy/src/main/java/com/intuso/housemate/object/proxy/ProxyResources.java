package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.utils.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 26/03/13
 * Time: 09:02
 * To change this template use File | Settings | File Templates.
 */
public class ProxyResources<
            F extends HousemateObjectFactory<? extends ProxyResources<?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
        implements ClientResources {

    private final Log log;
    private final Map<String, String> properties;
    private final Router router;
    private final F objectFactory;
    private final RegexMatcherFactory regexMatcherFactory;

    public ProxyResources(Log log, Map<String, String> properties, Router router, F objectFactory, RegexMatcherFactory regexMatcherFactory) {
        this.log = log;
        this.properties = properties;
        this.router = router;
        this.objectFactory = objectFactory;
        this.regexMatcherFactory = regexMatcherFactory;
    }

    public Log getLog() {
        return log;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Router getRouter() {
        return router;
    }

    public F getObjectFactory() {
        return objectFactory;
    }

    public RegexMatcherFactory getRegexMatcherFactory() {
        return regexMatcherFactory;
    }
}
