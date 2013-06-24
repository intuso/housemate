package com.intuso.housemate.api.object.root.proxy;

import com.intuso.housemate.api.object.root.RootListener;

/**
 *
 * Listener interface for proxy roots
 */
public interface ProxyRootListener<R extends ProxyRoot<?, ?, ?, ?, ?, ?>> extends RootListener<R> {

    /**
     * Notifies that the child objects have been loaded
     * @param root the root object whose child objects were loaded
     */
    public void loaded(R root);
}
