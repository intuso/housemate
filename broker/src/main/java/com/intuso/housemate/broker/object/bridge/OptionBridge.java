package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
public class OptionBridge
        extends BridgeObject<OptionWrappable,
            ListWrappable<SubTypeWrappable>,
            ListBridge<SubTypeWrappable, SubType<?>, SubTypeBridge>,
            OptionBridge,
            OptionListener>
        implements Option<ListBridge<SubTypeWrappable, SubType<?>, SubTypeBridge>> {

    private ListBridge<SubTypeWrappable, SubType<?>, SubTypeBridge> types;

    public OptionBridge(BrokerBridgeResources resources, Option<?> option) {
        super(resources, new OptionWrappable(option.getId(), option.getName(), option.getDescription()));
        if(option.getSubTypes() != null) {
            types = new ListBridge<SubTypeWrappable, SubType<?>, SubTypeBridge>(resources, option.getSubTypes(), new SubTypeBridge.Converter(resources));
            addWrapper(types);
        }
    }

    @Override
    public ListBridge<SubTypeWrappable, SubType<?>, SubTypeBridge> getSubTypes() {
        return types;
    }
}
