package com.intuso.housemate.comms.serialiser.javabin;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.comms.serialiser.api.Serialiser;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:21
 * To change this template use File | Settings | File Templates.
 */
public class JavabinSerialiser implements Serialiser {

    public final static String TYPE = "application/x-java-serialized-object";

    public static class Factory implements StreamSerialiserFactory {

        @Override
        public String getType() {
            return TYPE;
        }

        @Override
        public JavabinSerialiser create(OutputStream outputStream, InputStream inputStream) throws IOException {
            return new JavabinSerialiser(outputStream, inputStream);
        }
    }

    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public JavabinSerialiser(OutputStream outputStream, InputStream inputStream) throws IOException {
        this.outputStream = new ObjectOutputStream(outputStream);
        this.inputStream = new ObjectInputStream(inputStream);
    }

    @Override
    public void write(Message<?> message) throws IOException {
        outputStream.writeObject(message);
        outputStream.reset();
    }

    @Override
    public Message<?> read() throws IOException, HousemateException {
        try {
            Object read = inputStream.readObject();
            if(read instanceof Message)
                return (Message<?>) read;
            else
                throw new HousemateException("Read non-message object of type " + read.getClass().getName());
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to read object from stream", e);
        }
    }
}
