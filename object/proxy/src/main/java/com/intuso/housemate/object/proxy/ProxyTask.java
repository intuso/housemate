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
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <TASK> the type of the task
 */
public abstract class ProxyTask<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            VALUE extends ProxyValue<?, ?, VALUE>,
            PROPERTIES extends ProxyList<?, ?, PropertyWrappable, ? extends ProxyProperty<?, ?, ?, ?, ?>, PROPERTIES>,
            TASK extends ProxyTask<RESOURCES, CHILD_RESOURCES, VALUE, PROPERTIES, TASK>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, TaskWrappable, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, TASK, TaskListener<? super TASK>>
        implements Task<VALUE, VALUE, PROPERTIES, TASK> {

    private VALUE executing;
    private VALUE error;
    private PROPERTIES propertyList;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    public ProxyTask(RESOURCES resources, CHILD_RESOURCES childResources, TaskWrappable wrappable) {
        super(resources, childResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        error = (VALUE)getWrapper(ERROR_ID);
        executing = (VALUE)getWrapper(EXECUTING_TYPE);
        propertyList = (PROPERTIES)getWrapper(PROPERTIES_ID);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration>result = super.registerListeners();
        result.add(executing.addObjectListener(new ValueListener<VALUE>() {

            @Override
            public void valueChanging(VALUE value) {
                // do nothing
            }

            @Override
            public void valueChanged(VALUE value) {
                for(TaskListener listener : getObjectListeners())
                    listener.taskExecuting(getThis(), isExecuting());
            }
        }));
        result.add(error.addObjectListener(new ValueListener<VALUE>() {

            @Override
            public void valueChanging(VALUE value) {
                // do nothing
            }

            @Override
            public void valueChanged(VALUE value) {
                for(TaskListener listener : getObjectListeners())
                    listener.taskError(getThis(), getError());
            }
        }));
        return result;
    }

    @Override
    public final PROPERTIES getProperties() {
        return propertyList;
    }

    @Override
    public final VALUE getErrorValue() {
        return error;
    }

    @Override
    public final String getError() {
        return error.getTypeInstance() != null ? error.getTypeInstance().getValue() : null;
    }

    @Override
    public final VALUE getExecutingValue() {
        return executing;
    }

    @Override
    public final boolean isExecuting() {
        return executing.getTypeInstance() != null ? Boolean.parseBoolean(executing.getTypeInstance().getValue()) : null;
    }
}
