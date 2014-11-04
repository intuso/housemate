package com.google.gson.typeadapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by tomc on 24/10/14.
 */
public class EnumTypeAdapter<ENUM extends Enum<ENUM>> extends TypeAdapter<ENUM> {

    private final String typeFieldName;
    private final String type;
    private final String valueFieldName;

    public EnumTypeAdapter(String typeFieldName, String type, String valueFieldName) {
        this.typeFieldName = typeFieldName;
        this.type = type;
        this.valueFieldName = valueFieldName;
    }

    @Override
    public void write(JsonWriter jsonWriter, ENUM anEnum) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name(typeFieldName).value(type);
        jsonWriter.name(valueFieldName).value(anEnum.name());
        jsonWriter.endObject();
    }

    @Override
    public ENUM read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
