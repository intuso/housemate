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
                    ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge>,
                    ConsequenceBridge> {

    private ValueBridge executingValue;
    private ValueBridge errorValue;
    private ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge> propertyList;

    public ConsequenceBridge(BrokerBridgeResources resources, Consequence<?, ?, ?, ?, ?> consequence) {
        super(resources, new ConsequenceWrappable(consequence.getId(), consequence.getName(), consequence.getDescription()));
        executingValue = new ValueBridge(resources,consequence.getExecutingValue());
        errorValue = new ValueBridge(resources,consequence.getErrorValue());
        propertyList = new ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge>(resources, consequence.getProperties(), new PropertyBridge.Converter(resources));
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
        return errorValue != null && errorValue.getTypeInstance() != null ? errorValue.getTypeInstance().getValue() : null;
    }

    @Override
    public ValueBridge getExecutingValue() {
        return executingValue;
    }

    @Override
    public boolean isExecuting() {
        Boolean value = BooleanType.SERIALISER.deserialise(executingValue.getTypeInstance());
        return value != null ? value : false;
    }

    @Override
    public ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    public static class Converter implements Function<Consequence<?, ?, ?, ?, ?>, ConsequenceBridge> {

        private final BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ConsequenceBridge apply(@Nullable Consequence<?, ?, ?, ?, ?> command) {
            return new ConsequenceBridge(resources, command);
        }
    }
}
