package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.ChildData;
import com.intuso.utilities.listener.Listener;

public interface AvailableChildrenListener<OBJECT extends ProxyObject<?, ?, ?, ?, ?, ?, ?>> extends Listener {
    public void childAdded(OBJECT object, ChildData childData);
    public void childRemoved(OBJECT object, ChildData childData);
}

