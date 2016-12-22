package com.intuso.housemate.platform.pc;

import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 20/04/14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class CopyOnWriteListenersFactory implements ListenersFactory {
    @Override
    public <LISTENER> Listeners<LISTENER> create() {
        return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
    }
}
