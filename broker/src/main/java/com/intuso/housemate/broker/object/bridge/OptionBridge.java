package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.option.Option;
import com.intuso.housemate.api.object.type.option.OptionListener;
import com.intuso.housemate.api.object.type.option.OptionWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
public class OptionBridge
        extends BridgeObject<OptionWrappable, NoChildrenWrappable, NoChildrenBridgeObject, OptionBridge, OptionListener>
        implements Option {

    public OptionBridge(BrokerBridgeResources resources, String id, String name, String description) {
        super(resources, new OptionWrappable(id, name, description));
    }
}
