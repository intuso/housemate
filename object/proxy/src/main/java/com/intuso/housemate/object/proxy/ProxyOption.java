package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 */
public abstract class ProxyOption<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, ListWrappable<SubTypeWrappable>, STL>>,
            SR extends ProxyResources<? extends HousemateObjectFactory<? extends ProxyResources<?>, SubTypeWrappable, ST>>,
            ST extends ProxySubType<?, ?, ?>,
            STL extends ProxyList<SR, ?, SubTypeWrappable, ST, STL>,
            O extends ProxyOption<R, SR, ST, STL, O>>
        extends ProxyObject<R, SR, OptionWrappable, ListWrappable<SubTypeWrappable>, STL, O, OptionListener>
        implements Option<STL> {

    private STL subTypes;

    public ProxyOption(R resources, SR subResources, OptionWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        subTypes = getWrapper(SUB_TYPES_ID);
    }

    @Override
    public STL getSubTypes() {
        return subTypes;
    }
}
