package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.HousemateException;

import java.io.*;

/**
 * Created by tomc on 09/05/16.
 */
public class Serialiser {

    private Serialiser() {}

    public static byte[] serialise(Serializable serializable) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            new ObjectOutputStream(bytes).writeObject(serializable);
            return bytes.toByteArray();
        } catch(IOException e) {
            throw new HousemateException("Failed to serialise object", e);
        }
    }

    public static <T> T deserialise(byte[] bytes) {
        try {
            return (T) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch(IOException e) {
            throw new HousemateException("Failed to deserialise object", e);
        } catch(ClassNotFoundException e) {
            throw new HousemateException("Serialised object is not a known type", e);
        }
    }
}
