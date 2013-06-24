package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for values
 */
public interface ValueListener<V extends Value<?, ?>> extends ObjectListener {

    /**
     * Notifies that the value of this value object is about to be changed
     * @param value the value object whose value is about to be changed
     */
    public void valueChanging(V value);

    /**
     * Notifies that the value of this value object has been changed
     * @param value the value object whose value has just been changed
     */
    public void valueChanged(V value);
}
