package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <SUB_TYPE> the type of the sub type
 * @param <SUB_TYPES> the type of the sub types
 * @param <OPTION> the type of the option
 */
public abstract class ProxyOption<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, ListWrappable<SubTypeWrappable>, SUB_TYPES>>,
            CHILD_RESOURCES extends ProxyResources<? extends HousemateObjectFactory<? extends ProxyResources<?>, SubTypeWrappable, SUB_TYPE>>,
            SUB_TYPE extends ProxySubType<?, ?, ?>,
            SUB_TYPES extends ProxyList<CHILD_RESOURCES, ?, SubTypeWrappable, SUB_TYPE, SUB_TYPES>,
            OPTION extends ProxyOption<RESOURCES, CHILD_RESOURCES, SUB_TYPE, SUB_TYPES, OPTION>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, OptionWrappable, ListWrappable<SubTypeWrappable>, SUB_TYPES, OPTION, OptionListener>
        implements Option<SUB_TYPES> {

    private SUB_TYPES subTypes;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyOption(RESOURCES resources, CHILD_RESOURCES childResources, OptionWrappable data) {
        super(resources, childResources, data);
    }

    @Override
    protected void getChildObjects() {
        subTypes = getWrapper(SUB_TYPES_ID);
    }

    @Override
    public SUB_TYPES getSubTypes() {
        return subTypes;
    }
}
