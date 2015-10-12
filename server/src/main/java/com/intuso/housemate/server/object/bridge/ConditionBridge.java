package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Condition;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class ConditionBridge
        extends BridgeObject<ConditionData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, ConditionBridge, Condition.Listener<? super ConditionBridge>>
        implements Condition<CommandBridge,
        ValueBridge,
        PropertyBridge,
        ValueBridge,
        ValueBridge,
        ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
        CommandBridge,
        ConditionBridge,
        ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ConditionBridge>,
        ConditionBridge> {

    private CommandBridge removeCommand;
    private ValueBridge satisfiedValue;
    private ValueBridge errorValue;
    private PropertyBridge driverProperty;
    private ValueBridge driverLoadedValue;
    private ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;
    private ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private CommandBridge addConditionCommand;

    public ConditionBridge(Log log, ListenersFactory listenersFactory,
                           Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> condition) {
        super(log, listenersFactory,
                new ConditionData(condition.getId(), condition.getName(), condition.getDescription()));
        removeCommand = new CommandBridge(log, listenersFactory, condition.getRemoveCommand());
        satisfiedValue = new ValueBridge(log, listenersFactory, condition.getSatisfiedValue());
        errorValue = new ValueBridge(log, listenersFactory, condition.getErrorValue());
        driverProperty = new PropertyBridge(log, listenersFactory, condition.getDriverProperty());
        driverLoadedValue = new ValueBridge(log, listenersFactory, condition.getDriverLoadedValue());
        propertyList = new ConvertingListBridge<>(log, listenersFactory, condition.getProperties(), new PropertyBridge.Converter(log, listenersFactory));
        conditionList = new ConvertingListBridge<>(log, listenersFactory, (com.intuso.housemate.object.api.internal.List<? extends Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>) condition.getConditions(), new Converter(log, listenersFactory));
        addConditionCommand = condition.getAddConditionCommand() == null ? null : new CommandBridge(log, listenersFactory, condition.getAddConditionCommand());
        addChild(removeCommand);
        addChild(satisfiedValue);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(propertyList);
        addChild(conditionList);
        addChild(addConditionCommand);
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    @Override
    public ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
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
    public PropertyBridge getDriverProperty() {
        return driverProperty;
    }

    @Override
    public ValueBridge getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public ValueBridge getSatisfiedValue() {
        return satisfiedValue;
    }

    public static class Converter implements Function<Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ConditionBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public ConditionBridge apply(Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> condition) {
            return new ConditionBridge(log, listenersFactory, condition);
        }
    }
}
