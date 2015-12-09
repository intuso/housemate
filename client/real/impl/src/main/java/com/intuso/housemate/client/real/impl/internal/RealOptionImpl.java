package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealOption;
import com.intuso.housemate.client.real.api.internal.RealSubType;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.comms.api.internal.payload.OptionData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.Option;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public class RealOptionImpl
        extends RealObject<OptionData, ListData<SubTypeData>,
        RealListImpl<SubTypeData, RealSubTypeImpl<?>>, Option.Listener<? super RealOption>>
        implements RealOption {

    private final RealList<RealSubType<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the option's id
     * @param name the option's name
     * @param description the option's description
     * @param subTypes the option's sub types
     */
    public RealOptionImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, RealSubTypeImpl<?>... subTypes) {
        this(logger, listenersFactory, id, name, description, Arrays.asList(subTypes));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the option's id
     * @param name the option's name
     * @param description the option's description
     * @param subTypes the option's sub types
     */
    public RealOptionImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, List<RealSubTypeImpl<?>> subTypes) {
        super(logger, listenersFactory, new OptionData(id, name,  description));
        this.subTypes = (RealList)new RealListImpl<>(logger, listenersFactory, OptionData.SUB_TYPES_ID,
                "Sub Types", "The sub types of this option", subTypes);
        addChild((RealListImpl)this.subTypes);
    }

    @Override
    public final RealList<RealSubType<?>> getSubTypes() {
        return subTypes;
    }
}
