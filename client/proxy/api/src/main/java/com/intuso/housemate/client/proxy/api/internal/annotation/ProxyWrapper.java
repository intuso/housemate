package com.intuso.housemate.client.proxy.api.internal.annotation;

import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import org.slf4j.Logger;

/**
 * Created by tomc on 16/12/16.
 */
public interface ProxyWrapper {
    <T> T build(Logger logger, ProxyObject<?, ?> object, Class<T> tClass, String prefix, long commandTimeout);
}
