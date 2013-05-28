package com.intuso.housemate.api.object;

import com.intuso.utilities.listener.Listener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 19/10/12
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectLifecycleListener extends Listener {
    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?, ?> object);
    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?, ?> object);
}
