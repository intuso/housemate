package com.intuso.housemate.proxy;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.type.option.Option;
import com.intuso.housemate.core.object.type.option.OptionListener;
import com.intuso.housemate.core.object.type.option.OptionWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyOption<
            R extends ProxyResources<NoChildrenProxyObjectFactory>,
            O extends ProxyOption<R, O>>
        extends ProxyObject<R, ProxyResources<NoChildrenProxyObjectFactory>, OptionWrappable, NoChildrenWrappable, NoChildrenProxyObject, O, OptionListener>
        implements Option {
    public ProxyOption(R resources, OptionWrappable wrappable) {
        super(resources, null, wrappable);
    }
}
