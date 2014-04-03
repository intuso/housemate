package com.intuso.housemate.comms.serialiser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.comms.serialiser.api.Serialiser;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:28
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerialiser implements Serialiser {

    public final static String TYPE = "json";

    public static class Factory implements StreamSerialiserFactory {

        @Override
        public String getType() {
            return TYPE;
        }

        @Override
        public JsonSerialiser create(OutputStream outputStream, InputStream inputStream) throws IOException {
            return new JsonSerialiser(outputStream, inputStream);
        }
    }

    private final OutputStream outputStream;
    private final InputStream inputStream;
    private final ObjectMapper objectMapper;

    public JsonSerialiser(OutputStream outputStream, InputStream inputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JacksonModule());
    }

    @Override
    public void write(Message<?> message) throws IOException {
        objectMapper.writeValue(outputStream, message);
    }

    @Override
    public Message<?> read() throws InterruptedException, IOException {
        return objectMapper.readValue(inputStream, Message.class);
    }
}
