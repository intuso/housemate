package com.intuso.housemate.comms.serialiser.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:32
 * To change this template use File | Settings | File Templates.
 */
public interface StreamSerialiserFactory {
    public String getType();
    public Serialiser create(OutputStream outputStream, InputStream inputStream) throws IOException;
}
