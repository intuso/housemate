package com.intuso.housemate.comms.serialiser.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.comms.serialiser.api.Serialiser;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;
import com.intuso.housemate.comms.serialiser.json.config.GsonConfig;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:28
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerialiser implements Serialiser {

    public final static String TYPE = "application/json";
    public final static java.lang.reflect.Type MESSAGE_TYPE = new TypeToken<Message<Message.Payload>>() {}.getType();

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

    private final Gson gson;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    public JsonSerialiser(OutputStream outputStream, InputStream inputStream) throws IOException {
        try {
            gson = GsonConfig.createGson();
            jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch(Throwable t) {
            throw new HousemateRuntimeException("Failed to create json serialiser");
        }
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    public void write(Message<?> message) throws IOException {
        if(message != null)
            message.ensureSerialisable();
        gson.toJson(message, MESSAGE_TYPE, jsonWriter);
        jsonWriter.flush();
    }

    @Override
    public Message<?> read() throws InterruptedException, IOException {
        Message<?> message = gson.fromJson(jsonReader, MESSAGE_TYPE);
        if(message != null)
            message.ensureSerialisable();
        return message;
    }
}
