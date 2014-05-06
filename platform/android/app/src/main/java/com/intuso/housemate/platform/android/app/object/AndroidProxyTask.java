package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyTask;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyTask extends ProxyTask<AndroidProxyCommand, AndroidProxyValue,
        AndroidProxyList<PropertyData, AndroidProxyProperty>, AndroidProxyTask> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxyTask(Log log, ListenersFactory listenersFactory, TaskData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return factory.create(data);
    }
}
