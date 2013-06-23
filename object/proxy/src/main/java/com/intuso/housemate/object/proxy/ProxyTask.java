package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.api.object.task.TaskWrappable;
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
public abstract class ProxyTask<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            V extends ProxyValue<?, ?, V>,
            PL extends ProxyList<?, ?, PropertyWrappable, ? extends ProxyProperty<?, ?, ?, ?, ?>, PL>,
            T extends ProxyTask<R, SR, V, PL, T>>
        extends ProxyObject<R, SR, TaskWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, T, TaskListener<? super T>>
        implements Task<V, V, PL, T> {

    private V executing;
    private V error;
    private PL propertyList;

    public ProxyTask(R resources, SR subResources, TaskWrappable wrappable) {
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
            public void valueChanging(V value) {
                // do nothing
            }

            @Override
            public void valueChanged(V value) {
                for(TaskListener listener : getObjectListeners())
                    listener.taskExecuting(getThis(), isExecuting());
            }
        }));
        result.add(error.addObjectListener(new ValueListener<V>() {

            @Override
            public void valueChanging(V value) {
                // do nothing
            }

            @Override
            public void valueChanged(V value) {
                for(TaskListener listener : getObjectListeners())
                    listener.taskError(getThis(), getError());
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
        return error.getTypeInstance() != null ? error.getTypeInstance().getValue() : null;
    }

    public final V getExecutingValue() {
        return executing;
    }

    public final boolean isExecuting() {
        return executing.getTypeInstance() != null ? Boolean.parseBoolean(executing.getTypeInstance().getValue()) : null;
    }
}
