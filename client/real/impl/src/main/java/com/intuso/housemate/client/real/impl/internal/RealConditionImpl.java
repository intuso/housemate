package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.UsesDriver;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.real.api.internal.RealCondition;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.impl.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.impl.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Map;

/**
 * Base class for all condition
 */
public final class RealConditionImpl<DRIVER extends ConditionDriver>
        extends RealObject<Condition.Data, Condition.Listener<? super RealConditionImpl<DRIVER>>>
        implements RealCondition<DRIVER, RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
                RealPropertyImpl<PluginResource<ConditionDriver.Factory<DRIVER>>>, RealListImpl<RealPropertyImpl<?>>,
                RealConditionImpl<?>, RealListImpl<RealConditionImpl<?>>, RealConditionImpl<DRIVER>>, AddConditionCommand.Callback {

    private final static String PROPERTIES_DESCRIPTION = "The condition's properties";

    private final AnnotationProcessor annotationProcessor;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<ConditionDriver.Factory<DRIVER>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListImpl<RealPropertyImpl<?>> properties;
    private final RealValueImpl<Boolean> satisfiedValue;
    private final RealListImpl<RealConditionImpl<?>> childConditions;
    private final RealCommandImpl addConditionCommand;

    private final Map<String, Boolean> childSatisfied = Maps.newHashMap();
    private final Map<String, ListenerRegistration> childListenerRegistrations = Maps.newHashMap();

    private final RemoveCallback<RealConditionImpl<DRIVER>> removeCallback;

    private DRIVER driver;

    /**
     * @param logger {@inheritDoc}
     * @param data the condition's data
     * @param listenersFactory
     */
    @Inject
    public RealConditionImpl(@Assisted final Logger logger,
                             @Assisted Condition.Data data,
                             ListenersFactory listenersFactory,
                             @Assisted RemoveCallback<RealConditionImpl<DRIVER>> removeCallback,
                             AnnotationProcessor annotationProcessor,
                             ConditionFactoryType driverFactoryType,
                             AddConditionCommand.Factory addConditionCommandFactory) {
        super(logger, data, listenersFactory);
        this.annotationProcessor = annotationProcessor;
        this.removeCallback = removeCallback;
        this.renameCommand = new RealCommandImpl(ChildUtil.logger(logger, Renameable.RENAME_ID),
                new Command.Data(Renameable.RENAME_ID, Renameable.RENAME_ID, "Rename the condition"),
                listenersFactory,
                StringType.createParameter(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        new Parameter.Data(Renameable.NAME_ID, Renameable.NAME_ID, "The new name"),
                        listenersFactory)) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                    String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                    if (newName != null && !RealConditionImpl.this.getName().equals(newName))
                        setName(newName);
                }
            }
        };
        this.removeCommand = new RealCommandImpl(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                new Command.Data(Removeable.REMOVE_ID, Removeable.REMOVE_ID, "Remove the condition"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                remove();
            }
        };
        this.errorValue = StringType.createValue(ChildUtil.logger(logger, Failable.ERROR_ID),
                new Value.Data(Failable.ERROR_ID, Failable.ERROR_ID, "Current error for the condition"),
                listenersFactory,
                null);
        this.driverProperty = (RealPropertyImpl) new RealPropertyImpl(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                new Property.Data(UsesDriver.DRIVER_ID, UsesDriver.DRIVER_ID, "The condition's driver"),
                listenersFactory,
                driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                new Value.Data(UsesDriver.DRIVER_LOADED_ID, UsesDriver.DRIVER_LOADED_ID, "Whether the condition's driver is loaded or not"),
                listenersFactory,
                false);
        this.properties = new RealListImpl<>(ChildUtil.logger(logger, Condition.PROPERTIES_ID), new List.Data(Condition.PROPERTIES_ID, Condition.PROPERTIES_ID, PROPERTIES_DESCRIPTION), listenersFactory);
        this.satisfiedValue = BooleanType.createValue(ChildUtil.logger(logger, Condition.SATISFIED_ID),
                new Value.Data(Condition.SATISFIED_ID, Condition.SATISFIED_ID, "Whether the condition is satisfied or not"),
                listenersFactory,
                false);
        this.childConditions = new RealListImpl<>(ChildUtil.logger(logger, Condition.CONDITIONS_ID), new List.Data(Condition.CONDITIONS_ID, Condition.CONDITIONS_ID, "Child conditions"), listenersFactory);
        this.addConditionCommand = addConditionCommandFactory.create(ChildUtil.logger(logger, Condition.ADD_CONDITION_ID),
                new Command.Data(Condition.ADD_CONDITION_ID, Condition.ADD_CONDITION_ID, "Add condition"),
                this,
                new RemoveCallback() {
                    @Override
                    public void removeCondition(RealCondition condition) {
                        childConditions.remove(condition.getId());
                    }
                });
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginResource<ConditionDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginResource<ConditionDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginResource<ConditionDriver.Factory<DRIVER>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), session);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), session);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), session);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), session);
        properties.init(ChildUtil.name(name, Condition.PROPERTIES_ID), session);
        satisfiedValue.init(ChildUtil.name(name, Condition.SATISFIED_ID), session);
        childConditions.init(ChildUtil.name(name, Condition.PROPERTIES_ID), session);
        addConditionCommand.init(ChildUtil.name(name, Condition.PROPERTIES_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        properties.uninit();
        satisfiedValue.uninit();
        childConditions.uninit();
        addConditionCommand.uninit();
    }

    private void setName(String newName) {
        RealConditionImpl.this.getData().setName(newName);
        for(Condition.Listener<? super RealConditionImpl<DRIVER>> listener : listeners)
            listener.renamed(RealConditionImpl.this, RealConditionImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<ConditionDriver.Factory<DRIVER>> driverFactory = driverProperty.getValue();
            if(driverFactory != null) {
                driver = driverFactory.getResource().create(logger, this);
                for(RealPropertyImpl<?> property : annotationProcessor.findProperties(logger, driver))
                    properties.add(property);
                errorValue.setValue((String) null);
                driverLoadedValue.setValue(true);
                _start();
            }
        }
    }

    private void uninitDriver() {
        if(driver != null) {
            _stop();
            driverLoadedValue.setValue(false);
            errorValue.setValue("Driver not loaded");
            driver = null;
            for (RealPropertyImpl<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    @Override
    public DRIVER getDriver() {
        return driver;
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommandImpl getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValueImpl<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealPropertyImpl<PluginResource<ConditionDriver.Factory<DRIVER>>> getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValueImpl<Boolean> getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null ? driverLoadedValue.getValue() : false;
    }

    @Override
    public final RealListImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    @Override
    public RealValueImpl<Boolean> getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public boolean isSatisfied() {
        return satisfiedValue.getValue() != null ? satisfiedValue.getValue() : false;
    }

    @Override
    public RealListImpl<RealConditionImpl<?>> getConditions() {
        return childConditions;
    }

    @Override
    public RealCommandImpl getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public final void addCondition(RealConditionImpl<?> condition) {
        childConditions.add(condition);
    }

    protected final void remove() {
        removeCallback.removeCondition(this);
    }

    protected final void _start() {
        try {
            if(isDriverLoaded())
                driver.start();
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start condition: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(isDriverLoaded())
            driver.stop();
    }

    @Override
    public void setError(String error) {
        errorValue.setValue(error);
    }

    /**
     * Updates the satisfied value of the condition. If different, it will propagate to the parent. If It affects the
     * parent's satisfied value then it will propagate again until either it does not affect a parent condition or it
     * gets to the automation, in which case the tasks for the new value will be executed
     * @param satisfied
     */
    @Override
    public void conditionSatisfied(boolean satisfied) {
        if(satisfied != isSatisfied()) {
            getSatisfiedValue().setValue(satisfied);
            for(Condition.Listener<? super RealConditionImpl<DRIVER>> listener : listeners)
                listener.conditionSatisfied(this, satisfied);
        }
    }

    @Override
    public final void start() {
        for(RealConditionImpl childCondition : childConditions) {
            childCondition.start();
            childListenerRegistrations.put(childCondition.getId(), childCondition.addObjectListener(this));
            childSatisfied.put(childCondition.getId(), childCondition.isSatisfied());
        }
        if(isDriverLoaded())
            driver.start();
    }

    @Override
    public final void stop() {
        for(String id : childSatisfied.keySet())
            childListenerRegistrations.get(id).removeListener();
        for(RealCondition childCondition : childConditions)
            childCondition.stop();
        if(isDriverLoaded())
            driver.stop();
    }

    @Override
    public void conditionSatisfied(RealConditionImpl condition, boolean satisfied) {
        // todo call driver
    }

    @Override
    public void renamed(RealConditionImpl<?> renameable, String oldName, String newName) {
        // nothing to do, don't care if child is renamed
    }

    @Override
    public void driverLoaded(RealConditionImpl usesDriver, boolean loaded) {
        // todo figure out what to do here
    }

    @Override
    public void error(RealConditionImpl failable, String error) {
        // todo ignore this because it's from a child? or put this condition in error too
    }

    @Override
    public Map<String, Boolean> getChildSatisfied() {
        return childSatisfied;
    }

    public interface Factory {
        RealConditionImpl<?> create(Logger logger, Condition.Data data, RemoveCallback<RealConditionImpl<?>> removeCallback);
    }
}
