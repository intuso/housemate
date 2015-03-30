package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class OptionBridge
        extends BridgeObject<OptionData,
        ListData<SubTypeData>,
        ConvertingListBridge<SubTypeData, SubType<?>, SubTypeBridge>,
            OptionBridge,
            OptionListener>
        implements Option<ConvertingListBridge<SubTypeData, SubType<?>, SubTypeBridge>> {

    private ConvertingListBridge<SubTypeData, SubType<?>, SubTypeBridge> subTypes;

    public OptionBridge(Log log, ListenersFactory listenersFactory, Option<?> option) {
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

    public final static class Converter implements Function<Option<?>, OptionBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public OptionBridge apply(Option<?> option) {
            return new OptionBridge(log, listenersFactory, option);
        }
    }
}
