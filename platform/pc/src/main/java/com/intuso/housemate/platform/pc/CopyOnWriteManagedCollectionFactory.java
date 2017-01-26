package com.intuso.housemate.platform.pc;

import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 20/04/14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class CopyOnWriteManagedCollectionFactory implements ManagedCollectionFactory {
    @Override
    public <LISTENER> ManagedCollection<LISTENER> create() {
        return new ManagedCollection<>(new CopyOnWriteArrayList<LISTENER>());
    }
}
