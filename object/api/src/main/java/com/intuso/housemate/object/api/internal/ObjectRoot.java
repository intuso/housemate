package com.intuso.housemate.object.api.internal;

import com.intuso.utilities.listener.ListenerRegistration;

/**
 * @param <ROOT> the type of the root
 */
public interface ObjectRoot<LISTENER extends Root.Listener<? super ROOT>,
        ROOT extends ObjectRoot<?, ?>> extends Root<LISTENER, ROOT> {
    BaseHousemateObject<?> findObject(String[] path);
    ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener);
}
