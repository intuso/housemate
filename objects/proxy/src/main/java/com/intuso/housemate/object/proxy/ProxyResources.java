package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.api.resources.ResourcesImpl;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Resources class for proxy objects
 *
 * @param <OBJECT_FACTORY> the type of the factory
 */
public class ProxyResources<
            OBJECT_FACTORY extends HousemateObjectFactory<? extends ProxyResources<?, ?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>,
            FEATURE_FACTORY extends ProxyFeatureFactory<?, ?>>
        extends ResourcesImpl implements ClientResources{

    private final Router router;
    private final OBJECT_FACTORY objectFactory;
    private final FEATURE_FACTORY featureFactory;
    private final RegexMatcherFactory regexMatcherFactory;

    /**
     * @param log the log instance to use
     * @param properties the properties
     * @param router the router to connect through
     * @param objectFactory the object factory to use to create child objects
     * @param regexMatcherFactory the regex matcher factory to use to create regex matchers
     */
    public ProxyResources(Log log, PropertyContainer properties, Router router, OBJECT_FACTORY objectFactory,
                          FEATURE_FACTORY featureFactory, RegexMatcherFactory regexMatcherFactory) {
        super(log, properties);
        this.router = router;
        this.objectFactory = objectFactory;
        this.featureFactory = featureFactory;
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
    public OBJECT_FACTORY getObjectFactory() {
        return objectFactory;
    }

    public FEATURE_FACTORY getFeatureFactory() {
        return featureFactory;
    }

    /**
     * Gets the regex matcher factory
     * @return the regex matcher factory
     */
    public RegexMatcherFactory getRegexMatcherFactory() {
        return regexMatcherFactory;
    }
}
