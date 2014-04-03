package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.HasOptions;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.type.ChoiceTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <OPTION> the type of the options
 * @param <OPTIONS> the type of the options list
 * @param <TYPE> the type of the type
 */
public abstract class ProxyChoiceType<
            OPTION extends ProxyOption<?, ?, OPTION>,
            OPTIONS extends ProxyList<OptionData, OPTION, OPTIONS>,
            TYPE extends ProxyChoiceType<OPTION, OPTIONS, TYPE>>
        extends ProxyType<ChoiceTypeData, ListData<OptionData>, OPTIONS, TYPE>
        implements HasOptions {

    private static final String OPTIONS_ID = "options";

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyChoiceType(Log log, ListenersFactory listenersFactory, ChoiceTypeData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public OPTIONS getOptions() {
        return getChild(OPTIONS_ID);
    }
}
