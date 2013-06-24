package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 */
public abstract class ProxyCondition<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            V extends ProxyValue<?, ?, V>,
            PL extends ProxyList<?, ?, PropertyWrappable, ? extends ProxyProperty<?, ?, ?, ?, ?>, PL>, AC extends ProxyCommand<?, ?, ?, ?, AC>,
            C extends ProxyCondition<R, SR, V, PL, AC, C, CL>, CL extends ProxyList<?, ?, ConditionWrappable, C, CL>>
        extends ProxyObject<R, SR, ConditionWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, C, ConditionListener<? super C>>
        implements Condition<V, V, PL, AC, C, CL> {

    private V error;
    private V satisfied;
    private PL propertyList;
    private CL conditionList;
    private AC addConditionCommand;

    public ProxyCondition(R resources, SR subResources, ConditionWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected final void getChildObjects() {
        super.getChildObjects();
        error = (V)getWrapper(ERROR_ID);
        satisfied = (V)getWrapper(SATISFIED_ID);
        propertyList = (PL)getWrapper(PROPERTIES_ID);
        conditionList = (CL)getWrapper(CONDITIONS_ID);
        addConditionCommand = (AC)getWrapper(Automation.ADD_CONDITION_ID);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(satisfied.addObjectListener(new ValueListener<V>() {

            @Override
            public void valueChanging(V value) {
                // do nothing
            }

            @Override
            public void valueChanged(V value) {
                for(ConditionListener listener : getObjectListeners())
                    listener.conditionSatisfied(getThis(), isSatisfied());
            }
        }));
        result.add(error.addObjectListener(new ValueListener<V>() {

            @Override
            public void valueChanging(V value) {
                // do nothing
            }

            @Override
            public void valueChanged(V value) {
                for(ConditionListener listener : getObjectListeners())
                    listener.conditionError(getThis(), getError());
            }
        }));
        return result;
    }

    @Override
    public final PL getProperties() {
        return propertyList;
    }

    @Override
    public CL getConditions() {
        return conditionList;
    }

    public AC getAddConditionCommand() {
        return addConditionCommand;
    }

    public final V getErrorValue() {
        return error;
    }

    public final String getError() {
        return error.getTypeInstance() != null ? error.getTypeInstance().getValue() : null;
    }

    public final V getSatisfiedValue() {
        return satisfied;
    }

    public final boolean isSatisfied() {
        return satisfied.getTypeInstance() != null ? Boolean.parseBoolean(satisfied.getTypeInstance().getValue()) : null;
    }
}
