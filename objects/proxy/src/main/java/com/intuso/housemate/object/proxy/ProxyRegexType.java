package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.RegexTypeData;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxyRegexType<
            TYPE extends ProxyRegexType<TYPE>>
        extends ProxyType<RegexTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    private RegexMatcher regexMatcher;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyRegexType(Log log, Injector injector, RegexTypeData data) {
        super(log, injector, data);
        regexMatcher = injector.getInstance(RegexMatcherFactory.class).createRegexMatcher(getData().getRegexPattern());
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
