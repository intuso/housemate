package com.intuso.housemate.client.api.internal.object;

import com.intuso.utilities.collection.ManagedCollection;

import java.util.Iterator;

/**
 * Created by tomc on 02/05/17.
 */
public class ConvertingList<FROM, TO> implements List<TO, ConvertingList<FROM, TO>> {

    private final List<FROM, ?> list;
    private final Converter<? super FROM, ? extends TO> converter;

    public ConvertingList(List<FROM, ?> list, Converter<? super FROM, ? extends TO> converter) {
        this.list = list;
        this.converter = converter;
    }

    @Override
    public String getObjectClass() {
        return Data.OBJECT_CLASS;
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
    public ManagedCollection.Registration addObjectListener(Listener<? super TO, ? super ConvertingList<FROM, TO>> listener) {
        return list.addObjectListener(new ConvertingListener(listener));
    }

    @Override
    public TO get(String id) {
        FROM from = list.get(id);
        return from != null ? converter.apply(from) : null;
    }

    @Override
    public TO getByName(String name) {
        FROM from = list.getByName(name);
        return from != null ? converter.apply(from) : null;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(Listener<? super TO, ? super ConvertingList<FROM, TO>> listener, boolean callForExistingElements) {
        return list.addObjectListener(new ConvertingListener(listener), callForExistingElements);
    }

    @Override
    public Iterator<TO> iterator() {
        return new ConvertingIterator();
    }

    private class ConvertingListener implements Listener<FROM, List<FROM, ?>> {

        private final Listener<? super TO, ? super ConvertingList<FROM, TO>> listener;

        private ConvertingListener(Listener<? super TO, ? super ConvertingList<FROM, TO>> listener) {
            this.listener = listener;
        }

        @Override
        public void elementAdded(List<FROM, ?> list, FROM element) {
            listener.elementAdded(ConvertingList.this, converter.apply(element));
        }

        @Override
        public void elementRemoved(List<FROM, ?> list, FROM element) {
            listener.elementRemoved(ConvertingList.this, converter.apply(element));
        }
    }

    private class ConvertingIterator implements Iterator<TO> {

        private final Iterator<FROM> iter = list.iterator();

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public TO next() {
            return converter.apply(iter.next());
        }

        @Override
        public void remove() {
            iter.remove();
        }
    }

    public interface Converter<F, T> {
        T apply(F element);
    }
}
