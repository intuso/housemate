package com.intuso.housemate.comms.serialiser.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.comms.serialiser.api.Serialiser;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;

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

        RuntimeTypeAdapterFactory<HousemateData> dataAdapter = RuntimeTypeAdapterFactory
                .of(HousemateData.class, "class")
                .registerSubtype(NoChildrenData.class, "noChildren")
                .registerSubtype(ApplicationData.class, "application")
                .registerSubtype(ApplicationInstanceData.class, "applicationInstance")
                .registerSubtype(CommandData.class, "command")
                .registerSubtype(ConditionData.class, "condition")
                .registerSubtype(DeviceData.class, "device")
                .registerSubtype(ListData.class, "list")
                .registerSubtype(OptionData.class, "option")
                .registerSubtype(ParameterData.class, "parameter")
                .registerSubtype(PropertyData.class, "property")
                .registerSubtype(RootData.class, "root")
                .registerSubtype(SubTypeData.class, "subType")
                .registerSubtype(TaskData.class, "task")
                .registerSubtype(ChoiceTypeData.class, "choiceType")
                .registerSubtype(CompoundTypeData.class, "compoundType")
                .registerSubtype(ObjectTypeData.class, "objectType")
                .registerSubtype(RegexTypeData.class, "regexType")
                .registerSubtype(SimpleTypeData.class, "simpleType")
                .registerSubtype(UserData.class, "user")
                .registerSubtype(ValueData.class, "value");

        RuntimeTypeAdapterFactory<Message.Payload> payloadAdapter = RuntimeTypeAdapterFactory
                        .of(Message.Payload.class, "class")
                        .registerSubtype(ApplicationRegistration.class, "applicationRegistration")
                        .registerSubtype(NoPayload.class, "none")
                        .registerSubtype(StringPayload.class, "string")
                        .registerSubtype(ChildOverview.class, "childOverview")
                        .registerSubtype(HousemateObject.ChildOverviews.class, "childOverviews")
                        .registerSubtype(HousemateObject.TreeLoadInfo.class, "treeLoadInfo")
                        .registerSubtype(HousemateObject.TreeData.class, "tree")
                        .registerSubtype(HousemateObject.LoadRequest.class, "loadRequest")
                        .registerSubtype(HousemateObject.LoadResponse.class, "loadResponse")
                        .registerSubtype(NoChildrenData.class, "noChildren")
                        .registerSubtype(ApplicationData.class, "application")
                        .registerSubtype(ApplicationInstanceData.class, "applicationInstance")
                        .registerSubtype(CommandData.class, "command")
                        .registerSubtype(Command.PerformPayload.class, "commandPerform")
                        .registerSubtype(Command.PerformingPayload.class, "commandPerforming")
                        .registerSubtype(Command.FailedPayload.class, "commandFailed")
                        .registerSubtype(ConditionData.class, "condition")
                        .registerSubtype(DeviceData.class, "device")
                        .registerSubtype(ListData.class, "list")
                        .registerSubtype(OptionData.class, "option")
                        .registerSubtype(ParameterData.class, "parameter")
                        .registerSubtype(PropertyData.class, "property")
                        .registerSubtype(RootData.class, "root")
                        .registerSubtype(Root.ConnectionStatus.class, "connectionStatus")
                        .registerSubtype(SubTypeData.class, "subType")
                        .registerSubtype(TaskData.class, "task")
                        .registerSubtype(ChoiceTypeData.class, "choiceType")
                        .registerSubtype(CompoundTypeData.class, "compoundType")
                        .registerSubtype(ObjectTypeData.class, "objectType")
                        .registerSubtype(RegexTypeData.class, "regexType")
                        .registerSubtype(SimpleTypeData.class, "simpleType")
                        .registerSubtype(TypeInstance.class, "typeInstance")
                        .registerSubtype(TypeInstanceMap.class, "typeInstanceMap")
                        .registerSubtype(TypeInstances.class, "typeInstances")
                        .registerSubtype(UserData.class, "user")
                        .registerSubtype(ValueData.class, "value");

        TypeAdapterFactory typeInstancesAdapter = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

                if(!(type.getRawType().equals(TypeInstances.class)))
                    return null;

                return (TypeAdapter<T>) gson.getAdapter(Message.Payload.class);
            }
        };

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(dataAdapter)
                .registerTypeAdapterFactory(payloadAdapter)
//                .registerTypeAdapterFactory(typeInstancesAdapter)
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
