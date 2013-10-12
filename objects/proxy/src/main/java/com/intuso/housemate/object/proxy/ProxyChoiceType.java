package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.type.ChoiceTypeData;
import com.intuso.housemate.api.object.option.HasOptions;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child's resources
 * @param <OPTION> the type of the options
 * @param <OPTIONS> the type of the options list
 * @param <TYPE> the type of the type
 */
public abstract class ProxyChoiceType<
            RESOURCES extends ProxyResources<
                    ? extends HousemateObjectFactory<CHILD_RESOURCES, ListData<OptionData>, OPTIONS>,
                    ? extends ProxyFeatureFactory<?, ?>>,
            CHILD_RESOURCES extends ProxyResources<? extends HousemateObjectFactory<? extends ProxyResources<?, ?>, OptionData, ? extends OPTION>, ?>,
            OPTION extends ProxyOption<?, ?, ?, ?, OPTION>,
            OPTIONS extends ProxyList<?, ?, OptionData, OPTION, OPTIONS>,
            TYPE extends ProxyChoiceType<RESOURCES, CHILD_RESOURCES, OPTION, OPTIONS, TYPE>>
        extends ProxyType<RESOURCES, CHILD_RESOURCES, ChoiceTypeData, ListData<OptionData>, OPTIONS, TYPE>
        implements HasOptions {

    private static final String OPTIONS_ID = "options";

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyChoiceType(RESOURCES resources, CHILD_RESOURCES childResources, ChoiceTypeData data) {
        super(resources, childResources, data);
    }

    @Override
    public OPTIONS getOptions() {
        return getChild(OPTIONS_ID);
    }
}
