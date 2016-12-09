package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.real.api.object.RealList;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Iterator;

/**
 * Created by tomc on 05/11/15.
 */
public class RealListBridgeReverse<FROM extends com.intuso.housemate.client.api.internal.object.Object<?>, TO extends Object<?>>
        implements RealList<TO, RealListBridgeReverse<FROM, TO>> {

    private final com.intuso.housemate.client.real.api.internal.object.RealList<FROM, ?> list;
    private final Function<? super FROM, ? extends TO> convertFrom;
    private final Function<? super TO, ? extends FROM> convertTo;

    public RealListBridgeReverse(com.intuso.housemate.client.real.api.internal.object.RealList<FROM, ?> list, Function<? super FROM, ? extends TO> convertFrom, Function<? super TO, ? extends FROM> convertTo) {
        this.list = list;
        this.convertFrom = convertFrom;
        this.convertTo = convertTo;
    }

    public com.intuso.housemate.client.real.api.internal.object.RealList<FROM, ?> getList() {
        return list;
    }

    @Override
    public void add(TO element) {
        list.add(convertTo.apply(element));
    }

    @Override
    public TO remove(String id) {
        return convertFrom.apply(list.remove(id));
    }

    @Override
    public TO get(String name) {
        return convertFrom.apply(list.get(name));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super TO, ? super RealListBridgeReverse<FROM, TO>> listener, boolean callForExistingElements) {
        return list.addObjectListener(new ListenerBridge(listener), callForExistingElements);
    }

    @Override
    public String getId() {
        return list.getId();
    }

    @Override
    public String getName() {
        return list.getName();
    }

    @Override
    public String getDescription() {
        return list.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super TO, ? super RealListBridgeReverse<FROM, TO>> listener) {
        return null; // todo
    }

    @Override
    public Iterator<TO> iterator() {
        return Iterators.transform(list.iterator(), convertFrom);
    }

    private class ListenerBridge implements com.intuso.housemate.client.real.api.internal.object.RealList.Listener<FROM, com.intuso.housemate.client.real.api.internal.object.RealList<FROM, ?>> {

        private final Listener<? super TO, ? super RealListBridgeReverse<FROM, TO>> listener;

        private ListenerBridge(Listener<? super TO, ? super RealListBridgeReverse<FROM, TO>> listener) {
            this.listener = listener;
        }

        @Override
        public void elementAdded(com.intuso.housemate.client.real.api.internal.object.RealList<FROM, ?> list, FROM element) {
            listener.elementAdded(RealListBridgeReverse.this, convertFrom.apply(element));
        }

        @Override
        public void elementRemoved(com.intuso.housemate.client.real.api.internal.object.RealList<FROM, ?> list, FROM element) {
            listener.elementRemoved(RealListBridgeReverse.this, convertFrom.apply(element));
        }
    }
}
