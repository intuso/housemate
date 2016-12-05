package com.intuso.housemate.client.proxy.api.internal;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.object.Value;

/**
 * Created by tomc on 16/09/15.
 */
public interface ProxyFailable<ERROR_VALUE extends Value<?, ?, ?>> extends Failable<ERROR_VALUE> {
    String getError();
}
