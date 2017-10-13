package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by tomc on 02/05/17.
 */
public class CombinationList<T extends Object<?, ?, ?>>
        implements List<T, CombinationList<T>>, List.Listener<T, List<T, ?>> {

    private final List.Data data;
    private final ManagedCollection<List.Listener<? super T, ? super CombinationList<T>>> listeners;
    private final java.util.List<List<? extends T, ?>> lists = new ArrayList<>();

    public CombinationList(List.Data data, ManagedCollectionFactory managedCollectionFactory) {
        this.data = data;
        this.listeners = managedCollectionFactory.create();
    }

    public void addList(List<? extends T, ?> list) {
        lists.add(list);
        ((List<T, ?>)list).addObjectListener(this, true);
        for(Listener<? super T, ? super CombinationList<T>> listener : listeners)
            for(T element : list)
                listener.elementAdded(this, element);
    }

    @Override
    public Data getData() {
        return data;
    }

    @Override
    public String getObjectClass() {
        return Data.OBJECT_CLASS;
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public String getName() {
        return data.getName();
    }

    @Override
    public String getDescription() {
        return data.getDescription();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(Listener<? super T, ? super CombinationList<T>> listener) {
        return listeners.add(listener);
    }

    @Override
    public ManagedCollection.Registration addObjectListener(Listener<? super T, ? super CombinationList<T>> listener, boolean callForExistingElements) {
        ManagedCollection.Registration result = listeners.add(listener);
        if(callForExistingElements)
            for(List<? extends T, ?> list : lists)
                for(T element : list)
                    listener.elementAdded(this, element);
        return result;
    }

    @Override
    public T get(String id) {
        for(List<? extends T, ?> list : lists) {
            T element = list.get(id);
            if(element != null)
                return element;
        }
        return null;
    }

    @Override
    public T getByName(String name) {
        for(List<? extends T, ?> list : lists) {
            T element = list.getByName(name);
            if(element != null)
                return element;
        }
        return null;
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
        return get(id);
    }

    @Override
    public ListView<?> createView(View.Mode mode) {
        return new ListView<>(mode);
    }

    @Override
    public Tree getTree(ListView<?> view) {
        Tree result = new Tree(getData());
        switch (view.getMode()) {
            case ANCESTORS:
                for(T t : this)
                    result.getChildren().put(t.getId(), ((Object) t).getTree(t.createView(View.Mode.ANCESTORS)));
                break;
            case CHILDREN:
                for(T t : this)
                    result.getChildren().put(t.getId(), ((Object) t).getTree(t.createView(View.Mode.SELECTION)));
                break;
            case SELECTION:
                if (view.getElements() != null) {
                    for (String id : view.getElements()) {
                        T t = get(id);
                        if (t != null)
                            result.getChildren().put(id, ((Object) t).getTree(view.getElementView()));
                    }
                }
                break;
        }
        return result;
    }

    @Override
    public int size() {
        int totalSize = 0;
        for(List<? extends T, ?> list : lists)
            totalSize += list.size();
        return totalSize;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    @Override
    public void elementAdded(List<T, ?> list, T element) {
        for(List.Listener<? super T, ? super CombinationList<T>> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    public void elementRemoved(List<T, ?> list, T element) {
        for(List.Listener<? super T, ? super CombinationList<T>> listener : listeners)
            listener.elementRemoved(this, element);
    }

    public class IteratorImpl implements Iterator<T> {

        private final Iterator<List<? extends T, ?>> listsIter = lists.iterator();
        private Iterator<? extends T> current = listsIter.hasNext() ? listsIter.next().iterator() : null;

        @Override
        public boolean hasNext() {
            if(current != null && !current.hasNext())
                current = listsIter.hasNext() ? listsIter.next().iterator() : null;
            return current != null && current.hasNext();
        }

        @Override
        public T next() {
            if(current == null)
                throw new NoSuchElementException();
            return current.next();
        }

        @Override
        public void remove() {
            if(current == null)
                throw new NoSuchElementException();
            current.remove();
        }
    }
}
