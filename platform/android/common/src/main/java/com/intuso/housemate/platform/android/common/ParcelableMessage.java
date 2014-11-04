package com.intuso.housemate.platform.android.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 22/10/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class ParcelableMessage implements Parcelable {

    private final Message<?> message;

    public ParcelableMessage(Message<?> message) {
        this.message = message;
    }

    private ParcelableMessage(Parcel parcel) {
        String[] path = new String[parcel.readInt()];
        parcel.readStringArray(path);
        String type = parcel.readString();
        Message.Payload payload = readPayload(parcel);
        List<String> route = Lists.newArrayList();
        parcel.readStringList(route);
        this.message = new Message<Message.Payload>(path, type, payload, route);
    }

    public Message<?> getMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(message.getPath().length);
        parcel.writeStringArray(message.getPath());
        parcel.writeString(message.getType());
        writePayload(parcel, flags, message.getPayload());
        parcel.writeStringList(message.getRoute());
    }

    private static void writePayload(Parcel parcel, int flags, Message.Payload payload) {
        if(payload instanceof HousemateData) {
            parcel.writeString("data");
            writeHousemateData(parcel, flags, (HousemateData<?>) payload);
        } else if(payload instanceof TypeInstance) {
            parcel.writeString("typeInstance");
            writeTypeInstance(parcel, flags, (TypeInstance) payload);
        } else if(payload instanceof TypeInstances) {
            parcel.writeString("typeInstances");
            writeTypeInstances(parcel, flags, (TypeInstances) payload);
        } else if(payload instanceof TypeInstanceMap) {
            parcel.writeString("typeInstanceMap");
            writeTypeInstanceMap(parcel, flags, (TypeInstanceMap) payload);
        } else if(payload instanceof HousemateObject.LoadRequest) {
            parcel.writeString("loadRequest");
            writeLoadRequest(parcel, flags, (HousemateObject.LoadRequest) payload);
        } else if(payload instanceof HousemateObject.LoadResponse) {
            parcel.writeString("loadResponse");
            writeLoadResponse(parcel, flags, (HousemateObject.LoadResponse) payload);
        } else if(payload instanceof HousemateObject.TreeLoadInfo) {
            parcel.writeString("treeLoadInfo");
            writeTreeLoadInfo(parcel, flags, (HousemateObject.TreeLoadInfo) payload);
        } else if(payload instanceof HousemateObject.TreeData) {
            parcel.writeString("treeData");
            writeTreeData(parcel, flags, (HousemateObject.TreeData) payload);
        } else if(payload instanceof Command.FailedPayload) {
            parcel.writeString("commandFailed");
            writeCommandFailed(parcel, flags, (Command.FailedPayload) payload);
        } else if(payload instanceof Command.PerformPayload) {
            parcel.writeString("commandPerform");
            writeCommandPerform(parcel, flags, (Command.PerformPayload) payload);
        } else if(payload instanceof Command.PerformingPayload) {
            parcel.writeString("commandPerforming");
            writeCommandPerforming(parcel, flags, (Command.PerformingPayload) payload);
        } else if(payload instanceof ApplicationRegistration) {
            parcel.writeString("applicationRegistration");
            writeApplicationRegistration(parcel, flags, (ApplicationRegistration) payload);
        } else if(payload instanceof StringPayload) {
            parcel.writeString("string");
            parcel.writeString(((StringPayload) payload).getValue());
        } else if(payload instanceof NoPayload) {
            parcel.writeString("none");
        } else if(payload instanceof ServerConnectionStatus) {
            parcel.writeString("serverConnectionStatus");
            writeServerConnectionStatus(parcel, flags, (ServerConnectionStatus) payload);
        } else if(payload instanceof ApplicationStatus) {
            parcel.writeString("applicationStatus");
            writeApplicationStatus(parcel, flags, (ApplicationStatus)payload);
        } else if(payload instanceof ApplicationInstanceStatus) {
            parcel.writeString("applicationInstanceStatus");
            writeApplicationInstanceStatus(parcel, flags, (ApplicationInstanceStatus)payload);
        } else {
            // TODO log unknown type
            parcel.writeString("unknown");
            return;
        }
    }

    private static void writeHousemateData(Parcel parcel, int flags, HousemateData<?> data) {
        // write the data type
        if(data instanceof AutomationData)
            parcel.writeString("automation");
        else if(data instanceof CommandData)
            parcel.writeString("command");
        else if(data instanceof ConditionData)
            parcel.writeString("condition");
        else if(data instanceof DeviceData)
            parcel.writeString("device");
        else if(data instanceof ListData)
            parcel.writeString("list");
        else if(data instanceof OptionData)
            parcel.writeString("option");
        else if(data instanceof ParameterData)
            parcel.writeString("parameter");
        else if(data instanceof PropertyData)
            parcel.writeString("property");
        else if(data instanceof RootData) {
            parcel.writeString("root");
            return; // nothing else to write
        } else if(data instanceof SubTypeData)
            parcel.writeString("subtype");
        else if(data instanceof TypeData) {
            if(data instanceof ChoiceTypeData)
                parcel.writeString("type-choice");
            else if(data instanceof CompoundTypeData)
                parcel.writeString("type-compound");
            else if(data instanceof ObjectTypeData)
                parcel.writeString("type-object");
            else if(data instanceof RegexTypeData)
                parcel.writeString("type-regex");
            else if(data instanceof SimpleTypeData) {
                parcel.writeString("type-simple");
                parcel.writeString(((SimpleTypeData)data).getType().name());
                return;
            }
        } else if(data instanceof UserData)
            parcel.writeString("user");
        else if(data instanceof ValueData)
            parcel.writeString("value");
        else {
            // TODO log unknown type
            return;
        }

        // write all common fields
        parcel.writeString(data.getId());
        parcel.writeString(data.getName());
        parcel.writeString(data.getDescription());

        // write extra fields
        if(data instanceof ParameterData)
            parcel.writeString(((ParameterData)data).getType());
        else if(data instanceof PropertyData) {
            PropertyData propertyData = (PropertyData)data;
            parcel.writeString(propertyData.getType());
            writeTypeInstances(parcel, flags, propertyData.getTypeInstances());
        } else if(data instanceof SubTypeData)
            parcel.writeString(((SubTypeData)data).getType());
        else if(data instanceof TypeData) {
            TypeData<?> typeData = (TypeData<?>)data;
            parcel.writeInt(typeData.getMinValues());
            parcel.writeInt(typeData.getMaxValues());
            if(data instanceof RegexTypeData)
                parcel.writeString(((RegexTypeData)typeData).getRegexPattern());
        } else if(data instanceof ValueData) {
            ValueData valueData = (ValueData)data;
            parcel.writeString(valueData.getType());
            writeTypeInstances(parcel, flags, valueData.getTypeInstances());
        } else if(data instanceof DeviceData) {
            DeviceData deviceData = (DeviceData)data;
            writeStrings(parcel, deviceData.getFeatureIds());
            writeStrings(parcel, deviceData.getCustomCommandIds());
            writeStrings(parcel, deviceData.getCustomValueIds());
            writeStrings(parcel, deviceData.getCustomPropertyIds());
        }

        // write the children
        parcel.writeInt(data.getChildData().size());
        for(HousemateData<?> child : data.getChildData().values())
            writeHousemateData(parcel, flags, child);
    }

    private static void writeStrings(Parcel parcel, Collection<String> strings) {
        parcel.writeInt(strings.size());
        for(String s : strings)
            parcel.writeString(s);
    }

    private static void writeTypeInstance(Parcel parcel, int flags, TypeInstance typeInstance) {
        if(typeInstance == null) {
            parcel.writeString(null);
            writeTypeInstanceMap(parcel, flags, null);
        } else {
            parcel.writeString(typeInstance.getValue());
            writeTypeInstanceMap(parcel, flags, typeInstance.getChildValues());
        }
    }

    private static void writeTypeInstances(Parcel parcel, int flags, TypeInstances typeInstances) {
        if(typeInstances == null)
            parcel.writeInt(0);
        else {
            parcel.writeInt(typeInstances.getElements().size());
            for(TypeInstance typeInstance : typeInstances.getElements())
                writeTypeInstance(parcel, flags, typeInstance);
        }
    }

    private static void writeTypeInstanceMap(Parcel parcel, int flags, TypeInstanceMap typeInstanceMap) {
        if(typeInstanceMap == null)
            parcel.writeInt(0);
        else {
            parcel.writeInt(typeInstanceMap.getChildren().size());
            for(Map.Entry<String, TypeInstances> entry : typeInstanceMap.getChildren().entrySet()) {
                parcel.writeString(entry.getKey());
                writeTypeInstances(parcel, flags, entry.getValue());
            }
        }
    }

    private static void writeApplicationRegistration(Parcel parcel, int flags, ApplicationRegistration applicationRegistration) {
        parcel.writeString(applicationRegistration.getType().name());
        parcel.writeString(applicationRegistration.getApplicationDetails().getApplicationId());
        parcel.writeString(applicationRegistration.getApplicationDetails().getApplicationName());
        parcel.writeString(applicationRegistration.getApplicationDetails().getApplicationDescription());
        parcel.writeString(applicationRegistration.getApplicationInstanceId());
    }

    private static void writeServerConnectionStatus(Parcel parcel, int flags, ServerConnectionStatus serverConnectionStatus) {
        parcel.writeString(serverConnectionStatus.name());
    }

    private static void writeApplicationStatus(Parcel parcel, int flags, ApplicationStatus applicationStatus) {
        parcel.writeString(applicationStatus.name());
    }

    private static void writeApplicationInstanceStatus(Parcel parcel, int flags, ApplicationInstanceStatus applicationInstanceStatus) {
        parcel.writeString(applicationInstanceStatus.name());
    }

    private static void writeLoadRequest(Parcel parcel, int flags, HousemateObject.LoadRequest loadRequest) {
        parcel.writeString(loadRequest.getLoaderName());
        writeTreeLoadInfo(parcel, flags, loadRequest.getLoadInfo());
    }

    private static void writeLoadResponse(Parcel parcel, int flags, HousemateObject.LoadResponse loadResponse) {
        parcel.writeString(loadResponse.getLoaderName());
        writeTreeData(parcel, flags, loadResponse.getTreeData());
        parcel.writeString(loadResponse.getError());
    }

    private static void writeTreeLoadInfo(Parcel parcel, int flags, HousemateObject.TreeLoadInfo treeLoadInfo) {
        parcel.writeString(treeLoadInfo.getId());
        parcel.writeString(Boolean.toString(treeLoadInfo.isLoad()));
        parcel.writeInt(treeLoadInfo.getChildren().size());
        for(Map.Entry<String, HousemateObject.TreeLoadInfo> entry : treeLoadInfo.getChildren().entrySet()) {
            parcel.writeString(entry.getKey());
            writeTreeLoadInfo(parcel, flags, entry.getValue());
        }
    }

    private static void writeTreeData(Parcel parcel, int flags, HousemateObject.TreeData treeData) {
        parcel.writeString(treeData.getId());
        writeHousemateData(parcel, flags, treeData.getData());
        parcel.writeInt(treeData.getChildData() != null ? treeData.getChildData().size() : 0);
        for(Map.Entry<String, ChildOverview> entry : treeData.getChildData().entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue().getId());
            parcel.writeString(entry.getValue().getName());
            parcel.writeString(entry.getValue().getDescription());
        }
        parcel.writeInt(treeData.getChildren().size());
        for(Map.Entry<String, HousemateObject.TreeData> entry : treeData.getChildren().entrySet()) {
            parcel.writeString(entry.getKey());
            writeTreeData(parcel, flags, entry.getValue());
        }
    }

    private static void writeCommandFailed(Parcel parcel, int flags, Command.FailedPayload failedPayload) {
        parcel.writeString(failedPayload.getOpId());
        parcel.writeString(failedPayload.getCause());
    }

    private static void writeCommandPerform(Parcel parcel, int flags, Command.PerformPayload performPayload) {
        parcel.writeString(performPayload.getOpId());
        writeTypeInstanceMap(parcel, flags, performPayload.getValues());
    }

    private static void writeCommandPerforming(Parcel parcel, int flags, Command.PerformingPayload performingPayload) {
        parcel.writeString(performingPayload.getOpId());
        parcel.writeString(Boolean.toString(performingPayload.isPerforming()));
    }

    private static Message.Payload readPayload(Parcel parcel) {
        String payloadType = parcel.readString();
        if(payloadType == null) {
            // TODO log no type
            return null;
        } else if(payloadType.equals("data"))
            return readHousemateData(parcel);
        else if(payloadType.equals("typeInstance"))
            return readTypeInstance(parcel);
        else if(payloadType.equals("typeInstances"))
            return readTypeInstances(parcel);
        else if(payloadType.equals("typeInstanceMap"))
            return readTypeInstanceMap(parcel);
        else if(payloadType.equals("loadRequest"))
            return readLoadRequest(parcel);
        else if(payloadType.equals("loadResponse"))
            return readLoadResponse(parcel);
        else if(payloadType.equals("treeLoadInfo"))
            return readTreeLoadInfo(parcel);
        else if(payloadType.equals("treeData"))
            return readTreeData(parcel);
        else if(payloadType.equals("commandFailed"))
            return readCommandFailed(parcel);
        else if(payloadType.equals("commandPerform"))
            return readCommandPerform(parcel);
        else if(payloadType.equals("commandPerforming"))
            return readCommandPerforming(parcel);
        else if(payloadType.equals("applicationRegistration"))
            return readAccessRequest(parcel);
        else if(payloadType.equals("string"))
            return new StringPayload(parcel.readString());
        else if(payloadType.equals("none"))
            return NoPayload.INSTANCE;
        else if(payloadType.equals("serverConnectionStatus"))
            return readServerConnectionStatus(parcel);
        else if(payloadType.equals("applicationStatus"))
            return readApplicationStatus(parcel);
        else if(payloadType.equals("applicationInstanceStatus"))
            return readApplicationInstanceStatus(parcel);
        else {
            // TODO log unknown type
            return null;
        }
    }

    private static HousemateData<?> readHousemateData(Parcel parcel) {

        // read the data type
        String dataType = parcel.readString();

        // create the data object
        HousemateData<?> result = null;
        if(dataType == null) {
            // TODO log the unknown type
            return null;
        } else if(dataType.equals("automation"))
            result = new AutomationData(parcel.readString(), parcel.readString(), parcel.readString());
        else if(dataType.equals("command"))
            result = new CommandData(parcel.readString(), parcel.readString(), parcel.readString());
        else if(dataType.equals("condition"))
            result = new ConditionData(parcel.readString(), parcel.readString(), parcel.readString());
        else if(dataType.equals("device"))
            result = new DeviceData(parcel.readString(), parcel.readString(), parcel.readString(), readStrings(parcel), readStrings(parcel), readStrings(parcel), readStrings(parcel));
        else if(dataType.equals("list"))
            result = new ListData<HousemateData<?>>(parcel.readString(), parcel.readString(), parcel.readString());
        else if(dataType.equals("option"))
            result = new OptionData(parcel.readString(), parcel.readString(), parcel.readString());
        else if(dataType.equals("parameter"))
            result = new ParameterData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
        else if(dataType.equals("property"))
            result = new PropertyData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), readTypeInstances(parcel));
        else if(dataType.equals("root"))
            result = new RootData();
        else if(dataType.equals("subtype"))
            result = new SubTypeData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
        else if(dataType.equals("type-choice"))
            result = new ChoiceTypeData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt());
        else if(dataType.equals("type-compound"))
            result = new CompoundTypeData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt());
        else if(dataType.equals("type-object"))
            result = new ObjectTypeData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt());
        else if(dataType.equals("type-regex"))
            result = new RegexTypeData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readString());
        else if(dataType.equals("type-simple"))
            result = new SimpleTypeData(SimpleTypeData.Type.valueOf(parcel.readString()));
        else if(dataType.equals("user"))
            result = new UserData(parcel.readString(), parcel.readString(), parcel.readString());
        else if(dataType.equals("value"))
            result = new ValueData(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), readTypeInstances(parcel));
        else {
            // TODO log unknown type
            return null;
        }

        // add all the child objects
        int numChildren = parcel.readInt();
        HousemateData<HousemateData<?>> typedResult = (HousemateData<HousemateData<?>>)result;
        for(int i = 0; i < numChildren; i++) {
            HousemateData<?> child = readHousemateData(parcel);
            typedResult.getChildData().put(child.getId(), child);
        }
        return result;
    }

    private static List<String> readStrings(Parcel parcel) {
        int size = parcel.readInt();
        List<String> result = Lists.newArrayListWithCapacity(size);
        for(int i = 0; i < size; i++)
            result.add(parcel.readString());
        return result;
    }

    private static TypeInstance readTypeInstance(Parcel parcel) {
        return new TypeInstance(parcel.readString(), readTypeInstanceMap(parcel));
    }

    private static TypeInstances readTypeInstances(Parcel parcel) {
        TypeInstances result = new TypeInstances();
        int num = parcel.readInt();
        for(int i = 0; i < num; i++)
            result.getElements().add(readTypeInstance(parcel));
        return result;
    }

    private static TypeInstanceMap readTypeInstanceMap(Parcel parcel) {
        TypeInstanceMap result = new TypeInstanceMap();
        int num = parcel.readInt();
        for(int i = 0; i < num; i++)
            result.getChildren().put(parcel.readString(), readTypeInstances(parcel));
        return result;
    }

    private static ApplicationRegistration readAccessRequest(Parcel parcel) {
        ClientType clientType = ClientType.valueOf(parcel.readString());
        return new ApplicationRegistration(
                new ApplicationDetails(parcel.readString(), parcel.readString(), parcel.readString()),
                parcel.readString(),
                clientType);
    }

    private static ServerConnectionStatus readServerConnectionStatus(Parcel parcel) {
        return ServerConnectionStatus.valueOf(parcel.readString());
    }

    private static ApplicationStatus readApplicationStatus(Parcel parcel) {
        return ApplicationStatus.valueOf(parcel.readString());
    }

    private static ApplicationInstanceStatus readApplicationInstanceStatus(Parcel parcel) {
        return ApplicationInstanceStatus.valueOf(parcel.readString());
    }

    private static HousemateObject.LoadRequest readLoadRequest(Parcel parcel) {
        return new HousemateObject.LoadRequest(parcel.readString(), readTreeLoadInfo(parcel));
    }

    private static HousemateObject.LoadResponse readLoadResponse(Parcel parcel) {
        return new HousemateObject.LoadResponse(parcel.readString(), readTreeData(parcel), parcel.readString());
    }

    private static HousemateObject.TreeLoadInfo readTreeLoadInfo(Parcel parcel) {
        HousemateObject.TreeLoadInfo result = new HousemateObject.TreeLoadInfo(parcel.readString(), Maps.<String, HousemateObject.TreeLoadInfo>newHashMap());
        result.setLoad(Boolean.parseBoolean(parcel.readString()));
        int num = parcel.readInt();
        for(int i = 0; i < num; i++)
            result.getChildren().put(parcel.readString(), readTreeLoadInfo(parcel));
        return result;
    }

    private static HousemateObject.TreeData readTreeData(Parcel parcel) {
        HousemateObject.TreeData result = new HousemateObject.TreeData(parcel.readString(),
                readHousemateData(parcel), Maps.<String, HousemateObject.TreeData>newHashMap(), Maps.<String, ChildOverview>newHashMap());
        int num = parcel.readInt();
        for(int i = 0; i < num; i++)
            result.getChildData().put(parcel.readString(), new ChildOverview(parcel.readString(), parcel.readString(), parcel.readString()));
        num = parcel.readInt();
        for(int i = 0; i < num; i++)
            result.getChildren().put(parcel.readString(), readTreeData(parcel));
        return result;
    }

    private static Command.FailedPayload readCommandFailed(Parcel parcel) {
        return new Command.FailedPayload(parcel.readString(), parcel.readString());
    }

    private static Command.PerformPayload readCommandPerform(Parcel parcel) {
        return new Command.PerformPayload(parcel.readString(), readTypeInstanceMap(parcel));
    }

    private static Command.PerformingPayload readCommandPerforming(Parcel parcel) {
        return new Command.PerformingPayload(parcel.readString(), Boolean.parseBoolean(parcel.readString()));
    }

    public final static Parcelable.Creator<ParcelableMessage> CREATOR = new Creator<ParcelableMessage>() {
        @Override
        public ParcelableMessage createFromParcel(Parcel parcel) {
            return new ParcelableMessage(parcel);
        }

        @Override
        public ParcelableMessage[] newArray(int i) {
            return new ParcelableMessage[i];
        }
    };
}
