package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubTypeData;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <SUB_TYPE> the type of the sub type
 * @param <SUB_TYPES> the type of the sub types
 * @param <OPTION> the type of the option
 */
public abstract class ProxyOption<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, ListData<SubTypeData>, SUB_TYPES>>,
            CHILD_RESOURCES extends ProxyResources<? extends HousemateObjectFactory<? extends ProxyResources<?>, SubTypeData, SUB_TYPE>>,
            SUB_TYPE extends ProxySubType<?, ?, ?>,
            SUB_TYPES extends ProxyList<CHILD_RESOURCES, ?, SubTypeData, SUB_TYPE, SUB_TYPES>,
            OPTION extends ProxyOption<RESOURCES, CHILD_RESOURCES, SUB_TYPE, SUB_TYPES, OPTION>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, OptionData, ListData<SubTypeData>, SUB_TYPES, OPTION, OptionListener>
        implements Option<SUB_TYPES> {

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyOption(RESOURCES resources, CHILD_RESOURCES childResources, OptionData data) {
        super(resources, childResources, data);
    }

    @Override
    public SUB_TYPES getSubTypes() {
        return getChild(SUB_TYPES_ID);
    }
}
