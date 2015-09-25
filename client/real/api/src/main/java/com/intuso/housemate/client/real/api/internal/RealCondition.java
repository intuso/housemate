package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.real.api.internal.factory.condition.RealConditionOwner;
import com.intuso.housemate.client.real.api.internal.impl.type.BooleanType;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.ChildOverview;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Condition;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

public abstract class RealCondition
        extends RealObject<ConditionData, HousemateData<?>, RealObject<?, ?, ?, ?>,
                    Condition.Listener<? super RealCondition>>
        implements Condition<RealCommand, RealValue<String>, RealValue<Boolean>,
            RealList<PropertyData, RealProperty<?>>, RealCommand, RealCondition,
            RealList<ConditionData, RealCondition>>,
        RealConditionOwner {

    private final String type;

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
    public RealCondition(Log log, ListenersFactory listenersFactory, String type, ConditionData data, RealConditionOwner owner,
                         RealProperty<?>... properties) {
        this(log, listenersFactory, type, data, owner, Arrays.asList(properties));
    }

    /**
     * @param log {@inheritDoc}
     * @param data the condition's data
     * @param properties the condition's properties
     */
    public RealCondition(final Log log, ListenersFactory listenersFactory, String type, ConditionData data,
                         final RealConditionOwner owner, List<RealProperty<?>> properties) {
        super(log, listenersFactory, data);
        this.type = type;
        removeCommand = new RealCommand(log, listenersFactory, ConditionData.REMOVE_ID, ConditionData.REMOVE_ID, "Remove the condition", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                owner.removeCondition(RealCondition.this);
            }
        };
        errorValue = new RealValue<>(log, listenersFactory, ConditionData.ERROR_ID, ConditionData.ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        satisfiedValue = new RealValue<>(log, listenersFactory, ConditionData.SATISFIED_ID, ConditionData.SATISFIED_ID, "Whether the condition is satisfied", new BooleanType(log, listenersFactory), false);
        propertyList = new RealList<>(log, listenersFactory, ConditionData.PROPERTIES_ID, ConditionData.PROPERTIES_ID, "The condition's properties", properties);
        conditions = new RealList<>(log, listenersFactory, ConditionData.CONDITIONS_ID, ConditionData.CONDITIONS_ID, "The condition's sub-conditions");
        // add a command to add automations to the automation list
        addChild(removeCommand);
        addChild(errorValue);
        addChild(satisfiedValue);
        addChild(propertyList);
        addChild(conditions);
    }

    public String getType() {
        return type;
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
    public RealValue<Boolean> getSatisfiedValue() {
        return satisfiedValue;
    }

    public boolean isSatisfied() {
        return satisfiedValue.getTypedValue() != null ? satisfiedValue.getTypedValue() : false;
    }

    @Override
    public ChildOverview getAddConditionCommandDetails() {
        return new ChildOverview(ConditionData.ADD_CONDITION_ID, ConditionData.ADD_CONDITION_ID, "Add condition");
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
            for(Condition.Listener<? super RealCondition> listener : getObjectListeners())
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
