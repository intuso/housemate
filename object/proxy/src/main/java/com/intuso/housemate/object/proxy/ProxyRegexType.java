package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.RegexTypeData;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxyRegexType<
            TYPE extends ProxyRegexType<TYPE>>
        extends ProxyType<RegexTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    private final RegexMatcher regexMatcher;

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     * @param regexMatcher
     */
    public ProxyRegexType(Log log, ListenersFactory listenersFactory, RegexTypeData data, RegexMatcher regexMatcher) {
        super(log, listenersFactory, data);
        this.regexMatcher = regexMatcher;
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
