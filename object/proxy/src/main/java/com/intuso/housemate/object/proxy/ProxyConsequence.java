package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.consequence.Consequence;
import com.intuso.housemate.api.object.consequence.ConsequenceListener;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyConsequence<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            V extends ProxyValue<?, ?, V>,
            P extends ProxyProperty<?, ?, ?, ?, P>,
            PL extends ProxyList<?, ?, PropertyWrappable, P, PL>,
            C extends ProxyConsequence<R, SR, V, P, PL, C>>
        extends ProxyObject<R, SR, ConsequenceWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, C, ConsequenceListener<? super C>>
        implements Consequence<P, V, V, PL, C> {

    private V executing;
    private V error;
    private PL propertyList;

    public ProxyConsequence(R resources, SR subResources, ConsequenceWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        error = (V)getWrapper(ERROR);
        executing = (V)getWrapper(EXECUTING);
        propertyList = (PL)getWrapper(PROPERTIES);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration>result = super.registerListeners();
        result.add(executing.addObjectListener(new ValueListener<V>() {
            @Override
            public void valueChanged(V value) {
                for(ConsequenceListener listener : getObjectListeners())
                    listener.consequenceExecuting(getThis(), isExecuting());
            }
        }));
        result.add(error.addObjectListener(new ValueListener<V>() {
            @Override
            public void valueChanged(V value) {
                for(ConsequenceListener listener : getObjectListeners())
                    listener.consequenceError(getThis(), error.getValue());
            }
        }));
        return result;
    }

    @Override
    public final PL getProperties() {
        return propertyList;
    }

    public final V getErrorValue() {
        return error;
    }

    public final String getError() {
        return error.getValue();
    }

    public final V getExecutingValue() {
        return executing;
    }

    public final boolean isExecuting() {
        return Boolean.parseBoolean(executing.getValue());
    }
}
