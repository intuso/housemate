package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
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
public class ConditionBridge
        extends BridgeObject<ConditionWrappable, HousemateObjectWrappable<?>, BridgeObject<?, ?, ?, ?, ?>, ConditionBridge, ConditionListener<? super ConditionBridge>>
        implements Condition<PropertyBridge, ValueBridge, ValueBridge,
                    ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge>, CommandBridge, ConditionBridge,
                    ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge>> {

    private ValueBridge satisfiedValue;
    private ValueBridge errorValue;
    private ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge> propertyList;
    private ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private CommandBridge addConditionCommand;

    public ConditionBridge(BrokerBridgeResources resources, Condition<?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?, ?>, ?> condition) {
        super(resources,new ConditionWrappable(condition.getId(), condition.getName(), condition.getDescription()));
        satisfiedValue = new ValueBridge(resources, condition.getSatisfiedValue());
        errorValue = new ValueBridge(resources, condition.getErrorValue());
        propertyList = new ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge>(resources, condition.getProperties(), new PropertyBridge.Converter(resources));
        conditionList = new ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge>(resources, condition.getConditions(), new Converter(resources));
        addConditionCommand = new CommandBridge(resources, condition.getAddConditionCommand()) {};
        addWrapper(satisfiedValue);
        addWrapper(errorValue);
        addWrapper(propertyList);
        addWrapper(conditionList);
        addWrapper(addConditionCommand);
    }

    @Override
    public ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    @Override
    public ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
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
        return errorValue != null && errorValue.getTypeInstance() != null ? errorValue.getTypeInstance().getValue() : null;
    }

    @Override
    public ValueBridge getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public boolean isSatisfied() {
        Boolean value = BooleanType.SERIALISER.deserialise(satisfiedValue.getTypeInstance());
        return value != null ? value : false;
    }

    public static class Converter implements Function<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> {

        private final BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ConditionBridge apply(@Nullable Condition<?, ?, ?, ?, ?, ?, ?> condition) {
            return new ConditionBridge(resources, condition);
        }
    }
}
