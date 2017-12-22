package com.intuso.housemate.client.serialisation.javabin.internal;

import com.intuso.housemate.client.serialisation.api.internal.SerialisationException;
import com.intuso.housemate.client.serialisation.api.internal.Serialiser;

import java.io.*;

/**
 * Created by tomc on 09/05/16.
 */
public class JavabinSerialiser implements Serialiser<byte[]> {

    private static final String TYPE = "application/json";

    @Override
    public String getType() {
        return TYPE;
    }

    public byte[] serialise(Serializable serializable) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            new ObjectOutputStream(bytes).writeObject(serializable);
            return bytes.toByteArray();
        } catch(IOException e) {
            throw new SerialisationException("Failed to serialise object", e);
        }
    }

    public <T extends Serializable> T deserialise(byte[] bytes, Class<T> tClass) {
        try {
            Object object = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
            if(object != null && !tClass.isAssignableFrom(object.getClass()))
                throw new SerialisationException("Deserialised object is not a " + tClass.getName() + " but a " + object.getClass().getName());
            return (T) object;
        } catch(IOException e) {
            throw new SerialisationException("Failed to deserialise object", e);
        } catch(ClassNotFoundException e) {
            throw new SerialisationException("Serialised object is not a known type", e);
        }
    }
}
