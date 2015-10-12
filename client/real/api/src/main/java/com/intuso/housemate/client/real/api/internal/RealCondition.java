package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.api.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.api.internal.impl.type.BooleanType;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Condition;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;
import java.util.Map;

public final class RealCondition<DRIVER extends ConditionDriver>
        extends RealObject<ConditionData, HousemateData<?>, RealObject<?, ?, ?, ?>,
        Condition.Listener<? super RealCondition<DRIVER>>>
        implements Condition<RealCommand,
        RealValue<String>,
        RealProperty<ConditionDriver.Factory<DRIVER>>,
        RealValue<Boolean>,
        RealValue<Boolean>,
        RealList<PropertyData, RealProperty<?>>, RealCommand, RealCondition<?>,
        RealList<ConditionData, RealCondition<?>>,
        RealCondition<DRIVER>>,
        AddConditionCommand.Callback,
        Condition.Listener<RealCondition<?>> {

    private RealCommand removeCommand;
    private RealValue<String> errorValue;
    private final RealProperty<ConditionDriver.Factory<DRIVER>> driverProperty;
    private RealValue<Boolean> driverLoadedValue;
    private RealValue<Boolean> satisfiedValue;
    private RealList<PropertyData, RealProperty<?>> propertyList;
    private final RealList<ConditionData, RealCondition<?>> children;
    private final RealCommand addConditionCommand;

    private final Map<String, Boolean> childSatisfied = Maps.newHashMap();
    private final Map<String, ListenerRegistration> childListenerRegistrations = Maps.newHashMap();

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param data the condition's data
     */
    @Inject
    public RealCondition(final Log log,
                         ListenersFactory listenersFactory,
                         ConditionFactoryType driverFactoryType,
                         AddConditionCommand.Factory addConditionCommandFactory,
                         @Assisted ConditionData data,
                         @Assisted final RemovedListener removedListener) {
        super(log, listenersFactory, data);
        removeCommand = new RealCommand(log, listenersFactory, ConditionData.REMOVE_ID, ConditionData.REMOVE_ID, "Remove the condition", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                removedListener.conditionRemoved(RealCondition.this);
            }
        };
        errorValue = new RealValue<>(log, listenersFactory, ConditionData.ERROR_ID, ConditionData.ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        driverProperty = (RealProperty<ConditionDriver.Factory<DRIVER>>) new RealProperty(log, listenersFactory, "driver", "Driver", "The condition's driver", driverFactoryType);
        driverLoadedValue = BooleanType.createValue(log, listenersFactory, ConditionData.DRIVER_LOADED_ID, ConditionData.DRIVER_LOADED_ID, "Whether the task's driver is loaded or not", false);
        satisfiedValue = new RealValue<>(log, listenersFactory, ConditionData.SATISFIED_ID, ConditionData.SATISFIED_ID, "Whether the condition is satisfied", new BooleanType(log, listenersFactory), false);
        propertyList = new RealList<>(log, listenersFactory, ConditionData.PROPERTIES_ID, ConditionData.PROPERTIES_ID, "The condition's properties");
        children = new RealList<>(log, listenersFactory, ConditionData.CONDITIONS_ID, "Conditions", "Child conditions");
        addConditionCommand = addConditionCommandFactory.create(ConditionData.ADD_CONDITION_ID, ConditionData.ADD_CONDITION_ID, "Add condition", this, new RemovedListener() {
            @Override
            public void conditionRemoved(RealCondition condition) {
                children.remove(condition.getId());
            }
        });
        // add a command to add automations to the automation list
        addChild(removeCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(satisfiedValue);
        addChild(propertyList);
        addChild(children);
        addChild(addConditionCommand);
    }

    public DRIVER getDriver() {
        return driver;
    }

    @Override
    public RealList<ConditionData, RealCondition<?>> getConditions() {
        return children;
    }

    @Override
    public RealCommand getAddConditionCommand() {
        return addConditionCommand;
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
    public RealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealProperty<ConditionDriver.Factory<DRIVER>> getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValue<Boolean> getDriverLoadedValue() {
        return driverLoadedValue;
    }

    public boolean isDriverLoaded() {
        return driverLoadedValue.getTypedValue() != null ? driverLoadedValue.getTypedValue() : false;
    }

    @Override
    public RealValue<Boolean> getSatisfiedValue() {
        return satisfiedValue;
    }

    public boolean getChildSatisfied() {
        return satisfiedValue.getTypedValue() != null ? satisfiedValue.getTypedValue() : false;
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
        if(satisfied != getChildSatisfied()) {
            getSatisfiedValue().setTypedValues(satisfied);
            for(Condition.Listener<? super RealCondition<DRIVER>> listener : getObjectListeners())
                listener.conditionSatisfied(this, satisfied);
        }
    }

    public final void start() {
        for(RealCondition child : children) {
            child.start();
            childListenerRegistrations.put(child.getId(), child.addObjectListener(this));
            childSatisfied.put(child.getId(), child.getChildSatisfied());
        }
        // todo, call driver
    }


    public final void stop() {
        for(String id : childSatisfied.keySet())
            childListenerRegistrations.get(id).removeListener();
        for(RealCondition child : children)
            child.stop();
        // todo, call driver
    }

    @Override
    public void addCondition(RealCondition condition) {
        children.add(condition);
    }

    @Override
    public void conditionSatisfied(RealCondition condition, boolean satisfied) {
        // todo call driver
    }

    @Override
    public void driverLoaded(RealCondition usesDriver, boolean loaded) {
        // todo figure out what to do here
    }

    @Override
    public void error(RealCondition failable, String error) {
        // todo ignore this because it's from a child? or put this condition in error too
    }

    public interface RemovedListener {
        void conditionRemoved(RealCondition condition);
    }

    public interface Factory {
        RealCondition<?> create(ConditionData data, RemovedListener removedListener);
    }
}
