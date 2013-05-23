package com.intuso.housemate.api.object.type;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public interface TypeSerialiser<O> {
    public String serialise(O o);
    public O deserialise(String value);
}
