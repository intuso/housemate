package com.intuso.housemate.api.object;

import com.intuso.utilities.listener.ListenerRegistration;

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
    public ListenerRegistration addObjectListener(L listener);
    public String getName();
    public String getDescription();
}
