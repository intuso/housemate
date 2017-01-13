package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Iterator;

/**
 * Created by tomc on 05/11/15.
 */
public class RealListBridge<FROM extends Object, TO extends com.intuso.housemate.client.api.internal.object.Object<?>>
        implements RealList<TO, RealListBridge<FROM, TO>> {

    private final com.intuso.housemate.client.v1_0.real.api.RealList<FROM, ?> list;
    private final Function<? super FROM, ? extends TO> convertFrom;
    private final Function<? super TO, ? extends FROM> convertTo;

    public RealListBridge(com.intuso.housemate.client.v1_0.real.api.RealList<FROM, ?> list, Function<? super FROM, ? extends TO> convertFrom, Function<? super TO, ? extends FROM> convertTo) {
        this.list = list;
        this.convertFrom = convertFrom;
        this.convertTo = convertTo;
    }

    public com.intuso.housemate.client.v1_0.real.api.RealList<FROM, ?> getList() {
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
    public TO get(String id) {
        return convertFrom.apply(list.get(id));
    }

    @Override
    public TO getByName(String name) {
        return convertFrom.apply(list.getByName(name));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super TO, ? super RealListBridge<FROM, TO>> listener, boolean callForExistingElements) {
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
    public ListenerRegistration addObjectListener(Listener<? super TO, ? super RealListBridge<FROM, TO>> listener) {
        return null; // todo
    }

    @Override
    public Iterator<TO> iterator() {
        return Iterators.transform(list.iterator(), convertFrom);
    }

    private class ListenerBridge implements com.intuso.housemate.client.v1_0.real.api.RealList.Listener<FROM, com.intuso.housemate.client.v1_0.real.api.RealList<FROM, ?>> {

        private final Listener<? super TO, ? super RealListBridge<FROM, TO>> listener;

        private ListenerBridge(Listener<? super TO, ? super RealListBridge<FROM, TO>> listener) {
            this.listener = listener;
        }

        @Override
        public void elementAdded(com.intuso.housemate.client.v1_0.real.api.RealList<FROM, ?> list, FROM element) {
            listener.elementAdded(RealListBridge.this, convertFrom.apply(element));
        }

        @Override
        public void elementRemoved(com.intuso.housemate.client.v1_0.real.api.RealList<FROM, ?> list, FROM element) {
            listener.elementRemoved(RealListBridge.this, convertFrom.apply(element));
        }
    }
}
