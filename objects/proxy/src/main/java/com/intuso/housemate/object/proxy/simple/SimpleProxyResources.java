package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.utilities.log.Log;

import java.util.Map;

public class SimpleProxyResources<
            OBJECT_FACTORY extends HousemateObjectFactory<? extends ProxyResources<?, ?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
        extends ProxyResources<OBJECT_FACTORY, SimpleProxyFeatureFactory> {

    /**
     * @param log                 the log instance to use
     * @param properties          the properties
     * @param router              the router to connect through
     * @param objectFactory       the object factory to use to create child objects
     * @param regexMatcherFactory the regex matcher factory to use to create regex matchers
     */
    public SimpleProxyResources(Log log, Map<String, String> properties, Router router, OBJECT_FACTORY objectFactory,
                                RegexMatcherFactory regexMatcherFactory) {
        this(log, properties, router, objectFactory, new SimpleProxyFeatureFactory(), regexMatcherFactory);
    }

    /**
     * @param log                 the log instance to use
     * @param properties          the properties
     * @param router              the router to connect through
     * @param objectFactory       the object factory to use to create child objects
     * @param featureFactory      the feature factory to create device features from
     * @param regexMatcherFactory the regex matcher factory to use to create regex matchers
     */
    public SimpleProxyResources(Log log, Map<String, String> properties, Router router, OBJECT_FACTORY objectFactory, SimpleProxyFeatureFactory featureFactory, RegexMatcherFactory regexMatcherFactory) {
        super(log, properties, router, objectFactory, featureFactory, regexMatcherFactory);
    }
}
