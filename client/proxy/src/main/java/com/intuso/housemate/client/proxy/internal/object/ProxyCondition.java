package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Condition;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyFailable;
import com.intuso.housemate.client.proxy.internal.ProxyRemoveable;
import com.intuso.housemate.client.proxy.internal.ProxyUsesDriver;
import com.intuso.housemate.client.proxy.internal.object.view.*;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <COMMAND> the type of the add command
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 */
public abstract class ProxyCondition<
        COMMAND extends ProxyCommand<?, ?, ?>,
        VALUE extends ProxyValue<?, ?>,
        PROPERTY extends ProxyProperty<?, ?, ?>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        CONDITION extends ProxyCondition<COMMAND, VALUE, PROPERTY, PROPERTIES, CONDITION, CONDITIONS>,
        CONDITIONS extends ProxyList<CONDITION, ?>>
        extends ProxyObject<Condition.Data, Condition.Listener<? super CONDITION>, ConditionView>
        implements Condition<COMMAND, COMMAND, VALUE, PROPERTY, VALUE, VALUE, PROPERTIES, COMMAND, CONDITIONS, CONDITION>,
        ProxyRemoveable<COMMAND>,
        ProxyFailable<VALUE>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    private final ProxyObject.Factory<COMMAND> commandFactory;
    private final ProxyObject.Factory<VALUE> valueFactory;
    private final ProxyObject.Factory<PROPERTY> propertyFactory;
    private final ProxyObject.Factory<PROPERTIES> propertiesFactory;
    private final ProxyObject.Factory<CONDITIONS> conditionsFactory;

    private COMMAND renameCommand;
    private COMMAND removeCommand;
    private VALUE errorValue;
    private PROPERTY driverProperty;
    private VALUE driverLoadedValue;
    private PROPERTIES properties;
    private CONDITIONS conditions;
    private COMMAND addConditionCommand;
    private VALUE satisfiedValue;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyCondition(Logger logger,
                          String name,
                          ManagedCollectionFactory managedCollectionFactory,
                          Receiver.Factory receiverFactory,
                          ProxyObject.Factory<COMMAND> commandFactory,
                          ProxyObject.Factory<VALUE> valueFactory,
                          ProxyObject.Factory<PROPERTY> propertyFactory,
                          ProxyObject.Factory<PROPERTIES> propertiesFactory,
                          ProxyObject.Factory<CONDITIONS> conditionsFactory) {
        super(logger, name, Condition.Data.class, managedCollectionFactory, receiverFactory);
        this.commandFactory = commandFactory;
        this.valueFactory = valueFactory;
        this.propertyFactory = propertyFactory;
        this.propertiesFactory = propertiesFactory;
        this.conditionsFactory = conditionsFactory;
    }

    @Override
    public ConditionView createView() {
        return new ConditionView();
    }

    @Override
    public void view(ConditionView view) {

        super.view(view);

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(renameCommand == null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(errorValue == null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(driverProperty == null)
                    driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
                if(driverLoadedValue == null)
                    driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
                if(properties == null)
                    properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
                if(conditions == null)
                    conditions = conditionsFactory.create(ChildUtil.logger(logger, CONDITIONS_ID), ChildUtil.name(name, CONDITIONS_ID));
                if(addConditionCommand == null)
                    addConditionCommand = commandFactory.create(ChildUtil.logger(logger, ADD_CONDITION_ID), ChildUtil.name(name, ADD_CONDITION_ID));
                if(satisfiedValue == null)
                    satisfiedValue = valueFactory.create(ChildUtil.logger(logger, SATISFIED_ID), ChildUtil.name(name, SATISFIED_ID));
                break;
            case SELECTION:
                if(renameCommand == null && view.getRenameCommandView() != null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null && view.getRemoveCommandView() != null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(errorValue == null && view.getErrorValueView() != null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(driverProperty == null && view.getDriverPropertyView() != null)
                    driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
                if(driverLoadedValue == null && view.getDriverLoadedValueView() != null)
                    driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
                if(properties == null && view.getPropertiesView() != null)
                    properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
                if(conditions == null && view.getConditionsView() != null)
                    conditions = conditionsFactory.create(ChildUtil.logger(logger, CONDITIONS_ID), ChildUtil.name(name, CONDITIONS_ID));
                if(addConditionCommand == null && view.getAddConditionCommandView() != null)
                    addConditionCommand = commandFactory.create(ChildUtil.logger(logger, ADD_CONDITION_ID), ChildUtil.name(name, ADD_CONDITION_ID));
                if(satisfiedValue == null && view.getSatisfiedValueView() != null)
                    satisfiedValue = valueFactory.create(ChildUtil.logger(logger, SATISFIED_ID), ChildUtil.name(name, SATISFIED_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                renameCommand.view(new CommandView(View.Mode.ANCESTORS));
                removeCommand.view(new CommandView(View.Mode.ANCESTORS));
                errorValue.view(new ValueView(View.Mode.ANCESTORS));
                driverProperty.view(new PropertyView(View.Mode.ANCESTORS));
                driverLoadedValue.view(new ValueView(View.Mode.ANCESTORS));
                properties.view(new ListView(View.Mode.ANCESTORS));
                conditions.view(new ListView(View.Mode.ANCESTORS));
                addConditionCommand.view(new CommandView(View.Mode.ANCESTORS));
                satisfiedValue.view(new ValueView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getRenameCommandView() != null)
                    renameCommand.view(view.getRenameCommandView());
                if(view.getRemoveCommandView() != null)
                    removeCommand.view(view.getRemoveCommandView());
                if(view.getErrorValueView() != null)
                    errorValue.view(view.getErrorValueView());
                if(view.getDriverPropertyView() != null)
                    driverProperty.view(view.getDriverPropertyView());
                if(view.getDriverLoadedValueView() != null)
                    driverLoadedValue.view(view.getDriverLoadedValueView());
                if(view.getPropertiesView() != null)
                    properties.view(view.getPropertiesView());
                if(view.getConditionsView() != null)
                    conditions.view(view.getConditionsView());
                if(view.getAddConditionCommandView() != null)
                    addConditionCommand.view(view.getAddConditionCommandView());
                if(view.getSatisfiedValueView() != null)
                    satisfiedValue.view(view.getSatisfiedValueView());
                break;
        }
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(renameCommand != null)
            renameCommand.uninit();
        if(removeCommand != null)
            removeCommand.uninit();
        if(errorValue != null)
            errorValue.uninit();
        if(driverProperty != null)
            driverProperty.uninit();
        if(driverLoadedValue != null)
            driverLoadedValue.uninit();
        if(properties != null)
            properties.uninit();
        if(conditions != null)
            conditions.uninit();
        if(addConditionCommand != null)
            addConditionCommand.uninit();
        if(satisfiedValue != null)
            satisfiedValue.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public PROPERTY getDriverProperty() {
        return driverProperty;
    }

    @Override
    public final boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null
                && driverLoadedValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(driverLoadedValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public final PROPERTIES getProperties() {
        return properties;
    }

    @Override
    public COMMAND getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public CONDITIONS getConditions() {
        return conditions;
    }

    public final boolean isSatisfied() {
        return satisfiedValue.getValue() != null
                && satisfiedValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(satisfiedValue.getValue().getFirstValue());
    }

    @Override
    public final VALUE getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id)) {
            if(renameCommand == null)
                renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
            return renameCommand;
        } else if(REMOVE_ID.equals(id)) {
            if(removeCommand == null)
                removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
            return removeCommand;
        } else if(ERROR_ID.equals(id)) {
            if(errorValue == null)
                errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
            return errorValue;
        } else if(DRIVER_ID.equals(id)) {
            if(driverProperty == null)
                driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
            return driverProperty;
        } else if(DRIVER_LOADED_ID.equals(id)) {
            if(driverLoadedValue == null)
                driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
            return driverLoadedValue;
        } else if(PROPERTIES_ID.equals(id)) {
            if(properties == null)
                properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
            return properties;
        } else if(CONDITIONS_ID.equals(id)) {
            if(conditions == null)
                conditions = conditionsFactory.create(ChildUtil.logger(logger, CONDITIONS_ID), ChildUtil.name(name, CONDITIONS_ID));
            return conditions;
        } else if(ADD_CONDITION_ID.equals(id)) {
            if(addConditionCommand == null)
                addConditionCommand = commandFactory.create(ChildUtil.logger(logger, ADD_CONDITION_ID), ChildUtil.name(name, ADD_CONDITION_ID));
            return addConditionCommand;
        } else if(SATISFIED_ID.equals(id)) {
            if(satisfiedValue == null)
                satisfiedValue = valueFactory.create(ChildUtil.logger(logger, SATISFIED_ID), ChildUtil.name(name, SATISFIED_ID));
            return satisfiedValue;
        }
        return null;
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:16
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxyCondition<
            ProxyCommand.Simple,
            ProxyValue.Simple,
            ProxyProperty.Simple,
            ProxyList.Simple<ProxyProperty.Simple>,
            Simple,
            ProxyList.Simple<Simple>> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Sender.Factory senderFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyProperty.Simple> propertyFactory,
                      Factory<ProxyList.Simple<ProxyProperty.Simple>> propertiesFactory,
                      Factory<ProxyList.Simple<Simple>> conditionsFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, commandFactory, valueFactory, propertyFactory, propertiesFactory, conditionsFactory);
        }
    }
}
