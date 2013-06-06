package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.consequence.Consequence;
import com.intuso.housemate.api.object.consequence.ConsequenceListener;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.object.real.impl.type.BooleanType;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/08/12
 * Time: 00:13
 * To change this template use File | Settings | File Templates.
 */
public class ConsequenceBridge
        extends BridgeObject<ConsequenceWrappable, HousemateObjectWrappable<?>, BridgeObject<?, ?, ?, ?, ?>, ConsequenceBridge,
        ConsequenceListener<? super ConsequenceBridge>>
        implements Consequence<PropertyBridge, ValueBridge, ValueBridge,
                    ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge>,
                    ConsequenceBridge> {

    private ValueBridge executingValue;
    private ValueBridge errorValue;
    private ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge> propertyList;

    public ConsequenceBridge(BrokerBridgeResources resources, Consequence<?, ?, ?, ?, ?> consequence) {
        super(resources, new ConsequenceWrappable(consequence.getId(), consequence.getName(), consequence.getDescription()));
        executingValue = new ValueBridge(resources,consequence.getExecutingValue());
        errorValue = new ValueBridge(resources,consequence.getErrorValue());
        propertyList = new ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge>(resources, consequence.getProperties(), new PropertyConverter(resources));
        addWrapper(executingValue);
        addWrapper(errorValue);
        addWrapper(propertyList);
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue != null && errorValue.getValue() != null ? errorValue.getValue().getValue() : null;
    }

    @Override
    public ValueBridge getExecutingValue() {
        return executingValue;
    }

    @Override
    public boolean isExecuting() {
        return BooleanType.SERIALISER.deserialise(executingValue.getValue());
    }

    @Override
    public ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge> getProperties() {
        return propertyList;
    }

    private class PropertyConverter implements Function<Property<?, ?, ?>, PropertyBridge> {

        private final BrokerBridgeResources resources;

        private PropertyConverter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public PropertyBridge apply(@Nullable Property<?, ?, ?> property) {
            return new PropertyBridge(resources, property);
        }
    }
}
