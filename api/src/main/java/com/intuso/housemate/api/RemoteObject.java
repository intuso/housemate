package com.intuso.housemate.api;

import com.intuso.housemate.api.object.value.Value;

public interface RemoteObject<CONNECTED_VALUE extends Value<?, ?>> {

    /**
     * Gets the connected value object
     * @return the connected value
     */
    public CONNECTED_VALUE getConnectedValue();

    /**
     * Gets the connected value
     * @return the connected value
     */
    public boolean isConnected();
}
