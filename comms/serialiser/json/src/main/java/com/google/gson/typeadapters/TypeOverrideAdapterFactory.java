package com.google.gson.typeadapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Created by tomc on 27/10/14.
 */
public class TypeOverrideAdapterFactory implements TypeAdapterFactory {

    private final TypeToken<?> givenType;
    private final TypeToken<?> actualType;

    public TypeOverrideAdapterFactory(TypeToken<?> givenType, TypeToken<?> actualType) {
        this.givenType = givenType;
        this.actualType = actualType;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if(type.equals(givenType))
            return (TypeAdapter<T>) gson.getAdapter(actualType);
        return null;
    }
}
