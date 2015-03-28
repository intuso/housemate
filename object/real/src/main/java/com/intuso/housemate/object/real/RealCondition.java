package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.factory.condition.RealConditionOwner;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

public abstract class RealCondition
        extends RealObject<ConditionData, HousemateData<?>, RealObject<?, ?, ?, ?>,
                    ConditionListener<? super RealCondition>>
        implements Condition<RealCommand, RealValue<String>, RealValue<Boolean>,
            RealList<PropertyData, RealProperty<?>>, RealCommand, RealCondition,
            RealList<ConditionData, RealCondition>>,
        RealConditionOwner {

    private RealCommand removeCommand;
    private RealValue<String> errorValue;
    private RealValue<Boolean> satisfiedValue;
    private RealList<PropertyData, RealProperty<?>> propertyList;
    private RealList<ConditionData, RealCondition> conditions;

    /**
     * @param log {@inheritDoc}
     * @param data the condition's data
     * @param properties the condition's properties
     */
    public RealCondition(Log log, ListenersFactory listenersFactory, ConditionData data, RealConditionOwner owner,
                         RealProperty<?>... properties) {
        this(log, listenersFactory, data, owner, Arrays.asList(properties));
    }

    /**
     * @param log {@inheritDoc}
     * @param data the condition's data
     * @param properties the condition's properties
     */
    public RealCondition(final Log log, ListenersFactory listenersFactory, ConditionData data,
                         final RealConditionOwner owner, java.util.List<RealProperty<?>> properties) {
        super(log, listenersFactory, data);
        removeCommand = new RealCommand(log, listenersFactory, REMOVE_ID, REMOVE_ID, "Remove the condition", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                owner.removeCondition(RealCondition.this);
            }
        };
        errorValue = new RealValue<>(log, listenersFactory, ERROR_ID, ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        satisfiedValue = new RealValue<>(log, listenersFactory, SATISFIED_ID, SATISFIED_ID, "Whether the condition is satisfied", new BooleanType(log, listenersFactory), false);
        propertyList = new RealList<>(log, listenersFactory, PROPERTIES_ID, PROPERTIES_ID, "The condition's properties", properties);
        conditions = new RealList<>(log, listenersFactory, CONDITIONS_ID, CONDITIONS_ID, "The condition's sub-conditions");
        // add a command to add automations to the automation list
        addChild(removeCommand);
        addChild(errorValue);
        addChild(satisfiedValue);
        addChild(propertyList);
        addChild(conditions);
    }

    @Override
    public RealCommand getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealList<PropertyData, RealProperty<?>> getProperties() {
        return propertyList;
    }

    @Override
    public RealList<ConditionData, RealCondition> getConditions() {
        return conditions;
    }

    @Override
    public RealCommand getAddConditionCommand() {
        return null;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue.getTypedValue();
    }

    @Override
    public RealValue<Boolean> getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public boolean isSatisfied() {
        return satisfiedValue.getTypedValue() != null ? satisfiedValue.getTypedValue() : false;
    }

    @Override
    public ChildOverview getAddConditionCommandDetails() {
        return new ChildOverview(ADD_CONDITION_ID, ADD_CONDITION_ID, "Add condition");
    }

    @Override
    public void addCondition(RealCondition condition) {
        conditions.add(condition);
    }

    @Override
    public void removeCondition(RealCondition condition) {
        conditions.remove(condition.getId());
    }

    /**
     * Sets the error message for the object
     * @param error
     */
    public final void setError(String error) {
        getErrorValue().setTypedValues(error);
    }

    /**
     * Updates the satisfied value of the condition. If different, it will propagate to the parent. If It affects the
     * parent's satisfied value then it will propagate again until either it does not affect a parent condition or it
     * gets to the automation, in which case the tasks for the new value will be executed
     * @param satisfied
     */
    protected void conditionSatisfied(boolean satisfied) {
        if(satisfied != isSatisfied()) {
            getSatisfiedValue().setTypedValues(satisfied);
            for(ConditionListener<? super RealCondition> listener : getObjectListeners())
                listener.conditionSatisfied(this, satisfied);
        }
    }

    /**
     * Starts the condition
     */
    public abstract void start();

    /**
     * Stops the condition
     */
    public abstract void stop();

}
