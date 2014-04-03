package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <SUB_TYPE> the type of the sub type
 * @param <SUB_TYPES> the type of the sub types
 * @param <OPTION> the type of the option
 */
public abstract class ProxyOption<
            SUB_TYPE extends ProxySubType<?, ?>,
            SUB_TYPES extends ProxyList<SubTypeData, SUB_TYPE, SUB_TYPES>,
            OPTION extends ProxyOption<SUB_TYPE, SUB_TYPES, OPTION>>
        extends ProxyObject<OptionData, ListData<SubTypeData>, SUB_TYPES, OPTION, OptionListener>
        implements Option<SUB_TYPES> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyOption(Log log, ListenersFactory listenersFactory, OptionData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public SUB_TYPES getSubTypes() {
        return getChild(SUB_TYPES_ID);
    }
}
