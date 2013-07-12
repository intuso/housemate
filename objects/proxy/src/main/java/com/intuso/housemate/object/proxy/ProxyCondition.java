package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <ADD_COMMAND> the type of the add command
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 */
public abstract class ProxyCondition<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            VALUE extends ProxyValue<?, ?, VALUE>,
            PROPERTIES extends ProxyList<?, ?, PropertyData, ? extends ProxyProperty<?, ?, ?, ?, ?>, PROPERTIES>,
            ADD_COMMAND extends ProxyCommand<?, ?, ?, ?, ADD_COMMAND>,
            CONDITION extends ProxyCondition<RESOURCES, CHILD_RESOURCES, VALUE, PROPERTIES, ADD_COMMAND, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<?, ?, ConditionData, CONDITION, CONDITIONS>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, ConditionData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, CONDITION, ConditionListener<? super CONDITION>>
        implements Condition<VALUE, VALUE, PROPERTIES, ADD_COMMAND, CONDITION, CONDITIONS> {

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyCondition(RESOURCES resources, CHILD_RESOURCES childResources, ConditionData data) {
        super(resources, childResources, data);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(getSatisfiedValue().addObjectListener(new ValueListener<VALUE>() {

            @Override
            public void valueChanging(VALUE value) {
                // do nothing
            }

            @Override
            public void valueChanged(VALUE value) {
                for(ConditionListener listener : getObjectListeners())
                    listener.conditionSatisfied(getThis(), isSatisfied());
            }
        }));
        result.add(getErrorValue().addObjectListener(new ValueListener<VALUE>() {

            @Override
            public void valueChanging(VALUE value) {
                // do nothing
            }

            @Override
            public void valueChanged(VALUE value) {
                for(ConditionListener listener : getObjectListeners())
                    listener.conditionError(getThis(), getError());
            }
        }));
        return result;
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getWrapper(PROPERTIES_ID);
    }

    @Override
    public CONDITIONS getConditions() {
        return (CONDITIONS) getWrapper(CONDITIONS_ID);
    }

    @Override
    public ADD_COMMAND getAddConditionCommand() {
        return (ADD_COMMAND) getWrapper(Automation.ADD_CONDITION_ID);
    }

    @Override
    public final VALUE getErrorValue() {
        return (VALUE) getWrapper(ERROR_ID);
    }

    @Override
    public final String getError() {
        VALUE error = getErrorValue();
        return error.getTypeInstances() != null ? error.getTypeInstances().getFirstValue() : null;
    }

    @Override
    public final VALUE getSatisfiedValue() {
        return (VALUE) getWrapper(SATISFIED_ID);
    }

    @Override
    public final boolean isSatisfied() {
        VALUE satisfied = getSatisfiedValue();
        return satisfied.getTypeInstances() != null && satisfied.getTypeInstances().getFirstValue() != null
                ? Boolean.parseBoolean(satisfied.getTypeInstances().getFirstValue()) : false;
    }
}
