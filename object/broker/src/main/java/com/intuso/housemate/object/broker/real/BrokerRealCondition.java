package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.util.ArrayList;

/**
 */
public abstract class BrokerRealCondition
        extends BrokerRealObject<ConditionWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>,
            ConditionListener<? super BrokerRealCondition>>
        implements Condition<BrokerRealValue<String>, BrokerRealValue<Boolean>,
            BrokerRealList<PropertyWrappable, BrokerRealProperty<?>>, BrokerRealCommand, BrokerRealCondition,
            BrokerRealList<ConditionWrappable, BrokerRealCondition>> {

    private BrokerRealValue<String> errorValue;
    private BrokerRealValue<Boolean> satisfiedValue;
    private BrokerRealList<PropertyWrappable, BrokerRealProperty<?>> propertyList;
    private BrokerRealCommand addConditionCommand;
    private BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions;

    public BrokerRealCondition(BrokerRealResources resources, String id, String name, String description) {
        this(resources, id, name, description, new ArrayList<BrokerRealProperty<?>>(0));
    }

    public BrokerRealCondition(final BrokerRealResources resources, String id, String name, String description, java.util.List<BrokerRealProperty<?>> properties) {
        super(resources, new ConditionWrappable(id, name, description));
        errorValue = new BrokerRealValue<String>(resources, ERROR_ID, ERROR_ID, "The current error", new StringType(resources.getRealResources()), null);
        satisfiedValue = new BrokerRealValue<Boolean>(resources, SATISFIED_ID, SATISFIED_ID, "Whether the condition is satisfied", new BooleanType(resources.getRealResources()), false);
        propertyList = new BrokerRealList<PropertyWrappable, BrokerRealProperty<?>>(resources, PROPERTIES_ID, PROPERTIES_ID, "The condition's properties", properties);
        conditions = new BrokerRealList<ConditionWrappable, BrokerRealCondition>(resources, CONDITIONS_ID, CONDITIONS_ID, "The condition's sub-conditions");
        // add a command to add automations to the automation list
        addConditionCommand = getResources().getLifecycleHandler().createAddConditionCommand(conditions);
        addWrapper(errorValue);
        addWrapper(satisfiedValue);
        addWrapper(propertyList);
        addWrapper(addConditionCommand);
        addWrapper(conditions);
    }

    @Override
    public BrokerRealList<PropertyWrappable, BrokerRealProperty<?>> getProperties() {
        return propertyList;
    }

    @Override
    public BrokerRealList<ConditionWrappable, BrokerRealCondition> getConditions() {
        return conditions;
    }

    @Override
    public BrokerRealCommand getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public BrokerRealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue.getTypedValue();
    }

    @Override
    public BrokerRealValue<Boolean> getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public boolean isSatisfied() {
        return satisfiedValue.getTypedValue() != null ? satisfiedValue.getTypedValue() : false;
    }

    public final void setError(String error) {
        getErrorValue().setTypedValue(error);
    }

    protected void conditionSatisfied(boolean satisfied) {
        if(satisfied != isSatisfied()) {
            getSatisfiedValue().setTypedValue(satisfied);
            for(ConditionListener<? super BrokerRealCondition> listener : getObjectListeners())
                listener.conditionSatisfied(this, satisfied);
        }
    }

    public abstract void start();
    public abstract void stop();
}
