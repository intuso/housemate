package com.intuso.housemate.core.object.value;

import com.intuso.housemate.core.object.ObjectListener;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 28/05/12
 * Time: 00:18
 * To change this template use File | Settings | File Templates.
 */
public interface ValueListener<V extends Value<?, ?>> extends ObjectListener {
    public void valueChanged(V value);
}
