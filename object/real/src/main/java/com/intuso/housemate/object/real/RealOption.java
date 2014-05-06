package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

public class RealOption
        extends RealObject<OptionData, ListData<SubTypeData>,
            RealList<SubTypeData, RealSubType<?>>, OptionListener>
        implements Option<RealList<SubTypeData, RealSubType<?>>> {

    private final RealList<SubTypeData, RealSubType<?>> subTypes;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the option's id
     * @param name the option's name
     * @param description the option's description
     * @param subTypes the option's sub types
     */
    public RealOption(Log log, ListenersFactory listenersFactory, String id, String name, String description, RealSubType<?>... subTypes) {
        this(log, listenersFactory, id, name, description, Arrays.asList(subTypes));
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the option's id
     * @param name the option's name
     * @param description the option's description
     * @param subTypes the option's sub types
     */
    public RealOption(Log log, ListenersFactory listenersFactory, String id, String name, String description, List<RealSubType<?>> subTypes) {
        super(log, listenersFactory, new OptionData(id, name,  description));
        this.subTypes = new RealList<SubTypeData, RealSubType<?>>(log, listenersFactory, SUB_TYPES_ID,
                "Sub Types", "The sub types of this option", subTypes);
        addChild(this.subTypes);
    }

    @Override
    public final RealList<SubTypeData, RealSubType<?>> getSubTypes() {
        return subTypes;
    }
}
