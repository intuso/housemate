package com.intuso.housemate.core.object.list;

import com.intuso.housemate.core.object.BaseObject;
import com.intuso.listeners.ListenerRegistration;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public interface List<T> extends BaseObject<ListListener<? super T>>, Iterable<T> {

    public final static String ADD = "add";
    public final static String REMOVE = "remove";

    public T get(String name);
    public int size();

    public ListenerRegistration<? super ListListener<? super T>> addObjectListener(ListListener<? super T> listener, boolean callForExistingElements);
}
