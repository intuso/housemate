package com.intuso.housemate.client.proxy.internal.annotation;

import com.intuso.housemate.client.proxy.internal.object.ProxyObject;

/**
 * Created by tomc on 16/12/16.
 */
public interface ProxyWrapper {
    <T> T build(ProxyObject<?, ?> object, Class<T> tClass, String prefix, long commandTimeout);
}
