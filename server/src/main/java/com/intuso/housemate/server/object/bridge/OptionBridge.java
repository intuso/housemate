package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.comms.api.internal.payload.OptionData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.Option;
import com.intuso.housemate.object.api.internal.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class OptionBridge
        extends BridgeObject<OptionData,
        ListData<SubTypeData>,
        ConvertingListBridge<SubTypeData, SubType<?>, SubTypeBridge>,
            OptionBridge,
            Option.Listener<? super OptionBridge>>
        implements Option<ConvertingListBridge<SubTypeData, SubType<?>, SubTypeBridge>, OptionBridge> {

    private ConvertingListBridge<SubTypeData, SubType<?>, SubTypeBridge> subTypes;

    public OptionBridge(Logger logger, ListenersFactory listenersFactory, Option<?, ?> option) {
        super(logger, listenersFactory, new OptionData(option.getId(), option.getName(), option.getDescription()));
        if(option.getSubTypes() != null) {
            subTypes = new ConvertingListBridge<>(logger, listenersFactory, option.getSubTypes(),
                    new SubTypeBridge.Converter(logger, listenersFactory));
            addChild(subTypes);
        }
    }

    @Override
    public ConvertingListBridge<SubTypeData, SubType<?>, SubTypeBridge> getSubTypes() {
        return subTypes;
    }

    public final static class Converter implements Function<Option<?, ?>, OptionBridge> {

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public OptionBridge apply(Option<?, ?> option) {
            return new OptionBridge(logger, listenersFactory, option);
        }
    }
}
