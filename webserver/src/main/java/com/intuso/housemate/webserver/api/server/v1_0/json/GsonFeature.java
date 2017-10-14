package com.intuso.housemate.webserver.api.server.v1_0.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.intuso.housemate.client.v1_0.serialisation.json.config.GsonConfig;

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

@Provider
@Consumes("application/json")
@Produces("application/json")
public class GsonFeature implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

    private final Gson gson;

    public GsonFeature() {
        this.gson = GsonConfig.createGson();
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> clazz, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        return gson.fromJson(new InputStreamReader(inputStream), clazz);
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object o, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1L;//gson.toJson(o, clazz).getBytes().length;
    }

    @Override
    public void writeTo(Object o, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(new BufferedOutputStream(outputStream)));
        gson.toJson(o, clazz, writer);
        writer.flush();
    }
}
