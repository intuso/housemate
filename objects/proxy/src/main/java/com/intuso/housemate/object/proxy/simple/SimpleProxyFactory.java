package com.intuso.housemate.object.proxy.simple;

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

/**
 * Container class for factories of all the simple proxy object implementations.
 *
 * @see SimpleProxyObject
 */
public class SimpleProxyFactory {

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

    public static class All implements HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
        @Override
        public ProxyObject<?, ?, ?, ?, ?, ?, ?> create(ProxyResources<?> resources, HousemateData<?> data) throws HousemateException {
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
            ProxyResources<?>,
            SimpleProxyObject.Automation> {
        @Override
        public SimpleProxyObject.Automation create(ProxyResources<?> resources, AutomationData data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Automation(r, resources, data);
        }
    }

    public static class Command implements CommandFactory<ProxyResources<?>, SimpleProxyObject.Command> {
        @Override
        public SimpleProxyObject.Command create(ProxyResources<?> resources, CommandData data) throws HousemateException {
            ProxyResources<List<ParameterData, SimpleProxyObject.Parameter>> r = changeFactoryType(resources, new List<ParameterData, SimpleProxyObject.Parameter>());
            ProxyResources<Parameter> sr = changeFactoryType(resources, parameterFactory);
            return new SimpleProxyObject.Command(r, sr, data);
        }
    }

    public static class Condition implements ConditionFactory<ProxyResources<?>, SimpleProxyObject.Condition> {
        @Override
        public SimpleProxyObject.Condition create(ProxyResources<?> resources, ConditionData data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Condition(r, resources, data);
        }
    }

    public static class Device implements DeviceFactory<ProxyResources<?>, SimpleProxyObject.Device> {
        @Override
        public SimpleProxyObject.Device create(ProxyResources<?> resources, DeviceData data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Device(r, resources, data);
        }
    }

    public static class GenericList implements ListFactory<ProxyResources<?>, HousemateData<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            SimpleProxyObject.List<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> {

        @Override
        public SimpleProxyObject.List<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> create(ProxyResources<?> resources, ListData<HousemateData<?>> data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.List<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>(r, resources, data);
        }
    }

    public static class List<
                SWBL extends HousemateData<?>,
                SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SWBL, SWR>>, SWBL, SWR, SimpleProxyObject.List<SWBL, SWR>> {

        @Override
        public SimpleProxyObject.List<SWBL, SWR> create(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SWBL, SWR>> resources,
                                                        ListData<SWBL> data) throws HousemateException {
            return new SimpleProxyObject.List<SWBL, SWR>(resources, resources, data);
        }
    }

    public static class Option implements OptionFactory<ProxyResources<?>, SimpleProxyObject.Option> {
        @Override
        public SimpleProxyObject.Option create(ProxyResources<?> resources, OptionData data) throws HousemateException {
            ProxyResources<List<SubTypeData, SimpleProxyObject.SubType>> r = changeFactoryType(resources,
                    new List<SubTypeData, SimpleProxyObject.SubType>());
            ProxyResources<SubType> sr = changeFactoryType(resources, subTypeFactory);
            return new SimpleProxyObject.Option(r, sr, data);
        }
    }

    public static class Parameter implements ParameterFactory<ProxyResources<?>, SimpleProxyObject.Parameter> {
        @Override
        public SimpleProxyObject.Parameter create(ProxyResources<?> resources, ParameterData data) throws HousemateException {
            return new SimpleProxyObject.Parameter(noFactoryType(resources), data);
        }
    }

    public static class Property implements PropertyFactory<ProxyResources<?>, SimpleProxyObject.Property> {
        @Override
        public SimpleProxyObject.Property create(ProxyResources<?> resources, PropertyData data) throws HousemateException {
            ProxyResources<Command> r = changeFactoryType(resources, commandFactory);
            ProxyResources<List<ParameterData, SimpleProxyObject.Parameter>> sr = changeFactoryType(resources, new List<ParameterData, SimpleProxyObject.Parameter>());
            return new SimpleProxyObject.Property(r, sr, data);
        }
    }

    public static class SubType implements SubTypeFactory<ProxyResources<?>, SimpleProxyObject.SubType> {
        @Override
        public SimpleProxyObject.SubType create(ProxyResources<?> resources, SubTypeData data) throws HousemateException {
            return new SimpleProxyObject.SubType(noFactoryType(resources), data);
        }
    }

    public static class Task implements TaskFactory<ProxyResources<?>, SimpleProxyObject.Task> {
        @Override
        public SimpleProxyObject.Task create(ProxyResources<?> resources, TaskData data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Task(r, resources, data);
        }
    }

    public static class Type implements TypeFactory<ProxyResources<?>, SimpleProxyObject.Type> {
        @Override
        public SimpleProxyObject.Type create(ProxyResources<?> resources,
                                             TypeData<?> data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Type(r, resources, data);
        }
    }

    public static class User implements UserFactory<ProxyResources<?>, SimpleProxyObject.User> {
        @Override
        public SimpleProxyObject.User create(ProxyResources<?> resources, UserData data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.User(r, resources, data);
        }
    }

    public static class Value implements ValueFactory<ProxyResources<?>, SimpleProxyObject.Value> {
        @Override
        public SimpleProxyObject.Value create(ProxyResources<?> resources, ValueData data) throws HousemateException {
            return new SimpleProxyObject.Value(noFactoryType(resources), data);
        }
    }

    public static
            <NF extends HousemateObjectFactory<? extends ProxyResources<?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
            ProxyResources<NF> changeFactoryType(ProxyResources<?> resources, NF newFactory) {
        return new ProxyResources<NF>(resources.getLog(), resources.getProperties(), resources.getRouter(),
                newFactory, resources.getRegexMatcherFactory());
    }

    public static ProxyResources<NoChildrenProxyObjectFactory> noFactoryType(ProxyResources<?> resources) {
        return changeFactoryType(resources, null);
    }
}
