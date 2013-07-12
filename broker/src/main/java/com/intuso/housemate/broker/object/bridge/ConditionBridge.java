package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import javax.annotation.Nullable;
import java.util.List;

/**
 */
public class ConditionBridge
        extends BridgeObject<ConditionData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, ConditionBridge, ConditionListener<? super ConditionBridge>>
        implements Condition<ValueBridge, ValueBridge,
                    ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>, CommandBridge, ConditionBridge,
                    ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge>> {

    private ValueBridge satisfiedValue;
    private ValueBridge errorValue;
    private ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;
    private ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private CommandBridge addConditionCommand;

    public ConditionBridge(BrokerBridgeResources resources, Condition<?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?>, ?> condition) {
        super(resources,new ConditionData(condition.getId(), condition.getName(), condition.getDescription()));
        satisfiedValue = new ValueBridge(resources, condition.getSatisfiedValue());
        errorValue = new ValueBridge(resources, condition.getErrorValue());
        propertyList = new ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>(resources, condition.getProperties(), new PropertyBridge.Converter(resources));
        conditionList = new ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge>(resources, condition.getConditions(), new Converter(resources));
        addConditionCommand = new CommandBridge(resources, condition.getAddConditionCommand()) {};
        addChild(satisfiedValue);
        addChild(errorValue);
        addChild(propertyList);
        addChild(conditionList);
        addChild(addConditionCommand);
    }

    @Override
    public ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    @Override
    public ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
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
        List<String> errors = RealType.deserialiseAll(StringType.SERIALISER, errorValue.getTypeInstances());
        return errors != null && errors.size() > 0 ? errors.get(0) : null;
    }

    @Override
    public ValueBridge getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public boolean isSatisfied() {
        List<Boolean> satisfieds = RealType.deserialiseAll(BooleanType.SERIALISER, satisfiedValue.getTypeInstances());
        return satisfieds != null && satisfieds.size() > 0 && satisfieds.get(0) != null ? satisfieds.get(0) : false;
    }

    public static class Converter implements Function<Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> {

        private final BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ConditionBridge apply(@Nullable Condition<?, ?, ?, ?, ?, ?> condition) {
            return new ConditionBridge(resources, condition);
        }
    }
}
