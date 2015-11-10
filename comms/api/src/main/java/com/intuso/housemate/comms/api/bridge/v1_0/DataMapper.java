package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.inject.Inject;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstancesMapper;

/**
 * Created by tomc on 28/09/15.
 */
public class DataMapper {

    private final TypeInstancesMapper typeInstancesMapper;

    @Inject
    public DataMapper(TypeInstancesMapper typeInstancesMapper) {
        this.typeInstancesMapper = typeInstancesMapper;
    }

    public HousemateData<?> map(com.intuso.housemate.comms.v1_0.api.payload.HousemateData data) {
        if(data == null)
            return null;
        else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData) {
            // should never get an instance of this
            return null;
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ApplicationData) {
            com.intuso.housemate.comms.v1_0.api.payload.ApplicationData applicationData = (com.intuso.housemate.comms.v1_0.api.payload.ApplicationData) data;
            return mapChildren(data, new ApplicationData(applicationData.getId(), applicationData.getName(), applicationData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData) {
            com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData applicationInstanceData = (com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData) data;
            return mapChildren(data, new ApplicationInstanceData(applicationInstanceData.getId(), applicationInstanceData.getName(), applicationInstanceData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.AutomationData) {
            com.intuso.housemate.comms.v1_0.api.payload.AutomationData automationData = (com.intuso.housemate.comms.v1_0.api.payload.AutomationData) data;
            return mapChildren(data, new AutomationData(automationData.getId(), automationData.getName(), automationData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.CommandData) {
            com.intuso.housemate.comms.v1_0.api.payload.CommandData commandData = (com.intuso.housemate.comms.v1_0.api.payload.CommandData) data;
            return mapChildren(data, new CommandData(commandData.getId(), commandData.getName(), commandData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ConditionData) {
            com.intuso.housemate.comms.v1_0.api.payload.ConditionData conditionData = (com.intuso.housemate.comms.v1_0.api.payload.ConditionData) data;
            return mapChildren(data, new ConditionData(conditionData.getId(), conditionData.getName(), conditionData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.DeviceData)
            return map((com.intuso.housemate.comms.v1_0.api.payload.DeviceData) data);
        else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.FeatureData) {
            com.intuso.housemate.comms.v1_0.api.payload.FeatureData featureData = (com.intuso.housemate.comms.v1_0.api.payload.FeatureData) data;
            return mapChildren(data, new FeatureData(featureData.getId(), featureData.getName(), featureData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.HardwareData) {
            com.intuso.housemate.comms.v1_0.api.payload.HardwareData hardwareData = (com.intuso.housemate.comms.v1_0.api.payload.HardwareData) data;
            return mapChildren(data, new HardwareData(hardwareData.getId(), hardwareData.getName(), hardwareData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ListData) {
            com.intuso.housemate.comms.v1_0.api.payload.ListData listData = (com.intuso.housemate.comms.v1_0.api.payload.ListData) data;
            return mapChildren(data, new ListData(listData.getId(), listData.getName(), listData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.OptionData) {
            com.intuso.housemate.comms.v1_0.api.payload.OptionData optionData = (com.intuso.housemate.comms.v1_0.api.payload.OptionData) data;
            return mapChildren(data, new OptionData(optionData.getId(), optionData.getName(), optionData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ParameterData) {
            com.intuso.housemate.comms.v1_0.api.payload.ParameterData parameterData = (com.intuso.housemate.comms.v1_0.api.payload.ParameterData) data;
            return mapChildren(data, new ParameterData(parameterData.getId(), parameterData.getName(), parameterData.getDescription(), parameterData.getType()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.PropertyData) {
            com.intuso.housemate.comms.v1_0.api.payload.PropertyData propertyData = (com.intuso.housemate.comms.v1_0.api.payload.PropertyData) data;
            return mapChildren(data, new PropertyData(propertyData.getId(), propertyData.getName(), propertyData.getDescription(), propertyData.getType(), typeInstancesMapper.map(propertyData.getTypeInstances())));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ServerData) {
            com.intuso.housemate.comms.v1_0.api.payload.ServerData serverData = (com.intuso.housemate.comms.v1_0.api.payload.ServerData) data;
            return mapChildren(data, new ServerData(serverData.getId(), serverData.getName(), serverData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.RootData)
            return mapChildren(data, new RootData());
        else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.SubTypeData) {
            com.intuso.housemate.comms.v1_0.api.payload.SubTypeData subTypeData = (com.intuso.housemate.comms.v1_0.api.payload.SubTypeData) data;
            return mapChildren(data, new SubTypeData(subTypeData.getId(), subTypeData.getName(), subTypeData.getDescription(), subTypeData.getType()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.TaskData) {
            com.intuso.housemate.comms.v1_0.api.payload.TaskData taskData = (com.intuso.housemate.comms.v1_0.api.payload.TaskData) data;
            return mapChildren(data, new TaskData(taskData.getId(), taskData.getName(), taskData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ChoiceTypeData) {
            com.intuso.housemate.comms.v1_0.api.payload.ChoiceTypeData choiceTypeData = (com.intuso.housemate.comms.v1_0.api.payload.ChoiceTypeData) data;
            return mapChildren(data, new ChoiceTypeData(choiceTypeData.getId(), choiceTypeData.getName(), choiceTypeData.getDescription(), choiceTypeData.getMinValues(), choiceTypeData.getMaxValues()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.CompoundTypeData) {
            com.intuso.housemate.comms.v1_0.api.payload.CompoundTypeData compoundTypeData = (com.intuso.housemate.comms.v1_0.api.payload.CompoundTypeData) data;
            return mapChildren(data, new CompoundTypeData(compoundTypeData.getId(), compoundTypeData.getName(), compoundTypeData.getDescription(), compoundTypeData.getMinValues(), compoundTypeData.getMaxValues()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ObjectTypeData) {
            com.intuso.housemate.comms.v1_0.api.payload.ObjectTypeData objectTypeData = (com.intuso.housemate.comms.v1_0.api.payload.ObjectTypeData) data;
            return mapChildren(data, new ObjectTypeData(objectTypeData.getId(), objectTypeData.getName(), objectTypeData.getDescription(), objectTypeData.getMinValues(), objectTypeData.getMaxValues()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.RegexTypeData) {
            com.intuso.housemate.comms.v1_0.api.payload.RegexTypeData regexTypeData = (com.intuso.housemate.comms.v1_0.api.payload.RegexTypeData) data;
            return mapChildren(data, new RegexTypeData(regexTypeData.getId(), regexTypeData.getName(), regexTypeData.getDescription(), regexTypeData.getMinValues(), regexTypeData.getMaxValues(), regexTypeData.getRegexPattern()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.SimpleTypeData) {
            com.intuso.housemate.comms.v1_0.api.payload.SimpleTypeData simpleTypeData = (com.intuso.housemate.comms.v1_0.api.payload.SimpleTypeData) data;
            return mapChildren(data, new SimpleTypeData(SimpleTypeData.Type.valueOf(simpleTypeData.getType().getName())));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.UserData) {
            com.intuso.housemate.comms.v1_0.api.payload.UserData userData = (com.intuso.housemate.comms.v1_0.api.payload.UserData) data;
            return mapChildren(data, new UserData(userData.getId(), userData.getName(), userData.getDescription()));
        } else if (data instanceof com.intuso.housemate.comms.v1_0.api.payload.ValueData) {
            com.intuso.housemate.comms.v1_0.api.payload.ValueData valueData = (com.intuso.housemate.comms.v1_0.api.payload.ValueData) data;
            return mapChildren(data, new ValueData(valueData.getId(), valueData.getName(), valueData.getDescription(), valueData.getType(), typeInstancesMapper.map(valueData.getTypeInstances())));
        }
        throw new HousemateCommsException("Unknown HousemateData sub type " + data.getClass().getName());
    }

    public DeviceData map(com.intuso.housemate.comms.v1_0.api.payload.DeviceData deviceData) {
        return mapChildren(deviceData, new DeviceData(deviceData.getId(), deviceData.getName(), deviceData.getDescription()));
    }

    private <DATA extends HousemateData> DATA mapChildren(com.intuso.housemate.comms.v1_0.api.payload.HousemateData<?> oldData, DATA newData) {
        for(com.intuso.housemate.comms.v1_0.api.payload.HousemateData<?> childData : oldData.getChildData().values())
            newData.addChildData(map(childData));
        return newData;
    }

    public com.intuso.housemate.comms.v1_0.api.payload.HousemateData<?> map(HousemateData data) {
        if(data == null)
            return null;
        else if (data instanceof NoChildrenData) {
            // should never get an instance of this
            return null;
        } else if (data instanceof ApplicationData) {
            ApplicationData applicationData = (ApplicationData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ApplicationData(applicationData.getId(), applicationData.getName(), applicationData.getDescription()));
        } else if (data instanceof ApplicationInstanceData) {
            ApplicationInstanceData applicationInstanceData = (ApplicationInstanceData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData(applicationInstanceData.getId(), applicationInstanceData.getName(), applicationInstanceData.getDescription()));
        } else if (data instanceof AutomationData) {
            AutomationData automationData = (AutomationData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.AutomationData(automationData.getId(), automationData.getName(), automationData.getDescription()));
        } else if (data instanceof CommandData) {
            CommandData commandData = (CommandData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.CommandData(commandData.getId(), commandData.getName(), commandData.getDescription()));
        } else if (data instanceof ConditionData) {
            ConditionData conditionData = (ConditionData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ConditionData(conditionData.getId(), conditionData.getName(), conditionData.getDescription()));
        } else if (data instanceof DeviceData)
            return map((DeviceData) data);
        else if (data instanceof FeatureData) {
            FeatureData featureData = (FeatureData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.FeatureData(featureData.getId(), featureData.getName(), featureData.getDescription()));
        } else if (data instanceof HardwareData) {
            HardwareData hardwareData = (HardwareData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.HardwareData(hardwareData.getId(), hardwareData.getName(), hardwareData.getDescription()));
        } else if (data instanceof ListData) {
            ListData listData = (ListData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ListData(listData.getId(), listData.getName(), listData.getDescription()));
        } else if (data instanceof OptionData) {
            OptionData optionData = (OptionData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.OptionData(optionData.getId(), optionData.getName(), optionData.getDescription()));
        } else if (data instanceof ParameterData) {
            ParameterData parameterData = (ParameterData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ParameterData(parameterData.getId(), parameterData.getName(), parameterData.getDescription(), parameterData.getType()));
        } else if (data instanceof PropertyData) {
            PropertyData propertyData = (PropertyData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.PropertyData(propertyData.getId(), propertyData.getName(), propertyData.getDescription(), propertyData.getType(), typeInstancesMapper.map(propertyData.getTypeInstances())));
        } else if (data instanceof ServerData) {
            ServerData serverData = (ServerData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ServerData(serverData.getId(), serverData.getName(), serverData.getDescription()));
        } else if (data instanceof RootData)
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.RootData());
        else if (data instanceof SubTypeData) {
            SubTypeData subTypeData = (SubTypeData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.SubTypeData(subTypeData.getId(), subTypeData.getName(), subTypeData.getDescription(), subTypeData.getType()));
        } else if (data instanceof TaskData) {
            TaskData taskData = (TaskData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.TaskData(taskData.getId(), taskData.getName(), taskData.getDescription()));
        } else if (data instanceof ChoiceTypeData) {
            ChoiceTypeData choiceTypeData = (ChoiceTypeData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ChoiceTypeData(choiceTypeData.getId(), choiceTypeData.getName(), choiceTypeData.getDescription(), choiceTypeData.getMinValues(), choiceTypeData.getMaxValues()));
        } else if (data instanceof CompoundTypeData) {
            CompoundTypeData compoundTypeData = (CompoundTypeData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.CompoundTypeData(compoundTypeData.getId(), compoundTypeData.getName(), compoundTypeData.getDescription(), compoundTypeData.getMinValues(), compoundTypeData.getMaxValues()));
        } else if (data instanceof ObjectTypeData) {
            ObjectTypeData objectTypeData = (ObjectTypeData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ObjectTypeData(objectTypeData.getId(), objectTypeData.getName(), objectTypeData.getDescription(), objectTypeData.getMinValues(), objectTypeData.getMaxValues()));
        } else if (data instanceof RegexTypeData) {
            RegexTypeData regexTypeData = (RegexTypeData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.RegexTypeData(regexTypeData.getId(), regexTypeData.getName(), regexTypeData.getDescription(), regexTypeData.getMinValues(), regexTypeData.getMaxValues(), regexTypeData.getRegexPattern()));
        } else if (data instanceof SimpleTypeData) {
            SimpleTypeData simpleTypeData = (SimpleTypeData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.SimpleTypeData(com.intuso.housemate.comms.v1_0.api.payload.SimpleTypeData.Type.valueOf(simpleTypeData.getType().getName())));
        } else if (data instanceof UserData) {
            UserData userData = (UserData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.UserData(userData.getId(), userData.getName(), userData.getDescription()));
        } else if (data instanceof ValueData) {
            ValueData valueData = (ValueData) data;
            return mapChildren(data, new com.intuso.housemate.comms.v1_0.api.payload.ValueData(valueData.getId(), valueData.getName(), valueData.getDescription(), valueData.getType(), typeInstancesMapper.map(valueData.getTypeInstances())));
        }
        throw new com.intuso.housemate.comms.v1_0.api.HousemateCommsException("Unknown HousemateData sub type " + data.getClass().getName());
    }

    public com.intuso.housemate.comms.v1_0.api.payload.DeviceData map(DeviceData deviceData) {
        return mapChildren(deviceData, new com.intuso.housemate.comms.v1_0.api.payload.DeviceData(deviceData.getId(), deviceData.getName(), deviceData.getDescription()));
    }

    private <DATA extends com.intuso.housemate.comms.v1_0.api.payload.HousemateData> DATA mapChildren(HousemateData<?> oldData, DATA newData) {
        for(HousemateData<?> childData : oldData.getChildData().values())
            newData.addChildData(map(childData));
        return newData;
    }
}
