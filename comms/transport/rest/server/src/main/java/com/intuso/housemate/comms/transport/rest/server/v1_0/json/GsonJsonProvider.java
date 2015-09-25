package com.intuso.housemate.comms.transport.rest.server.v1_0.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.intuso.housemate.comms.v1_0.serialiser.json.config.DataAdapter;
import com.intuso.housemate.comms.v1_0.serialiser.json.config.PayloadAdapter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by tomc on 04/06/14.
 */
@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class GsonJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

    private final Gson gson;

    public GsonJsonProvider() {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new DataAdapter())
                .registerTypeAdapterFactory(new PayloadAdapter())
                .create();
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Serializable.class.isAssignableFrom(aClass);
    }

    @Override
    public Object readFrom(Class<Object> objectClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> stringStringMultivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        return gson.fromJson(new InputStreamReader(inputStream, "UTF-8"), objectClass);
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Serializable.class.isAssignableFrom(aClass);
    }

    @Override
    public long getSize(Object o, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object o, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        gson.toJson(o, aClass, new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8")));
    }
}
