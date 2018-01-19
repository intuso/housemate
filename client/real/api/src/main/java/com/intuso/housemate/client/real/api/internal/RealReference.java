package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Reference;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.proxy.internal.object.ProxyObject;

public interface RealReference<OBJECT_VIEW extends View,
        OBJECT extends ProxyObject<?, ?, OBJECT_VIEW>,
        REFERENCE extends RealReference<OBJECT_VIEW, OBJECT, REFERENCE>>
        extends Reference<OBJECT_VIEW,
        OBJECT,
        REFERENCE> {}
