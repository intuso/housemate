package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.parameter.ParameterFactory;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.automation.AutomationFactory;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.condition.ConditionFactory;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.TaskFactory;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.OptionFactory;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeFactory;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.user.UserFactory;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.ValueFactory;
import com.intuso.housemate.api.object.value.ValueWrappable;
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

    public static class All implements HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
        @Override
        public ProxyObject<?, ?, ?, ?, ?, ?, ?> create(ProxyResources<?> resources, HousemateObjectWrappable<?> data) throws HousemateException {
            if(data instanceof ParameterWrappable)
                return parameterFactory.create(resources, (ParameterWrappable) data);
            else if(data instanceof CommandWrappable)
                return commandFactory.create(resources, (CommandWrappable) data);
            else if(data instanceof ConditionWrappable)
                return conditionFactory.create(resources, (ConditionWrappable) data);
            else if(data instanceof UserWrappable)
                return userFactory.create(resources, (UserWrappable) data);
            else if(data instanceof TaskWrappable)
                return taskFactory.create(resources, (TaskWrappable) data);
            else if(data instanceof DeviceWrappable)
                return deviceFactory.create(resources, (DeviceWrappable) data);
            else if(data instanceof ListWrappable)
                return listFactory.create(resources, (ListWrappable<HousemateObjectWrappable<?>>) data);
            else if(data instanceof OptionWrappable)
                return optionFactory.create(resources, (OptionWrappable) data);
            else if(data instanceof PropertyWrappable)
                return propertyFactory.create(resources, (PropertyWrappable) data);
            else if(data instanceof AutomationWrappable)
                return automationFactory.create(resources, (AutomationWrappable) data);
            else if(data instanceof SubTypeWrappable)
                return subTypeFactory.create(resources, (SubTypeWrappable) data);
            else if(data instanceof TypeWrappable)
                return typeFactory.create(resources, (TypeWrappable) data);
            else if(data instanceof ValueWrappable)
                return valueFactory.create(resources, (ValueWrappable) data);
            else
                throw new HousemateException("Don't know how to create an object from " + data.getClass().getName());
        }
    }

    public static class Automation implements AutomationFactory<
            ProxyResources<?>,
            SimpleProxyObject.Automation> {
        @Override
        public SimpleProxyObject.Automation create(ProxyResources<?> resources, AutomationWrappable data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Automation(r, resources, data);
        }
    }

    public static class Command implements CommandFactory<ProxyResources<?>, SimpleProxyObject.Command> {
        @Override
        public SimpleProxyObject.Command create(ProxyResources<?> resources, CommandWrappable data) throws HousemateException {
            ProxyResources<List<ParameterWrappable, SimpleProxyObject.Parameter>> r = changeFactoryType(resources, new List<ParameterWrappable, SimpleProxyObject.Parameter>());
            ProxyResources<Parameter> sr = changeFactoryType(resources, parameterFactory);
            return new SimpleProxyObject.Command(r, sr, data);
        }
    }

    public static class Condition implements ConditionFactory<ProxyResources<?>, SimpleProxyObject.Condition> {
        @Override
        public SimpleProxyObject.Condition create(ProxyResources<?> resources, ConditionWrappable data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Condition(r, resources, data);
        }
    }

    public static class Device implements DeviceFactory<ProxyResources<?>, SimpleProxyObject.Device> {
        @Override
        public SimpleProxyObject.Device create(ProxyResources<?> resources, DeviceWrappable data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Device(r, resources, data);
        }
    }

    public static class GenericList implements ListFactory<ProxyResources<?>, HousemateObjectWrappable<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> {

        @Override
        public SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> create(ProxyResources<?> resources, ListWrappable<HousemateObjectWrappable<?>> data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>(r, resources, data);
        }
    }

    public static class List<
                SWBL extends HousemateObjectWrappable<?>,
                SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SWBL, SWR>>, SWBL, SWR, SimpleProxyObject.List<SWBL, SWR>> {

        @Override
        public SimpleProxyObject.List<SWBL, SWR> create(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SWBL, SWR>> resources,
                                                        ListWrappable<SWBL> data) throws HousemateException {
            return new SimpleProxyObject.List<SWBL, SWR>(resources, resources, data);
        }
    }

    public static class Option implements OptionFactory<ProxyResources<?>, SimpleProxyObject.Option> {
        @Override
        public SimpleProxyObject.Option create(ProxyResources<?> resources, OptionWrappable data) throws HousemateException {
            ProxyResources<List<SubTypeWrappable, SimpleProxyObject.SubType>> r = changeFactoryType(resources,
                    new List<SubTypeWrappable, SimpleProxyObject.SubType>());
            ProxyResources<SubType> sr = changeFactoryType(resources, subTypeFactory);
            return new SimpleProxyObject.Option(r, sr, data);
        }
    }

    public static class Parameter implements ParameterFactory<ProxyResources<?>, SimpleProxyObject.Parameter> {
        @Override
        public SimpleProxyObject.Parameter create(ProxyResources<?> resources, ParameterWrappable data) throws HousemateException {
            return new SimpleProxyObject.Parameter(noFactoryType(resources), data);
        }
    }

    public static class Property implements PropertyFactory<ProxyResources<?>, SimpleProxyObject.Property> {
        @Override
        public SimpleProxyObject.Property create(ProxyResources<?> resources, PropertyWrappable data) throws HousemateException {
            ProxyResources<Command> r = changeFactoryType(resources, commandFactory);
            ProxyResources<List<ParameterWrappable, SimpleProxyObject.Parameter>> sr = changeFactoryType(resources, new List<ParameterWrappable, SimpleProxyObject.Parameter>());
            return new SimpleProxyObject.Property(r, sr, data);
        }
    }

    public static class SubType implements SubTypeFactory<ProxyResources<?>, SimpleProxyObject.SubType> {
        @Override
        public SimpleProxyObject.SubType create(ProxyResources<?> resources, SubTypeWrappable data) throws HousemateException {
            return new SimpleProxyObject.SubType(noFactoryType(resources), data);
        }
    }

    public static class Task implements TaskFactory<ProxyResources<?>, SimpleProxyObject.Task> {
        @Override
        public SimpleProxyObject.Task create(ProxyResources<?> resources, TaskWrappable data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Task(r, resources, data);
        }
    }

    public static class Type implements TypeFactory<ProxyResources<?>, SimpleProxyObject.Type> {
        @Override
        public SimpleProxyObject.Type create(ProxyResources<?> resources,
                                             TypeWrappable<?> data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Type(r, resources, data);
        }
    }

    public static class User implements UserFactory<ProxyResources<?>, SimpleProxyObject.User> {
        @Override
        public SimpleProxyObject.User create(ProxyResources<?> resources, UserWrappable data) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.User(r, resources, data);
        }
    }

    public static class Value implements ValueFactory<ProxyResources<?>, SimpleProxyObject.Value> {
        @Override
        public SimpleProxyObject.Value create(ProxyResources<?> resources, ValueWrappable data) throws HousemateException {
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
