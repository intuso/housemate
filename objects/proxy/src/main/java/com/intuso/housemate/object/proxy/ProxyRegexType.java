package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.RegexTypeWrappable;
import com.intuso.housemate.api.resources.RegexMatcher;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 */
public abstract class ProxyRegexType<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory>,
            TYPE extends ProxyRegexType<RESOURCES, TYPE>>
        extends ProxyType<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory>, RegexTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, TYPE> {

    private RegexMatcher regexMatcher;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyRegexType(RESOURCES resources, RegexTypeWrappable data) {
        super(resources, null, data);
        regexMatcher = resources.getRegexMatcherFactory().createRegexMatcher(getData().getRegexPattern());
    }

    /**
     * Checks that the given value matches the regex for this type
     * @param value the value to check
     * @return true if the value matches the regex
     */
    public final boolean isCorrectFormat(String value) {
        return regexMatcher.matches(value);
    }
}
