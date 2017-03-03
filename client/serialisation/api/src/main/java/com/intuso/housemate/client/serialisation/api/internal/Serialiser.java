package com.intuso.housemate.client.serialisation.api.internal;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:17
 * To change this template use File | Settings | File Templates.
 */
public interface Serialiser<SERIALISED> {
    String getType();
    SERIALISED serialise(Serializable object);
    <T extends Serializable> T deserialise(SERIALISED serialised, Class<T> tClass);
}
