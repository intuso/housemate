package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.type.option.Option;
import com.intuso.housemate.api.object.type.option.OptionWrappable;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
public class TypeBridge
        extends BridgeObject<TypeWrappable<HousemateObjectWrappable<?>>, HousemateObjectWrappable<?>,
            BridgeObject<?, ?, ?, ?, ?>, TypeBridge, TypeListener>
        implements Type {

    private final static String OPTIONS = "options";

    public TypeBridge(final BrokerBridgeResources resources, TypeWrappable<HousemateObjectWrappable<?>> wrappable, Type type) {
        super(resources, (TypeWrappable<HousemateObjectWrappable<?>>)wrappable.clone());
        if(type instanceof HousemateObject && ((HousemateObject)type).getWrapper(OPTIONS) != null) {
            addWrapper(new ListBridge<Option, OptionWrappable, OptionBridge>(resources, (List)((HousemateObject)(type)).getWrapper(OPTIONS),
                    new Function<Option, OptionBridge>() {
                        @Override
                        public OptionBridge apply(@Nullable Option option) {
                            return new OptionBridge(resources, option.getId(), option.getName(), option.getDescription());
                        }
                    }));
        }
    }
}
