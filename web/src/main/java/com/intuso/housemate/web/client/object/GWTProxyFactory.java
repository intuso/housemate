package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterFactory;
import com.intuso.housemate.api.object.automation.AutomationFactory;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.condition.ConditionFactory;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.task.TaskFactory;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.option.OptionFactory;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.subtype.SubTypeFactory;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserFactory;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.api.object.value.ValueFactory;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyFactory {

    private final static All allFactory = new All();
    private final static Automation automationFactory = new Automation();
    private final static Command commandFactory = new Command();
    private final static Condition conditionFactory = new Condition();
    private final static Device deviceFactory = new Device();
    private final static GenericList listFactory = new GenericList();
    private final static Option optionFactory = new Option();
    private final static Parameter parameterFactory = new Parameter();
    private final static Property propertyFactory = new Property();
    private final static SubType subTypeFactory = new SubType();
    private final static Task taskFactory = new Task();
    private final static Type typeFactory = new Type();
    private final static User userFactory = new User();
    private final static Value valueFactory = new Value();

    public static class All implements HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
        @Override
        public ProxyObject<?, ?, ?, ?, ?, ?, ?> create(GWTResources<?> resources, HousemateData<?> data) throws HousemateException {
            if(data instanceof ParameterData)
                return parameterFactory.create(resources, (ParameterData) data);
            else if(data instanceof CommandData)
                return commandFactory.create(resources, (CommandData) data);
            else if(data instanceof ConditionData)
                return conditionFactory.create(resources, (ConditionData) data);
            else if(data instanceof UserData)
                return userFactory.create(resources, (UserData) data);
            else if(data instanceof TaskData)
                return taskFactory.create(resources, (TaskData) data);
            else if(data instanceof DeviceData)
                return deviceFactory.create(resources, (DeviceData) data);
            else if(data instanceof ListData)
                return listFactory.create(resources, (ListData<HousemateData<?>>) data);
            else if(data instanceof OptionData)
                return optionFactory.create(resources, (OptionData) data);
            else if(data instanceof PropertyData)
                return propertyFactory.create(resources, (PropertyData) data);
            else if(data instanceof AutomationData)
                return automationFactory.create(resources, (AutomationData) data);
            else if(data instanceof SubTypeData)
                return subTypeFactory.create(resources, (SubTypeData) data);
            else if(data instanceof TypeData)
                return typeFactory.create(resources, (TypeData) data);
            else if(data instanceof ValueData)
                return valueFactory.create(resources, (ValueData) data);
            else
                throw new HousemateException("Don't know how to create an object from " + data.getClass().getName());
        }
    }

    public static class Automation implements AutomationFactory<
            GWTResources<?>,
            GWTProxyAutomation> {
        @Override
        public GWTProxyAutomation create(GWTResources<?> resources, AutomationData data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyAutomation(r, resources, data);
        }
    }

    public static class Command implements CommandFactory<GWTResources<?>, GWTProxyCommand> {
        @Override
        public GWTProxyCommand create(GWTResources<?> resources, CommandData data) throws HousemateException {
            GWTResources<List<ParameterData, GWTProxyParameter>> r = changeFactoryType(resources, new List<ParameterData, GWTProxyParameter>());
            GWTResources<Parameter> sr = changeFactoryType(resources, parameterFactory);
            return new GWTProxyCommand(r, sr, data);
        }
    }

    public static class Condition implements ConditionFactory<GWTResources<?>, GWTProxyCondition> {
        @Override
        public GWTProxyCondition create(GWTResources<?> resources, ConditionData data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyCondition(r, resources, data);
        }
    }

    public static class Device implements DeviceFactory<GWTResources<?>, GWTProxyDevice> {
        @Override
        public GWTProxyDevice create(GWTResources<?> resources, DeviceData data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyDevice(r, resources, data);
        }
    }

    public static class GenericList implements ListFactory<GWTResources<?>, HousemateData<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> {

        @Override
        public GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> create(GWTResources<?> resources, ListData<HousemateData<?>> data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>(r, resources, data);
        }
    }

    public static class List<
            SWBL extends HousemateData<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>>, SWBL, SWR, GWTProxyList<SWBL, SWR>> {

        @Override
        public GWTProxyList<SWBL, SWR> create(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>> resources,
                                                        ListData<SWBL> data) throws HousemateException {
            return new GWTProxyList<SWBL, SWR>(resources, resources, data);
        }
    }

    public static class Option implements OptionFactory<GWTResources<?>, GWTProxyOption> {
        @Override
        public GWTProxyOption create(GWTResources<?> resources, OptionData data) throws HousemateException {
            GWTResources<List<SubTypeData, GWTProxySubType>> r = changeFactoryType(resources, new List<SubTypeData, GWTProxySubType>());
            GWTResources<SubType> sr = changeFactoryType(resources, subTypeFactory);
            return new GWTProxyOption(r, sr, data);
        }
    }

    public static class Parameter implements ParameterFactory<GWTResources<?>, GWTProxyParameter> {
        @Override
        public GWTProxyParameter create(GWTResources<?> resources, ParameterData data) throws HousemateException {
            return new GWTProxyParameter(noFactoryType(resources), data);
        }
    }

    public static class Property implements PropertyFactory<GWTResources<?>, GWTProxyProperty> {
        @Override
        public GWTProxyProperty create(GWTResources<?> resources, PropertyData data) throws HousemateException {
            GWTResources<Command> r = changeFactoryType(resources, commandFactory);
            GWTResources<List<ParameterData, GWTProxyParameter>> sr = changeFactoryType(resources, new List<ParameterData, GWTProxyParameter>());
            return new GWTProxyProperty(r, sr, data);
        }
    }

    public static class SubType implements SubTypeFactory<GWTResources<?>, GWTProxySubType> {
        @Override
        public GWTProxySubType create(GWTResources<?> resources, SubTypeData data) throws HousemateException {
            return new GWTProxySubType(noFactoryType(resources), data);
        }
    }

    public static class Task implements TaskFactory<GWTResources<?>, GWTProxyTask> {
        @Override
        public GWTProxyTask create(GWTResources<?> resources, TaskData data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyTask(r, resources, data);
        }
    }

    public static class Type implements TypeFactory<GWTResources<?>, GWTProxyType> {
        @Override
        public GWTProxyType create(GWTResources<?> resources,
                                             TypeData<?> data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyType(r, resources, data);
        }
    }

    public static class User implements UserFactory<GWTResources<?>, GWTProxyUser> {
        @Override
        public GWTProxyUser create(GWTResources<?> resources, UserData data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyUser(r, resources, data);
        }
    }

    public static class Value implements ValueFactory<GWTResources<?>, GWTProxyValue> {
        @Override
        public GWTProxyValue create(GWTResources<?> resources, ValueData data) throws HousemateException {
            return new GWTProxyValue(noFactoryType(resources), data);
        }
    }

    public static <NF extends HousemateObjectFactory<? extends ProxyResources<?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
            GWTResources<NF> changeFactoryType(GWTResources<?> resources, NF newFactory) {
        return new GWTResources<NF>(resources.getLog(), resources.getProperties(), resources.getRouter(),
                newFactory, resources.getRegexMatcherFactory());
    }

    public static GWTResources<NoChildrenProxyObjectFactory> noFactoryType(GWTResources<?> resources) {
        return changeFactoryType(resources, null);
    }
}