package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.comms.api.internal.payload.OptionData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.Option;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

public class RealOption
        extends RealObject<OptionData, ListData<SubTypeData>,
            RealList<SubTypeData, RealSubType<?>>, Option.Listener<? super RealOption>>
        implements Option<RealList<SubTypeData, RealSubType<?>>, RealOption> {

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
        this.subTypes = new RealList<>(log, listenersFactory, OptionData.SUB_TYPES_ID,
                "Sub Types", "The sub types of this option", subTypes);
        addChild(this.subTypes);
    }

    @Override
    public final RealList<SubTypeData, RealSubType<?>> getSubTypes() {
        return subTypes;
    }
}
