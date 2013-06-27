package com.intuso.housemate.web.client.object;

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

    public static class All implements HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
        @Override
        public ProxyObject<?, ?, ?, ?, ?, ?, ?> create(GWTResources<?> resources, HousemateObjectWrappable<?> data) throws HousemateException {
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
            GWTResources<?>,
            GWTProxyAutomation> {
        @Override
        public GWTProxyAutomation create(GWTResources<?> resources, AutomationWrappable data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyAutomation(r, resources, data);
        }
    }

    public static class Command implements CommandFactory<GWTResources<?>, GWTProxyCommand> {
        @Override
        public GWTProxyCommand create(GWTResources<?> resources, CommandWrappable data) throws HousemateException {
            GWTResources<List<ParameterWrappable, GWTProxyParameter>> r = changeFactoryType(resources, new List<ParameterWrappable, GWTProxyParameter>());
            GWTResources<Parameter> sr = changeFactoryType(resources, parameterFactory);
            return new GWTProxyCommand(r, sr, data);
        }
    }

    public static class Condition implements ConditionFactory<GWTResources<?>, GWTProxyCondition> {
        @Override
        public GWTProxyCondition create(GWTResources<?> resources, ConditionWrappable data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyCondition(r, resources, data);
        }
    }

    public static class Device implements DeviceFactory<GWTResources<?>, GWTProxyDevice> {
        @Override
        public GWTProxyDevice create(GWTResources<?> resources, DeviceWrappable data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyDevice(r, resources, data);
        }
    }

    public static class GenericList implements ListFactory<GWTResources<?>, HousemateObjectWrappable<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            GWTProxyList<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> {

        @Override
        public GWTProxyList<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> create(GWTResources<?> resources, ListWrappable<HousemateObjectWrappable<?>> data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyList<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>(r, resources, data);
        }
    }

    public static class List<
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>>, SWBL, SWR, GWTProxyList<SWBL, SWR>> {

        @Override
        public GWTProxyList<SWBL, SWR> create(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>> resources,
                                                        ListWrappable<SWBL> data) throws HousemateException {
            return new GWTProxyList<SWBL, SWR>(resources, resources, data);
        }
    }

    public static class Option implements OptionFactory<GWTResources<?>, GWTProxyOption> {
        @Override
        public GWTProxyOption create(GWTResources<?> resources, OptionWrappable data) throws HousemateException {
            GWTResources<List<SubTypeWrappable, GWTProxySubType>> r = changeFactoryType(resources, new List<SubTypeWrappable, GWTProxySubType>());
            GWTResources<SubType> sr = changeFactoryType(resources, subTypeFactory);
            return new GWTProxyOption(r, sr, data);
        }
    }

    public static class Parameter implements ParameterFactory<GWTResources<?>, GWTProxyParameter> {
        @Override
        public GWTProxyParameter create(GWTResources<?> resources, ParameterWrappable data) throws HousemateException {
            return new GWTProxyParameter(noFactoryType(resources), data);
        }
    }

    public static class Property implements PropertyFactory<GWTResources<?>, GWTProxyProperty> {
        @Override
        public GWTProxyProperty create(GWTResources<?> resources, PropertyWrappable data) throws HousemateException {
            GWTResources<Command> r = changeFactoryType(resources, commandFactory);
            GWTResources<List<ParameterWrappable, GWTProxyParameter>> sr = changeFactoryType(resources, new List<ParameterWrappable, GWTProxyParameter>());
            return new GWTProxyProperty(r, sr, data);
        }
    }

    public static class SubType implements SubTypeFactory<GWTResources<?>, GWTProxySubType> {
        @Override
        public GWTProxySubType create(GWTResources<?> resources, SubTypeWrappable data) throws HousemateException {
            return new GWTProxySubType(noFactoryType(resources), data);
        }
    }

    public static class Task implements TaskFactory<GWTResources<?>, GWTProxyTask> {
        @Override
        public GWTProxyTask create(GWTResources<?> resources, TaskWrappable data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyTask(r, resources, data);
        }
    }

    public static class Type implements TypeFactory<GWTResources<?>, GWTProxyType> {
        @Override
        public GWTProxyType create(GWTResources<?> resources,
                                             TypeWrappable<?> data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyType(r, resources, data);
        }
    }

    public static class User implements UserFactory<GWTResources<?>, GWTProxyUser> {
        @Override
        public GWTProxyUser create(GWTResources<?> resources, UserWrappable data) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyUser(r, resources, data);
        }
    }

    public static class Value implements ValueFactory<GWTResources<?>, GWTProxyValue> {
        @Override
        public GWTProxyValue create(GWTResources<?> resources, ValueWrappable data) throws HousemateException {
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