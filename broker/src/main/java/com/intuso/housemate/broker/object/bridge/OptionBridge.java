package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.broker.proxy.BrokerProxyType;

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

    public OptionBridge(BrokerBridgeResources resources, Option<?> option,
                        ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types) {
        super(resources, new OptionData(option.getId(), option.getName(), option.getDescription()));
        if(option.getSubTypes() != null) {
            subTypes = new ListBridge<SubTypeData, SubType<?>, SubTypeBridge>(resources, option.getSubTypes(),
                    new SubTypeBridge.Converter(resources, types));
            addChild(subTypes);
        }
    }

    @Override
    public ListBridge<SubTypeData, SubType<?>, SubTypeBridge> getSubTypes() {
        return subTypes;
    }

    public final static class Converter implements Function<Option<?>, OptionBridge> {

        private final BrokerBridgeResources resources;
        private final ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types;

        public Converter(BrokerBridgeResources resources, ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types) {
            this.resources = resources;
            this.types = types;
        }

        @Override
        public OptionBridge apply(Option<?> option) {
            return new OptionBridge(resources, option, types);
        }
    }
}
