package com.intuso.housemate.web.client;

import com.google.common.collect.Lists;
import com.intuso.utilities.collection.Listener;
import com.intuso.utilities.collection.Listeners;
import com.intuso.utilities.collection.ListenersFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 20/04/14
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
public class GWTListenersFactory implements ListenersFactory {
    @Override
    public <LISTENER extends Listener> Listeners<LISTENER> create() {
        return new Listeners<>(Lists.<LISTENER>newArrayList());
    }
}
