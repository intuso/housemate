package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.impl.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.impl.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.Condition;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;
import java.util.Map;

public final class RealConditionImpl<DRIVER extends ConditionDriver>
        extends RealObject<ConditionData, HousemateData<?>, RealObject<?, ?, ?, ?>,
        Condition.Listener<? super RealCondition<DRIVER>>>
        implements RealCondition<DRIVER>,
        AddConditionCommand.Callback {

    private final AnnotationProcessor annotationProcessor;

    private RealCommandImpl removeCommand;
    private RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<ConditionDriver.Factory<DRIVER>>> driverProperty;
    private RealValueImpl<Boolean> driverLoadedValue;
    private RealValueImpl<Boolean> satisfiedValue;
    private RealList<RealProperty<?>> properties;
    private final RealList<RealCondition<?>> children;
    private final RealCommandImpl addConditionCommand;

    private final Map<String, Boolean> childSatisfied = Maps.newHashMap();
    private final Map<String, ListenerRegistration> childListenerRegistrations = Maps.newHashMap();

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param data the condition's data
     */
    @Inject
    public RealConditionImpl(final Log log,
                             ListenersFactory listenersFactory,
                             AnnotationProcessor annotationProcessor,
                             ConditionFactoryType driverFactoryType,
                             AddConditionCommand.Factory addConditionCommandFactory,
                             @Assisted ConditionData data,
                             @Assisted final RemoveCallback removeCallback) {
        super(log, listenersFactory, data);
        this.annotationProcessor = annotationProcessor;
        removeCommand = new RealCommandImpl(log, listenersFactory, ConditionData.REMOVE_ID, ConditionData.REMOVE_ID, "Remove the condition", Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                removeCallback.removeCondition(RealConditionImpl.this);
            }
        };
        errorValue = new RealValueImpl<>(log, listenersFactory, ConditionData.ERROR_ID, ConditionData.ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        driverProperty = (RealPropertyImpl<PluginResource<ConditionDriver.Factory<DRIVER>>>) new RealPropertyImpl(log, listenersFactory, "driver", "Driver", "The condition's driver", driverFactoryType);
        driverLoadedValue = BooleanType.createValue(log, listenersFactory, ConditionData.DRIVER_LOADED_ID, ConditionData.DRIVER_LOADED_ID, "Whether the task's driver is loaded or not", false);
        satisfiedValue = new RealValueImpl<>(log, listenersFactory, ConditionData.SATISFIED_ID, ConditionData.SATISFIED_ID, "Whether the condition is satisfied", new BooleanType(log, listenersFactory), false);
        properties = (RealList)new RealListImpl<>(log, listenersFactory, ConditionData.PROPERTIES_ID, ConditionData.PROPERTIES_ID, "The condition's properties");
        children = (RealList)new RealListImpl<>(log, listenersFactory, ConditionData.CONDITIONS_ID, "Conditions", "Child conditions");
        addConditionCommand = addConditionCommandFactory.create(ConditionData.ADD_CONDITION_ID, ConditionData.ADD_CONDITION_ID, "Add condition", this, new RemoveCallback() {
            @Override
            public void removeCondition(RealCondition condition) {
                children.remove(condition.getId());
            }
        });
        // add a command to add automations to the automation list
        addChild(removeCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(satisfiedValue);
        addChild((RealListImpl)properties);
        addChild((RealListImpl)children);
        addChild(addConditionCommand);
        driverProperty.addObjectListener(new Property.Listener<RealProperty<PluginResource<ConditionDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealProperty<PluginResource<ConditionDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealProperty<PluginResource<ConditionDriver.Factory<DRIVER>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<ConditionDriver.Factory<DRIVER>> driverFactoryEntry = driverProperty.getTypedValue();
            if(driverFactoryEntry != null) {
                driver = driverFactoryEntry.getResource().create(this);
                annotationProcessor.process(this, driver);
                errorValue.setTypedValues((String) null);
                driverLoadedValue.setTypedValues(false);
            }
        }
    }

    private void uninitDriver() {
        if(driver != null) {
            driverLoadedValue.setTypedValues(false);
            errorValue.setTypedValues("Driver not loaded");
            driver = null;
            for (RealProperty<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    public DRIVER getDriver() {
        return driver;
    }

    @Override
    public RealList<RealCondition<?>> getConditions() {
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
    public RealList<RealProperty<?>> getProperties() {
        return properties;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealProperty<PluginResource<ConditionDriver.Factory<DRIVER>>> getDriverProperty() {
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

    public boolean isSatisfied() {
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
    public void conditionSatisfied(boolean satisfied) {
        if(satisfied != isSatisfied()) {
            getSatisfiedValue().setTypedValues(satisfied);
            for(Listener<? super RealConditionImpl<DRIVER>> listener : getObjectListeners())
                listener.conditionSatisfied(this, satisfied);
        }
    }

    public final void start() {
        for(RealCondition child : children) {
            child.start();
            childListenerRegistrations.put(child.getId(), child.addObjectListener(this));
            childSatisfied.put(child.getId(), child.isSatisfied());
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
        children.add((RealConditionImpl<?>) condition);
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

    @Override
    public Map<String, Boolean> getChildSatisfied() {
        return childSatisfied;
    }
}
