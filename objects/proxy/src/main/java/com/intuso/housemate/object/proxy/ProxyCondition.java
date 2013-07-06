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

    private VALUE error;
    private VALUE satisfied;
    private PROPERTIES propertyList;
    private CONDITIONS conditionList;
    private ADD_COMMAND addConditionCommand;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyCondition(RESOURCES resources, CHILD_RESOURCES childResources, ConditionData data) {
        super(resources, childResources, data);
    }

    @Override
    protected final void getChildObjects() {
        super.getChildObjects();
        error = (VALUE)getWrapper(ERROR_ID);
        satisfied = (VALUE)getWrapper(SATISFIED_ID);
        propertyList = (PROPERTIES)getWrapper(PROPERTIES_ID);
        conditionList = (CONDITIONS)getWrapper(CONDITIONS_ID);
        addConditionCommand = (ADD_COMMAND)getWrapper(Automation.ADD_CONDITION_ID);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(satisfied.addObjectListener(new ValueListener<VALUE>() {

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
        result.add(error.addObjectListener(new ValueListener<VALUE>() {

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
        return propertyList;
    }

    @Override
    public CONDITIONS getConditions() {
        return conditionList;
    }

    @Override
    public ADD_COMMAND getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public final VALUE getErrorValue() {
        return error;
    }

    @Override
    public final String getError() {
        return error.getTypeInstances() != null ? error.getTypeInstances().getFirstValue() : null;
    }

    @Override
    public final VALUE getSatisfiedValue() {
        return satisfied;
    }

    @Override
    public final boolean isSatisfied() {
        return satisfied.getTypeInstances() != null && satisfied.getTypeInstances().getFirstValue() != null
                ? Boolean.parseBoolean(satisfied.getTypeInstances().getFirstValue()) : false;
    }
}
