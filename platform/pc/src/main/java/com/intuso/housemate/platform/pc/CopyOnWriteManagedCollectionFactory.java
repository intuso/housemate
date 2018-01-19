package com.intuso.housemate.platform.pc;

import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 20/04/14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class CopyOnWriteManagedCollectionFactory implements ManagedCollectionFactory {

    @Override
    public <LISTENER> ManagedCollection<LISTENER> createSet() {
        return new ManagedCollection<>(Collections.synchronizedSet(new HashSet<>()));
    }

    @Override
    public <LISTENER> ManagedCollection<LISTENER> createList() {
        return new ManagedCollection<>(Collections.synchronizedList(new LinkedList<>()));
    }
}
