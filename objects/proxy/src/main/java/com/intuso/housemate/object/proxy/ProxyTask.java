package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.task.TaskListener;
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
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>, ?>,
            CHILD_RESOURCES extends ProxyResources<?, ?>,
            COMMAND extends ProxyCommand<?, ?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, ?, VALUE>,
            PROPERTIES extends ProxyList<?, ?, PropertyData, ? extends ProxyProperty<?, ?, ?, ?, ?>, PROPERTIES>,
            TASK extends ProxyTask<RESOURCES, CHILD_RESOURCES, COMMAND, VALUE, PROPERTIES, TASK>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, TaskData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, TASK, TaskListener<? super TASK>>
        implements Task<COMMAND, VALUE, VALUE, PROPERTIES, TASK> {

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyTask(RESOURCES resources, CHILD_RESOURCES childResources, TaskData data) {
        super(resources, childResources, data);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        final java.util.List<ListenerRegistration>result = super.registerListeners();
        addChildLoadedListener(EXECUTING_ID, new ChildLoadedListener<TASK, ProxyObject<?, ?, ?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(TASK object, ProxyObject<?, ?, ?, ?, ?, ?, ?> proxyObject) {
                result.add(getExecutingValue().addObjectListener(new ValueListener<VALUE>() {

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
            }
        });
        addChildLoadedListener(ERROR_ID, new ChildLoadedListener<TASK, ProxyObject<?, ?, ?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(TASK object, ProxyObject<?, ?, ?, ?, ?, ?, ?> proxyObject) {
                result.add(getErrorValue().addObjectListener(new ValueListener<VALUE>() {

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
    public final VALUE getErrorValue() {
        return (VALUE) getChild(ERROR_ID);
    }

    @Override
    public final String getError() {
        VALUE error = getErrorValue();
        return error.getTypeInstances() != null ? error.getTypeInstances().getFirstValue() : null;
    }

    @Override
    public final VALUE getExecutingValue() {
        return (VALUE) getChild(EXECUTING_ID);
    }

    @Override
    public final boolean isExecuting() {
        VALUE executing = getExecutingValue();
        return executing.getTypeInstances() != null && executing.getTypeInstances().getFirstValue() != null
                ? Boolean.parseBoolean(executing.getTypeInstances().getFirstValue()) : false;
    }
}
