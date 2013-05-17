package com.intuso.housemate.core.object;

import com.intuso.listeners.ListenerRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 16/01/13
 * Time: 16:12
 * To change this template use File | Settings | File Templates.
 */
public interface BaseObject<L extends ObjectListener> {
    public String getId();
    public String[] getPath();
    public ListenerRegistration<? super L> addObjectListener(L listener);
    public String getName();
    public String getDescription();
}
