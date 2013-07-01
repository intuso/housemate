package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.MultiChoiceTypeWrappable;
import com.intuso.housemate.api.object.option.HasOptions;
import com.intuso.housemate.api.object.option.OptionWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child's resources
 * @param <OPTION> the type of the options
 * @param <OPTIONS> the type of the options list
 * @param <TYPE> the type of the type
 */
public abstract class ProxyMultiChoiceType<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, ListWrappable<OptionWrappable>, OPTIONS>>,
            CHILD_RESOURCES extends ProxyResources<? extends HousemateObjectFactory<? extends ProxyResources<?>, OptionWrappable, ? extends OPTION>>,
            OPTION extends ProxyOption<?, ?, ?, ?, OPTION>,
            OPTIONS extends ProxyList<?, ?, OptionWrappable, OPTION, OPTIONS>,
            TYPE extends ProxyMultiChoiceType<RESOURCES, CHILD_RESOURCES, OPTION, OPTIONS, TYPE>>
        extends ProxyType<RESOURCES, CHILD_RESOURCES, MultiChoiceTypeWrappable, ListWrappable<OptionWrappable>, OPTIONS, TYPE>
        implements HasOptions {

    private static final String OPTIONS_ID = "options";

    private OPTIONS options;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyMultiChoiceType(RESOURCES resources, CHILD_RESOURCES childResources, MultiChoiceTypeWrappable data) {
        super(resources, childResources, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        options = getWrapper(OPTIONS_ID);
    }

    @Override
    public OPTIONS getOptions() {
        return options;
    }
}
