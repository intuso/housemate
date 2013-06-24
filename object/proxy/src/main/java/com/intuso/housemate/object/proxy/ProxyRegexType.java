package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.RegexTypeWrappable;
import com.intuso.housemate.api.resources.RegexMatcher;

/**
 */
public abstract class ProxyRegexType<
            R extends ProxyResources<NoChildrenProxyObjectFactory>,
            T extends ProxyRegexType<R, T>>
        extends ProxyType<R, ProxyResources<NoChildrenProxyObjectFactory>, RegexTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, T> {

    private RegexMatcher regexMatcher;

    public ProxyRegexType(R resources, RegexTypeWrappable wrappable) {
        super(resources, null, wrappable);
        regexMatcher = resources.getRegexMatcherFactory().createRegexMatcher(getWrappable().getRegexPattern());
    }

    public final boolean isCorrectFormat(String value) {
        return regexMatcher.matches(value);
    }

    public String getRegexPattern() {
        return getWrappable().getRegexPattern();
    }
}
