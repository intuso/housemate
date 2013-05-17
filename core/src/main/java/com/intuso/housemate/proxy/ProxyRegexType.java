package com.intuso.housemate.proxy;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.type.RegexTypeWrappable;
import com.intuso.housemate.core.resources.RegexMatcher;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
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
