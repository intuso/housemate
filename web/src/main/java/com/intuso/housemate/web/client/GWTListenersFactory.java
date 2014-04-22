package com.intuso.housemate.web.client;

import com.google.common.collect.Lists;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;

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
        return new Listeners<LISTENER>(Lists.<LISTENER>newArrayList());
    }
}
