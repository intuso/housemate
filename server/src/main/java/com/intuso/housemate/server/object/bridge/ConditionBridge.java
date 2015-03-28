package com.intuso.housemate.server.object.bridge;

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
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public class ConditionBridge
        extends BridgeObject<ConditionData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, ConditionBridge, ConditionListener<? super ConditionBridge>>
        implements Condition<CommandBridge, ValueBridge, ValueBridge,
                    ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>, CommandBridge, ConditionBridge,
                    ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge>> {

    private CommandBridge removeCommand;
    private ValueBridge satisfiedValue;
    private ValueBridge errorValue;
    private SingleListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;
    private SingleListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private CommandBridge addConditionCommand;

    public ConditionBridge(Log log, ListenersFactory listenersFactory,
                           Condition<?, ?, ?, ?, ?, ?, ?> condition) {
        super(log, listenersFactory,
                new ConditionData(condition.getId(), condition.getName(), condition.getDescription()));
        removeCommand = new CommandBridge(log, listenersFactory, condition.getRemoveCommand());
        satisfiedValue = new ValueBridge(log, listenersFactory, condition.getSatisfiedValue());
        errorValue = new ValueBridge(log, listenersFactory, condition.getErrorValue());
        propertyList = new SingleListBridge<>(log, listenersFactory, condition.getProperties(), new PropertyBridge.Converter(log, listenersFactory));
        conditionList = new SingleListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge>(log, listenersFactory, condition.getConditions(), new Converter(log, listenersFactory));
        addConditionCommand = condition.getAddConditionCommand() == null ? null : new CommandBridge(log, listenersFactory, condition.getAddConditionCommand());
        addChild(removeCommand);
        addChild(satisfiedValue);
        addChild(errorValue);
        addChild(propertyList);
        addChild(conditionList);
        addChild(addConditionCommand);
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    @Override
    public ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
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

    public static class Converter implements Function<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public ConditionBridge apply(Condition<?, ?, ?, ?, ?, ?, ?> condition) {
            return new ConditionBridge(log, listenersFactory, condition);
        }
    }
}
