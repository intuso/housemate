package com.intuso.housemate.api.object.root.proxy;

import com.intuso.housemate.api.object.root.RootListener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/04/13
 * Time: 08:58
 * To change this template use File | Settings | File Templates.
 */
public interface ProxyRootListener<R extends ProxyRoot<?, ?, ?, ?, ?, ?>> extends RootListener<R> {
    public void loaded(R root);
}
