package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 28/05/12
 * Time: 00:18
 * To change this template use File | Settings | File Templates.
 */
public interface ValueListener<V extends Value<?, ?>> extends ObjectListener {
    public void valueChanging(V value);
    public void valueChanged(V value);
}
