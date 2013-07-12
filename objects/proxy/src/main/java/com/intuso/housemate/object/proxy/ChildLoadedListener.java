package com.intuso.housemate.object.proxy;

import com.intuso.utilities.listener.Listener;

public interface ChildLoadedListener<
            OBJECT extends ProxyObject<?, ?, ?, ?, CHILD, ?, ?>,
            CHILD extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>
        extends Listener {
    public void childLoaded(OBJECT object, CHILD child);
}
