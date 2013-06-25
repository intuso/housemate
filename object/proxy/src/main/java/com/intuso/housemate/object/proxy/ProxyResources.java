package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.api.resources.SimpleResources;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Resources class for proxy objects
 *
 * @param <FACTORY> the type of the factory
 */
public class ProxyResources<
            FACTORY extends HousemateObjectFactory<? extends ProxyResources<?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
        extends SimpleResources implements ClientResources{

    private final Router router;
    private final FACTORY objectFactory;
    private final RegexMatcherFactory regexMatcherFactory;

    /**
     * @param log the log instance to use
     * @param properties the properties
     * @param router the router to connect through
     * @param objectFactory the object factory to use to create child objects
     * @param regexMatcherFactory the regex matcher factory to use to create regex matchers
     */
    public ProxyResources(Log log, Map<String, String> properties, Router router, FACTORY objectFactory, RegexMatcherFactory regexMatcherFactory) {
        super(log, properties);
        this.router = router;
        this.objectFactory = objectFactory;
        this.regexMatcherFactory = regexMatcherFactory;
    }

    @Override
    public Router getRouter() {
        return router;
    }

    /**
     * Gets the object factory
     * @return the object factory
     */
    public FACTORY getObjectFactory() {
        return objectFactory;
    }

    /**
     * Gets the regex matcher factory
     * @return the regex matcher factory
     */
    public RegexMatcherFactory getRegexMatcherFactory() {
        return regexMatcherFactory;
    }
}
