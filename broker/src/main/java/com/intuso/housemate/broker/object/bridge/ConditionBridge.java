package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.property.Property;
import com.intuso.housemate.core.object.property.PropertyWrappable;
import com.intuso.housemate.core.object.condition.Condition;
import com.intuso.housemate.core.object.condition.ConditionListener;
import com.intuso.housemate.core.object.condition.ConditionWrappable;
import com.intuso.housemate.real.impl.type.BooleanType;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/08/12
 * Time: 00:13
 * To change this template use File | Settings | File Templates.
 */
public class ConditionBridge
        extends BridgeObject<ConditionWrappable, HousemateObjectWrappable<?>, BridgeObject<?, ?, ?, ?, ?>, ConditionBridge, ConditionListener<? super ConditionBridge>>
        implements Condition<PropertyBridge, ValueBridge, ValueBridge,
            ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge>, CommandBridge, ConditionBridge,
            ListBridge<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionWrappable, ConditionBridge>> {

    private ValueBridge satisfiedValue;
    private ValueBridge errorValue;
    private ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge> propertyList;
    private ListBridge<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionWrappable, ConditionBridge> conditionList;
    private CommandBridge addConditionCommand;

    public ConditionBridge(BrokerBridgeResources resources, Condition<?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?, ?>, ?> condition) {
        super(resources,new ConditionWrappable(condition.getId(), condition.getName(), condition.getDescription()));
        satisfiedValue = new ValueBridge(resources, condition.getSatisfiedValue());
        errorValue = new ValueBridge(resources, condition.getErrorValue());
        propertyList = new ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge>(resources, condition.getProperties(), new PropertyConverter(resources));
        conditionList = new ListBridge<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionWrappable, ConditionBridge>(resources, condition.getConditions(), new ConditionConverter(resources));
        addConditionCommand = new CommandBridge(resources, condition.getAddConditionCommand()) {};
        addWrapper(satisfiedValue);
        addWrapper(errorValue);
        addWrapper(propertyList);
        addWrapper(conditionList);
        addWrapper(addConditionCommand);
    }

    @Override
    public ListBridge<Property<?, ?, ?>, PropertyWrappable, PropertyBridge> getProperties() {
        return propertyList;
    }

    @Override
    public ListBridge<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionWrappable, ConditionBridge> getConditions() {
        return conditionList;
    }

    @Override
    public CommandBridge getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue.getValue();
    }

    @Override
    public ValueBridge getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public boolean isSatisfied() {
        return BooleanType.SERIALISER.deserialise(satisfiedValue.getValue());
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

    private class ConditionConverter implements Function<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> {

        private final BrokerBridgeResources resources;

        private ConditionConverter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ConditionBridge apply(@Nullable Condition<?, ?, ?, ?, ?, ?, ?> condition) {
            return new ConditionBridge(resources, condition);
        }
    }
}
