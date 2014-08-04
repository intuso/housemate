package com.intuso.housemate.comms.serialiser.json.config;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;

/**
 * Created by tomc on 04/06/14.
 */
public class HousemateDataAdapter extends RuntimeTypeAdapterFactory<HousemateData> {

    public HousemateDataAdapter() {
        super(HousemateData.class, "class");
        registerSubtype(NoChildrenData.class, "noChildren");
        registerSubtype(ApplicationData.class, "application");
        registerSubtype(ApplicationInstanceData.class, "applicationInstance");
        registerSubtype(CommandData.class, "command");
        registerSubtype(ConditionData.class, "condition");
        registerSubtype(DeviceData.class, "device");
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
        registerSubtype(RegexTypeData.class, "regexType");
        registerSubtype(SimpleTypeData.class, "simpleType");
        registerSubtype(UserData.class, "user");
        registerSubtype(ValueData.class, "value");
    }
}
