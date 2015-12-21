package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.FeatureData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Feature;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class FeatureBridge
        extends BridgeObject<FeatureData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, FeatureBridge, Feature.Listener<? super FeatureBridge>>
        implements Feature<
        ConvertingListBridge<CommandData, Command<?, ?, ?, ?>, CommandBridge>,
        ConvertingListBridge<ValueData, Value<?, ?>, ValueBridge>,
        FeatureBridge> {

    private Feature<?, ?, ?> feature;
    private ConvertingListBridge<CommandData, Command<?, ?, ?, ?>, CommandBridge> commandList;
    private ConvertingListBridge<ValueData, Value<?, ?>, ValueBridge> valueList;

    public FeatureBridge(Logger logger, ListenersFactory listenersFactory,
                         Feature<?, ?, ?> feature) {
        super(listenersFactory, logger, new FeatureData(feature.getId(), feature.getName(), feature.getDescription()));
        this.feature = feature;
        commandList = new ConvertingListBridge<>(logger, listenersFactory, (com.intuso.housemate.object.api.internal.List<? extends Command<?, ?, ?, ?>>) feature.getCommands(), new CommandBridge.Converter(logger, listenersFactory));
        valueList = new ConvertingListBridge<>(logger, listenersFactory, (com.intuso.housemate.object.api.internal.List<? extends Value<?, ?>>) feature.getValues(), new ValueBridge.Converter(logger, listenersFactory));
        addChild(commandList);
        addChild(valueList);
    }

    @Override
    public ConvertingListBridge<CommandData, Command<?, ?, ?, ?>, CommandBridge> getCommands() {
        return commandList;
    }

    @Override
    public ConvertingListBridge<ValueData, Value<?, ?>, ValueBridge> getValues() {
        return valueList;
    }

    public final static class Converter implements Function<Feature<?, ?, ?>, FeatureBridge> {

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public FeatureBridge apply(Feature<?, ?, ?> feature) {
            return new FeatureBridge(logger, listenersFactory, feature);
        }
    }
}
