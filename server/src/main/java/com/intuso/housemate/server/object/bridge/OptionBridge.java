package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.comms.api.internal.payload.OptionData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.Option;
import com.intuso.housemate.object.api.internal.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

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

    public OptionBridge(Log log, ListenersFactory listenersFactory, Option<?, ?> option) {
        super(log, listenersFactory, new OptionData(option.getId(), option.getName(), option.getDescription()));
        if(option.getSubTypes() != null) {
            subTypes = new ConvertingListBridge<>(log, listenersFactory, option.getSubTypes(),
                    new SubTypeBridge.Converter(log, listenersFactory));
            addChild(subTypes);
        }
    }

    @Override
    public ConvertingListBridge<SubTypeData, SubType<?>, SubTypeBridge> getSubTypes() {
        return subTypes;
    }

    public final static class Converter implements Function<Option<?, ?>, OptionBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public OptionBridge apply(Option<?, ?> option) {
            return new OptionBridge(log, listenersFactory, option);
        }
    }
}
