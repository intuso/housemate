package com.intuso.housemate.object.server.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

public abstract class ServerRealCondition
        extends ServerRealObject<ConditionData, HousemateData<?>, ServerRealObject<?, ?, ?, ?>,
                    ConditionListener<? super ServerRealCondition>>
        implements Condition<ServerRealCommand, ServerRealValue<String>, ServerRealValue<Boolean>,
        ServerRealList<PropertyData, ServerRealProperty<?>>, ServerRealCommand, ServerRealCondition,
        ServerRealList<ConditionData, ServerRealCondition>>,
        ServerRealConditionOwner {

    private ServerRealCommand removeCommand;
    private ServerRealValue<String> errorValue;
    private ServerRealValue<Boolean> satisfiedValue;
    private ServerRealList<PropertyData, ServerRealProperty<?>> propertyList;
    private ServerRealCommand addConditionCommand;
    private ServerRealList<ConditionData, ServerRealCondition> conditions;

    /**
     * @param log {@inheritDoc}
     * @param data the condition's data
     * @param properties the condition's properties
     */
    public ServerRealCondition(Log log, ListenersFactory listenersFactory, ConditionData data, ServerRealConditionOwner owner,
                               LifecycleHandler lifecycleHandler,
                               ServerRealProperty<?>... properties) {
        this(log, listenersFactory, data, owner, lifecycleHandler, Arrays.asList(properties));
    }

    /**
     * @param log {@inheritDoc}
     * @param data the condition's data
     * @param properties the condition's properties
     */
    public ServerRealCondition(final Log log, ListenersFactory listenersFactory, ConditionData data,
                               final ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler,
                               java.util.List<ServerRealProperty<?>> properties) {
        super(log, listenersFactory, data);
        removeCommand = new ServerRealCommand(log, listenersFactory, REMOVE_ID, REMOVE_ID, "Remove the condition", Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                owner.remove(ServerRealCondition.this);
            }
        };
        errorValue = new ServerRealValue<String>(log, listenersFactory, ERROR_ID, ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        satisfiedValue = new ServerRealValue<Boolean>(log, listenersFactory, SATISFIED_ID, SATISFIED_ID, "Whether the condition is satisfied", new BooleanType(log, listenersFactory), false);
        propertyList = new ServerRealList<PropertyData, ServerRealProperty<?>>(log, listenersFactory, PROPERTIES_ID, PROPERTIES_ID, "The condition's properties", properties);
        conditions = new ServerRealList<ConditionData, ServerRealCondition>(log, listenersFactory, CONDITIONS_ID, CONDITIONS_ID, "The condition's sub-conditions");
        // add a command to add automations to the automation list
        addConditionCommand = lifecycleHandler.createAddConditionCommand(conditions, this);
        addChild(removeCommand);
        addChild(errorValue);
        addChild(satisfiedValue);
        addChild(propertyList);
        addChild(addConditionCommand);
        addChild(conditions);
    }

    @Override
    public ServerRealCommand getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ServerRealList<PropertyData, ServerRealProperty<?>> getProperties() {
        return propertyList;
    }

    @Override
    public ServerRealList<ConditionData, ServerRealCondition> getConditions() {
        return conditions;
    }

    @Override
    public ServerRealCommand getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public ServerRealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue.getTypedValue();
    }

    @Override
    public ServerRealValue<Boolean> getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public boolean isSatisfied() {
        return satisfiedValue.getTypedValue() != null ? satisfiedValue.getTypedValue() : false;
    }

    @Override
    public void remove(ServerRealCondition condition) {
        conditions.remove(condition.getId());
    }

    /**
     * Sets the error message for the object
     * @param error
     */
    public final void setError(String error) {
        getErrorValue().setTypedValue(error);
    }

    /**
     * Updates the satisfied value of the condition. If different, it will propagate to the parent. If It affects the
     * parent's satisfied value then it will propagate again until either it does not affect a parent condition or it
     * gets to the automation, in which case the tasks for the new value will be executed
     * @param satisfied
     */
    protected void conditionSatisfied(boolean satisfied) {
        if(satisfied != isSatisfied()) {
            getSatisfiedValue().setTypedValue(satisfied);
            for(ConditionListener<? super ServerRealCondition> listener : getObjectListeners())
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
