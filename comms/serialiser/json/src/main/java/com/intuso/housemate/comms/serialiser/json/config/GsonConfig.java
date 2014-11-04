package com.intuso.housemate.comms.serialiser.json.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.EnumTypeAdapter;
import com.google.gson.typeadapters.TypeOverrideAdapterFactory;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.utilities.object.Data;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by tomc on 28/10/14.
 */
public class GsonConfig {

    private final static TypeToken SCS_TYPE = new TypeToken<ServerConnectionStatus>() {};
    private final static TypeToken AS_TYPE = new TypeToken<ApplicationStatus>() {};
    private final static TypeToken AIS_TYPE = new TypeToken<ApplicationInstanceStatus>() {};

    public static Gson createGson() throws NoSuchFieldException {

        // there is a problem serialising ListData.class, because it is generic. Gson only finds the DATA parameter
        // of the Data class, which doesn't extend from HousemateData so doesn't get any type info in the json for
        // the child data, causing deserialisation problems
        Type untypedListType = $Gson$Types.resolve(new TypeToken<ListData>() {}.getType(), ListData.class, ListData.class.getGenericSuperclass());
        TypeToken untypedChildDataType = TypeToken.get($Gson$Types.resolve(untypedListType, ListData.class, Data.class.getDeclaredField("childData").getGenericType()));
        TypeToken typedChildDataType = new TypeToken<Map<String, HousemateData<HousemateData<?>>>>() {};

        return new GsonBuilder()
                // register main sub-class type adapters
                .registerTypeAdapterFactory(new DataAdapter())
                .registerTypeAdapterFactory(new PayloadAdapter())
                // generic type fixes
                .registerTypeAdapterFactory(new TypeOverrideAdapterFactory(untypedChildDataType, typedChildDataType))
                // add fixes for enums
                .registerTypeAdapter(SCS_TYPE.getType(), new EnumTypeAdapter<ServerConnectionStatus>(PayloadAdapter.TYPE_FIELD_NAME, "serverConnectionStatus", PayloadAdapter.ENUM_VALUE_FIELD_NAME))
                .registerTypeAdapter(AS_TYPE.getType(), new EnumTypeAdapter<ApplicationStatus>(PayloadAdapter.TYPE_FIELD_NAME, "applicationStatus", PayloadAdapter.ENUM_VALUE_FIELD_NAME))
                .registerTypeAdapter(AIS_TYPE.getType(), new EnumTypeAdapter<ApplicationInstanceStatus>(PayloadAdapter.TYPE_FIELD_NAME, "applicationInstanceStatus", PayloadAdapter.ENUM_VALUE_FIELD_NAME))
                .create();
    }
}
