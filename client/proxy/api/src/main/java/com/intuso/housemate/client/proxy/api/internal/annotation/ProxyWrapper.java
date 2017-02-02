package com.intuso.housemate.client.proxy.api.internal.annotation;

import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;

/**
 * Created by tomc on 16/12/16.
 */
public interface ProxyWrapper {
    <T> T build(ProxyObject<?, ?> object, Class<T> tClass, String prefix, long commandTimeout);
}
