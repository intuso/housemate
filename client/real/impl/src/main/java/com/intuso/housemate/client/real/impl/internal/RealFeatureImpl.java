package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealFeature;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.comms.api.internal.payload.FeatureData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.Feature;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

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
     * @param logger {@inheritDoc}
     * @param listenersFactory
     */
    @Inject
    public RealFeatureImpl(ListenersFactory listenersFactory,
                           @Assisted final Logger logger,
                           @Assisted FeatureData data) {
        super(listenersFactory, logger, new FeatureData(data.getId(), data.getName(), data.getDescription()));
        this.commands = (RealList)new RealListImpl<>(logger, listenersFactory, FeatureData.COMMANDS_ID, FeatureData.COMMANDS_ID, COMMANDS_DESCRIPTION);
        this.values = (RealList)new RealListImpl<>(logger, listenersFactory, FeatureData.VALUES_ID, FeatureData.VALUES_ID, VALUES_DESCRIPTION);
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
