package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.option.Option;
import com.intuso.housemate.api.object.type.option.OptionListener;
import com.intuso.housemate.api.object.type.option.OptionWrappable;

import java.util.List;

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

    @Override
    public List<String> getSubTypes() {
        return getWrappable().getSubTypes();
    }
}
