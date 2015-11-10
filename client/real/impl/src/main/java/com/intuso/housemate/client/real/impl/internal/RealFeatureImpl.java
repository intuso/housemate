package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealFeature;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.comms.api.internal.payload.FeatureData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.Feature;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Base class for all devices
 */
public final class RealFeatureImpl
        extends RealObject<
        FeatureData,
        HousemateData<?>,
        RealObject<?, ?, ?, ?>,
        Feature.Listener<? super RealFeature>>
        implements RealFeature {

    private final static String COMMANDS_DESCRIPTION = "The feature's commands";
    private final static String VALUES_DESCRIPTION = "The feature's values";

    private final RealList<RealCommand> commands;
    private final RealList<RealValue<?>> values;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     */
    public RealFeatureImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description) {
        super(log, listenersFactory, new FeatureData(id, name, description));
        this.commands = (RealList)new RealListImpl<>(log, listenersFactory, FeatureData.COMMANDS_ID, FeatureData.COMMANDS_ID, COMMANDS_DESCRIPTION);
        this.values = (RealList)new RealListImpl<>(log, listenersFactory, FeatureData.VALUES_ID, FeatureData.VALUES_ID, VALUES_DESCRIPTION);
        addChild((RealListImpl)commands);
        addChild((RealListImpl)values);
    }

    @Override
    public final RealList<RealCommand> getCommands() {
        return commands;
    }

    @Override
    public final RealList<RealValue<?>> getValues() {
        return values;
    }
}
