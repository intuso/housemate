package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;

/**
 */
public class OptionBridge
        extends BridgeObject<OptionData,
        ListData<SubTypeData>,
            ListBridge<SubTypeData, SubType<?>, SubTypeBridge>,
            OptionBridge,
            OptionListener>
        implements Option<ListBridge<SubTypeData, SubType<?>, SubTypeBridge>> {

    private ListBridge<SubTypeData, SubType<?>, SubTypeBridge> types;

    public OptionBridge(BrokerBridgeResources resources, Option<?> option) {
        super(resources, new OptionData(option.getId(), option.getName(), option.getDescription()));
        if(option.getSubTypes() != null) {
            types = new ListBridge<SubTypeData, SubType<?>, SubTypeBridge>(resources, option.getSubTypes(), new SubTypeBridge.Converter(resources));
            addChild(types);
        }
    }

    @Override
    public ListBridge<SubTypeData, SubType<?>, SubTypeBridge> getSubTypes() {
        return types;
    }
}
