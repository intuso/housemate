package com.intuso.housemate.comms.serialiser.json.config;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.realclient.RealClientData;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;

/**
 * Created by tomc on 04/06/14.
 */
public class PayloadAdapter extends RuntimeTypeAdapterFactory<Message.Payload> {

    public final static String TYPE_FIELD_NAME = "_type";
    public static final String ENUM_VALUE_FIELD_NAME = "value";

    public PayloadAdapter() {
        super(Message.Payload.class, TYPE_FIELD_NAME, ENUM_VALUE_FIELD_NAME);
        registerSubtype(ApplicationRegistration.class, "applicationRegistration");
        registerSubtype(NoPayload.class, "none");
        registerSubtype(StringPayload.class, "string");
        registerSubtype(ChildOverview.class, "childOverview");
        registerSubtype(HousemateObject.ChildOverviews.class, "childOverviews");
        registerSubtype(HousemateObject.TreeLoadInfo.class, "treeLoadInfo");
        registerSubtype(HousemateObject.TreeData.class, "tree");
        registerSubtype(HousemateObject.LoadRequest.class, "loadRequest");
        registerSubtype(HousemateObject.LoadResponse.class, "loadResponse");
        registerSubtype(NoChildrenData.class, "noChildren");
        registerSubtype(ApplicationData.class, "application");
        registerSubtype(ApplicationInstanceData.class, "applicationInstance");
        registerSubtype(AutomationData.class, "automation");
        registerSubtype(CommandData.class, "command");
        registerSubtype(Command.PerformPayload.class, "commandPerform");
        registerSubtype(Command.PerformingPayload.class, "commandPerforming");
        registerSubtype(Command.FailedPayload.class, "commandFailed");
        registerSubtype(ConditionData.class, "condition");
        registerSubtype(DeviceData.class, "device");
        registerSubtype(HardwareData.class, "hardware");
        registerSubtype(ListData.class, "list");
        registerSubtype(OptionData.class, "option");
        registerSubtype(ParameterData.class, "parameter");
        registerSubtype(PropertyData.class, "property");
        registerSubtype(RootData.class, "root");
        registerSubtype(SubTypeData.class, "subType");
        registerSubtype(TaskData.class, "task");
        registerSubtype(ChoiceTypeData.class, "choiceType");
        registerSubtype(CompoundTypeData.class, "compoundType");
        registerSubtype(ObjectTypeData.class, "objectType");
        registerSubtype(RealClientData.class, "realClient");
        registerSubtype(RegexTypeData.class, "regexType");
        registerSubtype(SimpleTypeData.class, "simpleType");
        registerSubtype(TypeInstance.class, "typeInstance");
        registerSubtype(TypeInstanceMap.class, "typeInstanceMap");
        registerSubtype(TypeInstances.class, "typeInstances");
        registerSubtype(UserData.class, "user");
        registerSubtype(ValueData.class, "value");
        registerSubtype(ServerConnectionStatus.class, "serverConnectionStatus");
        registerSubtype(ApplicationStatus.class, "applicationStatus");
        registerSubtype(ApplicationInstanceStatus.class, "applicationInstanceStatus");
    }
}
