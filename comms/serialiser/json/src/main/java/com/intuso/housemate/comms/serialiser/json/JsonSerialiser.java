package com.intuso.housemate.comms.serialiser.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.comms.serialiser.api.Serialiser;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;
import com.intuso.housemate.comms.serialiser.json.config.HousemateDataAdapter;
import com.intuso.housemate.comms.serialiser.json.config.PayloadDataAdapter;

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

    private final java.lang.reflect.Type messageType = new TypeToken<Message<Message.Payload>>() {}.getType();

    private final Gson gson;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    public JsonSerialiser(OutputStream outputStream, InputStream inputStream) throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new HousemateDataAdapter())
                .registerTypeAdapterFactory(new PayloadDataAdapter())
                .create();
        jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
    }

    @Override
    public void write(Message<?> message) throws IOException {
        gson.toJson(message, messageType, jsonWriter);
        jsonWriter.flush();
    }

    @Override
    public Message<?> read() throws InterruptedException, IOException {
        Message<?> message = gson.fromJson(jsonReader, messageType);
        if(message != null)
            message.ensureSerialisable();
        return message;
    }
}
