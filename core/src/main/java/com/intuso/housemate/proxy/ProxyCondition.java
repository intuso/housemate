package com.intuso.housemate.proxy;

import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.core.object.property.PropertyWrappable;
import com.intuso.housemate.core.object.condition.Condition;
import com.intuso.housemate.core.object.condition.ConditionListener;
import com.intuso.housemate.core.object.condition.ConditionWrappable;
import com.intuso.housemate.real.impl.type.BooleanType;
import com.intuso.listeners.ListenerRegistration;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyCondition<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            P extends ProxyProperty<?, ?, ?, ?, P>, V extends ProxyValue<?, ?, V>,
            PL extends ProxyList<?, ?, PropertyWrappable, P, PL>, AC extends ProxyCommand<?, ?, ?, ?, AC>,
            C extends ProxyCondition<R, SR, P, V, PL, AC, C, CL>, CL extends ProxyList<?, ?, ConditionWrappable, C, CL>>
        extends ProxyObject<R, SR, ConditionWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, C, ConditionListener<? super C>>
        implements Condition<P, V, V, PL, AC, C, CL> {

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
        error = (V)getWrapper(ERROR);
        satisfied = (V)getWrapper(SATISFIED);
        propertyList = (PL)getWrapper(PROPERTIES);
        conditionList = (CL)getWrapper(CONDITIONS);
        addConditionCommand = (AC)getWrapper(ADD_CONDITION);
    }

    @Override
    protected java.util.List<ListenerRegistration<?>> registerListeners() {
        java.util.List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(satisfied.addObjectListener(new ValueListener<V>() {
            @Override
            public void valueChanged(V value) {
                for(ConditionListener listener : getObjectListeners())
                    listener.conditionSatisfied(getThis(), isSatisfied());
            }
        }));
        result.add(error.addObjectListener(new ValueListener<V>() {
            @Override
            public void valueChanged(V value) {
                for(ConditionListener listener : getObjectListeners())
                    listener.conditionError(getThis(), error.getValue());
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
        return error.getValue();
    }

    public final V getSatisfiedValue() {
        return satisfied;
    }

    public final boolean isSatisfied() {
        return BooleanType.SERIALISER.deserialise(satisfied.getValue());
    }
}
