package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <COMMAND> the type of the add command
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 */
public abstract class ProxyCondition<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            PROPERTIES extends ProxyList<PropertyData, ? extends ProxyProperty<?, ?, ?>, PROPERTIES>,
            CONDITION extends ProxyCondition<COMMAND, VALUE, PROPERTIES, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<ConditionData, CONDITION, CONDITIONS>>
        extends ProxyObject<ConditionData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, CONDITION, ConditionListener<? super CONDITION>>
        implements Condition<COMMAND, VALUE, VALUE, PROPERTIES, COMMAND, CONDITION, CONDITIONS> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyCondition(Log log, ListenersFactory listenersFactory, ConditionData data) {
        super(log, listenersFactory, data);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        final java.util.List<ListenerRegistration> result = super.registerListeners();
        addChildLoadedListener(SATISFIED_ID, new ChildLoadedListener<CONDITION, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(CONDITION object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
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
            }
        });
        addChildLoadedListener(ERROR_ID, new ChildLoadedListener<CONDITION, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(CONDITION object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
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
            }
        });
        return result;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(REMOVE_ID);
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getChild(PROPERTIES_ID);
    }

    @Override
    public CONDITIONS getConditions() {
        return (CONDITIONS) getChild(CONDITIONS_ID);
    }

    @Override
    public COMMAND getAddConditionCommand() {
        return (COMMAND) getChild(Automation.ADD_CONDITION_ID);
    }

    @Override
    public final VALUE getErrorValue() {
        return (VALUE) getChild(ERROR_ID);
    }

    @Override
    public final String getError() {
        VALUE error = getErrorValue();
        return error.getTypeInstances() != null ? error.getTypeInstances().getFirstValue() : null;
    }

    @Override
    public final VALUE getSatisfiedValue() {
        return (VALUE) getChild(SATISFIED_ID);
    }

    @Override
    public final boolean isSatisfied() {
        VALUE satisfied = getSatisfiedValue();
        return satisfied.getTypeInstances() != null && satisfied.getTypeInstances().getFirstValue() != null
                ? Boolean.parseBoolean(satisfied.getTypeInstances().getFirstValue()) : false;
    }
}
