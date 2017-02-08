package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.bridge.v1_0.driver.ConditionDriverBridge;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.UsesDriver;
import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.object.Condition;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.api.internal.RealCondition;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.housemate.client.real.impl.internal.utils.AddConditionCommand;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.Map;

/**
 * Base class for all condition
 */
public final class RealConditionImpl
        extends RealObject<Condition.Data, Condition.Listener<? super RealConditionImpl>>
        implements RealCondition<RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
        RealPropertyImpl<PluginDependency<ConditionDriver.Factory<?>>>, RealListGeneratedImpl<RealPropertyImpl<?>>,
        RealConditionImpl, RealListPersistedImpl<Condition.Data, RealConditionImpl>, RealConditionImpl>, AddConditionCommand.Callback {

    private final static String PROPERTIES_DESCRIPTION = "The condition's properties";

    private final AnnotationParser annotationParser;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginDependency<ConditionDriver.Factory<?>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListGeneratedImpl<RealPropertyImpl<?>> properties;
    private final RealValueImpl<Boolean> satisfiedValue;
    private final RealListPersistedImpl<Condition.Data, RealConditionImpl> childConditions;
    private final RealCommandImpl addConditionCommand;

    private final Map<String, Boolean> childSatisfied = Maps.newHashMap();
    private final Map<String, ManagedCollection.Registration> childListenerRegistrations = Maps.newHashMap();

    private final RealListPersistedImpl.RemoveCallback<RealConditionImpl> removeCallback;

    private ManagedCollection.Registration driverAvailableListenerRegsitration;
    private ConditionDriver driver;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealConditionImpl(@Assisted final Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted final RealListPersistedImpl.RemoveCallback<RealConditionImpl> removeCallback,
                             ManagedCollectionFactory managedCollectionFactory,
                             AnnotationParser annotationParser,
                             RealCommandImpl.Factory commandFactory,
                             RealParameterImpl.Factory parameterFactory,
                             RealPropertyImpl.Factory propertyFactory,
                             RealValueImpl.Factory valueFactory,
                             RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory,
                             final RealListPersistedImpl.Factory<Condition.Data, RealConditionImpl> conditionsFactory,
                             AddConditionCommand.Factory addConditionCommandFactory,
                             TypeRepository typeRepository) {
        super(logger, new Condition.Data(id, name, description), managedCollectionFactory);
        this.annotationParser = annotationParser;
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the condition",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealConditionImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.newArrayList(parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
                        typeRepository.getType(new TypeSpec(String.class)),
                        1,
                        1)));
        this.removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                Removeable.REMOVE_ID,
                Removeable.REMOVE_ID,
                "Remove the condition",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        remove();
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.errorValue = (RealValueImpl<String>) valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID),
                Failable.ERROR_ID,
                Failable.ERROR_ID,
                "Current error for the condition",
                typeRepository.getType(new TypeSpec(String.class)),
                1,
                1,
                Lists.<String>newArrayList());
        this.driverProperty = (RealPropertyImpl<PluginDependency<ConditionDriver.Factory<?>>>) propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                UsesDriver.DRIVER_ID,
                UsesDriver.DRIVER_ID,
                "The condition's driver",
                typeRepository.getType(new TypeSpec(Types.newParameterizedType(PluginDependency.class, ConditionDriver.class))),
                1,
                1,
                Lists.<PluginDependency<ConditionDriver.Factory<?>>>newArrayList());
        this.driverLoadedValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                UsesDriver.DRIVER_LOADED_ID,
                UsesDriver.DRIVER_LOADED_ID,
                "Whether the condition's driver is loaded or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.properties = propertiesFactory.create(ChildUtil.logger(logger, Condition.PROPERTIES_ID),
                Condition.PROPERTIES_ID,
                Condition.PROPERTIES_ID,
                PROPERTIES_DESCRIPTION,
                Lists.<RealPropertyImpl<?>>newArrayList());
        this.satisfiedValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, Condition.SATISFIED_ID),
                Condition.SATISFIED_ID,
                Condition.SATISFIED_ID,
                "Whether the condition is satisfied or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        final RealListPersistedImpl.RemoveCallback<RealConditionImpl> childRemoveCallback = new RealListPersistedImpl.RemoveCallback<RealConditionImpl>() {
            @Override
            public void remove(RealConditionImpl condition) {
                childConditions.remove(condition.getId());
            }
        };
        this.childConditions = conditionsFactory.create(ChildUtil.logger(logger, Condition.CONDITIONS_ID),
                Condition.CONDITIONS_ID,
                Condition.CONDITIONS_ID,
                "Child conditions");
        this.addConditionCommand = addConditionCommandFactory.create(ChildUtil.logger(logger, Condition.CONDITIONS_ID),
                ChildUtil.logger(logger, Condition.ADD_CONDITION_ID),
                Condition.ADD_CONDITION_ID,
                Condition.ADD_CONDITION_ID,
                "Add condition",
                this,
                childRemoveCallback);
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginDependency<ConditionDriver.Factory<?>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginDependency<ConditionDriver.Factory<?>>> factoryRealProperty) {
                uninitDriver();
                uninitDriverListener();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginDependency<ConditionDriver.Factory<?>>> property) {
                if(property.getValue() != null) {
                    initDriverListener();
                    if (property.getValue().getDependency() != null)
                        initDriver(property.getValue().getDependency());
                }
            }
        });
    }

    private void initDriverListener() {
        PluginDependency<ConditionDriver.Factory<?>> driverFactory = driverProperty.getValue();
        driverAvailableListenerRegsitration = driverFactory.addListener(new PluginDependency.Listener<ConditionDriver.Factory<?>>() {
            @Override
            public void dependencyAvailable(ConditionDriver.Factory<?> dependency) {
                initDriver(dependency);
            }

            @Override
            public void dependencyUnavailable() {
                uninitDriver();
            }
        });
    }

    private void uninitDriverListener() {
        if(driverAvailableListenerRegsitration != null) {
            driverAvailableListenerRegsitration.remove();
            driverAvailableListenerRegsitration = null;
        }
    }

    private void initDriver(ConditionDriver.Factory<?> driverFactory) {
        if(driver != null)
            uninit();
        driver = driverFactory.create(logger, this);
        java.lang.Object annotatedObject;
        if(driver instanceof ConditionDriverBridge)
            annotatedObject = ((ConditionDriverBridge) driver).getConditionDriver();
        else
            annotatedObject = driver;
        for(RealPropertyImpl<?> property : annotationParser.findProperties(logger, "", annotatedObject))
            properties.add(property);
        errorValue.setValue(null);
        driverLoadedValue.setValue(true);
        driver.init(logger, this);
    }

    private void uninitDriver() {
        if(driver != null) {
            driver.uninit();
            driverLoadedValue.setValue(false);
            errorValue.setValue("Driver not loaded");
            driver = null;
            for (RealPropertyImpl<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), connection);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), connection);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), connection);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), connection);
        properties.init(ChildUtil.name(name, Condition.PROPERTIES_ID), connection);
        satisfiedValue.init(ChildUtil.name(name, Condition.SATISFIED_ID), connection);
        childConditions.init(ChildUtil.name(name, Condition.PROPERTIES_ID), connection);
        addConditionCommand.init(ChildUtil.name(name, Condition.PROPERTIES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        _stop();
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
        for(Condition.Listener<? super RealConditionImpl> listener : listeners)
            listener.renamed(RealConditionImpl.this, RealConditionImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    public <DRIVER extends ConditionDriver> DRIVER getDriver() {
        return (DRIVER) driver;
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
    public RealPropertyImpl<PluginDependency<ConditionDriver.Factory<?>>> getDriverProperty() {
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
    public final RealListGeneratedImpl<RealPropertyImpl<?>> getProperties() {
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
    public RealListPersistedImpl<Condition.Data, RealConditionImpl> getConditions() {
        return childConditions;
    }

    @Override
    public RealCommandImpl getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public final void addCondition(RealConditionImpl condition) {
        childConditions.add(condition);
    }

    protected final void remove() {
        removeCallback.remove(this);
    }

    protected final void _start() {
        try {
            if(isDriverLoaded())
                driver.init(logger, this);
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start condition: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(isDriverLoaded())
            driver.uninit();
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
            for(Condition.Listener<? super RealConditionImpl> listener : listeners)
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
            driver.init(logger, this);
    }

    @Override
    public final void stop() {
        for(String id : childSatisfied.keySet())
            childListenerRegistrations.get(id).remove();
        for(RealCondition childCondition : childConditions)
            childCondition.stop();
        if(isDriverLoaded())
            driver.uninit();
    }

    @Override
    public void conditionSatisfied(RealConditionImpl condition, boolean satisfied) {
        // todo call driver
    }

    @Override
    public void renamed(RealConditionImpl renameable, String oldName, String newName) {
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
        RealConditionImpl create(Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 RealListPersistedImpl.RemoveCallback<RealConditionImpl> removeCallback);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<Condition.Data, RealConditionImpl> {

        private final RealConditionImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealConditionImpl create(Logger logger, Condition.Data data, RealListPersistedImpl.RemoveCallback<RealConditionImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription(), removeCallback);
        }
    }
}
