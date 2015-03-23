package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.ServerProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class OptionBridge
        extends BridgeObject<OptionData,
        ListData<SubTypeData>,
            ListBridge<SubTypeData, SubType<?>, SubTypeBridge>,
            OptionBridge,
            OptionListener>
        implements Option<ListBridge<SubTypeData, SubType<?>, SubTypeBridge>> {

    private ListBridge<SubTypeData, SubType<?>, SubTypeBridge> subTypes;

    public OptionBridge(Log log, ListenersFactory listenersFactory, Option<?> option,
                        ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory, new OptionData(option.getId(), option.getName(), option.getDescription()));
        if(option.getSubTypes() != null) {
            subTypes = new SingleListBridge<SubTypeData, SubType<?>, SubTypeBridge>(log, listenersFactory, option.getSubTypes(),
                    new SubTypeBridge.Converter(log, listenersFactory, types));
            addChild(subTypes);
        }
    }

    @Override
    public ListBridge<SubTypeData, SubType<?>, SubTypeBridge> getSubTypes() {
        return subTypes;
    }

    public final static class Converter implements Function<Option<?>, OptionBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListenersFactory listenersFactory, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
            this.types = types;
        }

        @Override
        public OptionBridge apply(Option<?> option) {
            return new OptionBridge(log, listenersFactory, option, types);
        }
    }
}
