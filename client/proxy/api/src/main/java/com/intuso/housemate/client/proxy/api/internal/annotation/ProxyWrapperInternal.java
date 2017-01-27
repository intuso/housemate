package com.intuso.housemate.client.proxy.api.internal.annotation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.annotation.*;
import com.intuso.housemate.client.api.internal.annotation.Parameter;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.api.internal.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.proxy.api.internal.object.*;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by tomc on 16/12/16.
 */
public class ProxyWrapperInternal implements ProxyWrapper {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final TypeSerialiser.Repository typeSerialiserRepository;

    @Inject
    public ProxyWrapperInternal(ManagedCollectionFactory managedCollectionFactory, TypeSerialiser.Repository typeSerialiserRepository) {
        this.typeSerialiserRepository = typeSerialiserRepository;
        this.managedCollectionFactory = managedCollectionFactory;
    }

    @Override
    public <T> T build(Logger logger, ProxyObject<?, ?> object, Class<T> clazz, String prefix) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, new InvocationHandlerImpl(object, clazz));
    }

    private Object defaultValueFor(Class<?> clazz) {
        if(boolean.class.equals(clazz))
            return false;
        else if(byte.class.equals(clazz))
            return (byte) 0;
        else if(double.class.equals(clazz))
            return 0.0;
        else if(float.class.equals(clazz))
            return 0.0f;
        else if(int.class.equals(clazz))
            return 0;
        else if(long.class.equals(clazz))
            return 0L;
        else if(short.class.equals(clazz))
            return (short) 0;
        return null;
    }

    private class InvocationHandlerImpl implements InvocationHandler {

        private final ProxyObject<?, ?> object;
        private final Class<?> clazz;
        private final Map<Method, MethodInvocationHandler> handlers = Maps.newHashMap();

        private InvocationHandlerImpl(ProxyObject<?, ?> object, Class<?> clazz) {
            this.object = object;
            this.clazz = clazz;
        }

        @Override
        public synchronized Object invoke(Object o, Method method, Object[] args) throws Throwable {
            if(!handlers.containsKey(method))
                handlers.put(method, createHandler(method));
            return handlers.get(method).handle(args);
        }

        MethodInvocationHandler createHandler(Method method) {
            Id id = method.getAnnotation(Id.class);
            if(method.getAnnotation(Command.class) != null)
                return createCommandHandler(method);
            else if(method.getAnnotation(Property.class) != null)
                return createPropertyHandler(method, method.getAnnotation(Property.class));
            else if(method.getAnnotation(Value.class) != null)
                return createValueHandler(method, id, method.getAnnotation(Value.class));
            else if(method.getAnnotation(com.intuso.housemate.client.api.internal.annotation.AddListener.class) != null)
                return createAddListenerHandler(method);
            return new Problem("Don't know how to handle invocation of " + clazz.getName() + " method " + method.toString() + ". Expecting one of the following annotations:\n" + Command.class.getName() + "\n" + Property.class.getName() + "\n" + AddListener.class.getName());
        }

        MethodInvocationHandler createCommandHandler(Method method) {
            Id id = method.getAnnotation(Id.class);
            if(id == null)
                return new Problem(clazz.getName() + " command method " + method.toString() + " has no " + Id.class.getName() + " annotation");
            if(!(object instanceof ProxyCommand.Container))
                return new Problem(clazz.getName() + " has a command method " + method.toString() + " but the object being wrapped is not a " + ProxyCommand.Container.class.getName());
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            ParameterSerialiser[] parameterSerialisers = new ParameterSerialiser[parameterAnnotations.length];
            for(int p = 0; p < parameterAnnotations.length; p++) {
                Id parameterId = null;
                Parameter parameter = null;
                for(int a = 0; a < parameterAnnotations[p].length; a++) {
                    if (parameterAnnotations[p][a] instanceof Id)
                        parameterId = (Id) parameterAnnotations[p][a];
                    else if (parameterAnnotations[p][a] instanceof Parameter)
                        parameter = (Parameter) parameterAnnotations[p][a];
                }
                if(parameterId != null)
                    return new Problem(clazz.getName() + " command method " + method.toString() + " has no " + Id.class.getName() + " annotation for parameter " + p);
                if(parameter == null)
                    parameter = new ParameterDefaultImpl();
                TypeSerialiser typeSerialiser = typeSerialiserRepository.getSerialiser(new TypeSpec(method.getParameterTypes()[p], parameter.restriction()));
                parameterSerialisers[p] = new ParameterSerialiser(parameterId.value(), parameter.minValues(), parameter.maxValues(), typeSerialiser);
            }
            return new CommandInvoker(
                    ((ProxyCommand.Container<? extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>>) object).getCommands(),
                    id.value(),
                    parameterSerialisers);
        }

        MethodInvocationHandler createPropertyHandler(Method method, Property property) {
            if(method.getParameterTypes().length != 1)
                return new Problem(clazz.getName() + " property method should have a single argument");
            Id id = method.getAnnotation(Id.class);
            if(id == null)
                return new Problem(clazz.getName() + " property method " + method.toString() + " has no " + Id.class.getName() + " annotation");
            if(!(object instanceof ProxyProperty.Container))
                return new Problem(clazz.getName() + " has a property method " + method.toString() + " but the object being wrapped is not a " + ProxyProperty.Container.class.getName());
            TypeSerialiser typeSerialiser = typeSerialiserRepository.getSerialiser(new TypeSpec(method.getParameterTypes()[0], property.restriction()));
            return new PropertySetter(
                    ((ProxyProperty.Container<? extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>>) object).getProperties(),
                    id.value(),
                    new PropertyValueSerialiser(property.minValues(), property.maxValues(), typeSerialiser));
        }

        MethodInvocationHandler createValueHandler(Method method, Id id, Value value) {
            if(method.getParameterTypes().length != 0)
                return new Problem(clazz.getName() + " value method should not have any arguments");
            if(id == null)
                return new Problem(clazz.getName() + " value method " + method.toString() + " has no " + Id.class.getName() + " annotation");
            if(!(object instanceof ProxyValue.Container))
                return new Problem(clazz.getName() + " has a value method " + method.toString() + " but the object being wrapped is not a " + ProxyValue.Container.class.getName());
            TypeSerialiser typeSerialiser = typeSerialiserRepository.getSerialiser(new TypeSpec(method.getGenericReturnType(), value.restriction()));
            return new ValueGetter(
                    ((ProxyValue.Container<? extends ProxyList<? extends ProxyValue<?, ?>, ?>>) object).getValues(),
                    id.value(),
                    new ValueDeserialiser(List.class.isAssignableFrom(method.getReturnType()), typeSerialiser, defaultValueFor(method.getReturnType())));
        }

        MethodInvocationHandler createAddListenerHandler(Method method) {

            // check there is only a single argument
            if(method.getParameterTypes().length != 1)
                return new Problem(clazz.getName() + " add listener method should have a single argument");

            // check the object is a value container
            if(!(object instanceof ProxyValue.Container))
                return new Problem(clazz.getName() + " has an add listener method " + method.toString() + " but the object being wrapped is not a " + ProxyValue.Container.class.getName());

            // find all the value methods, and listen to those values
            ManagedCollection listeners = managedCollectionFactory.create();
            findValuesIn(method.getParameterTypes()[0], listeners, ((ProxyValue.Container<ProxyList<ProxyValue<?, ?>, ?>>) object).getValues());

            return new AddListener(listeners);
        }

        private void findValuesIn(Class<?> clazz, final ManagedCollection listeners, final ProxyList<ProxyValue<?, ?>, ?> values) {
            for(Method method : clazz.getDeclaredMethods()) {
                if (method.getAnnotation(Value.class) != null) {
                    Value value = method.getAnnotation(Value.class);
                    if(method.getParameterTypes().length != 1)
                        throw new HousemateException(clazz.getName() + " value method \" + method.toString() + \" should have a single argument");
                    Id id = method.getAnnotation(Id.class);
                    if(id == null)
                        throw new HousemateException(clazz.getName() + " value method " + method.toString() + " has no " + Id.class.getName() + " annotation");
                    TypeSerialiser typeSerialiser = typeSerialiserRepository.getSerialiser(new TypeSpec(method.getParameterTypes()[0], value.restriction()));
                    addValueListener(listeners, method, values, id.value(), new ValueDeserialiser(List.class.isAssignableFrom(method.getParameterTypes()[0]), typeSerialiser, defaultValueFor(method.getParameterTypes()[0])));
                }
            }
            if(clazz.getSuperclass() != null)
                findValuesIn(clazz.getSuperclass(), listeners, values);
            for(Class<?> interfaceClass : clazz.getInterfaces())
                findValuesIn(interfaceClass, listeners, values);
        }

        private void addValueListener(final ManagedCollection listeners, final Method method, final ProxyList<ProxyValue<?, ?>, ?> values, final String id, final ValueDeserialiser valueDeserialiser) {
            values.addObjectListener(new com.intuso.housemate.client.api.internal.object.List.Listener<ProxyValue<?, ?>, ProxyList<ProxyValue<?, ?>, ?>>() {

                @Override
                public void elementRemoved(ProxyList<ProxyValue<?, ?>, ?> list, ProxyValue<?, ?> v) {

                }

                @Override
                public void elementAdded(ProxyList<ProxyValue<?, ?>, ?> list, ProxyValue<?, ?> v) {
                    if(v.getId().equals(id))
                        v.addObjectListener(new ValueListenerCallback(listeners, method, valueDeserialiser));
                }
            }, true);
        }

        private class ValueListenerCallback implements com.intuso.housemate.client.api.internal.object.Value.Listener<ProxyValue<?, ?>> {

            private final ManagedCollection listeners;
            private final Method method;
            private final ValueDeserialiser valueDeserialiser;

            private ValueListenerCallback(ManagedCollection listeners, Method method, ValueDeserialiser valueDeserialiser) {
                this.listeners = listeners;
                this.method = method;
                this.valueDeserialiser = valueDeserialiser;
            }

            @Override
            public void valueChanging(ProxyValue<?, ?> proxyValue) {

            }

            @Override
            public void valueChanged(ProxyValue<?, ?> value) {
                Object[] args = new Object[] {valueDeserialiser.deserialise(value.getValue())};
                for(Object listener : listeners) {
                    try {
                        method.invoke(listener, args);
                    } catch (InvocationTargetException |IllegalAccessException e) {
                        new HousemateException("Failed to invoke listener " + listener + " for changed value " + value.getId(), e).printStackTrace();
                    }
                }
            }
        }

        private class Problem implements MethodInvocationHandler {

            private final String message;

            private Problem(String message) {
                this.message = message;
            }

            @Override
            public Object handle(Object[] args) {
                throw new HousemateException(message);
            }
        }

        private class CommandInvoker implements MethodInvocationHandler {

            private final ProxyList<? extends ProxyCommand<?, ?, ?>, ?> commands;
            private final String commandId;
            private final ParameterSerialiser[] parameterSerialisers;

            private CommandInvoker(ProxyList<? extends ProxyCommand<?, ?, ?>, ?> commands, String commandId, ParameterSerialiser[] parameterSerialisers) {
                this.commands = commands;
                this.commandId = commandId;
                this.parameterSerialisers = parameterSerialisers;
            }

            @Override
            public Object handle(Object[] args) {
                final CountDownLatch latch = new CountDownLatch(1);
                ProxyCommand<?, ?, ?> command = commands.get(commandId);
                if(commandId == null)
                    throw new HousemateException("Could not find command " + commandId);
                Type.InstanceMap values = new Type.InstanceMap();
                for(int i = 0; i < args.length; i++)
                    values.getChildren().put(parameterSerialisers[i].getKey(), parameterSerialisers[i].serialise(args[i]));
                command.perform(values, new com.intuso.housemate.client.api.internal.object.Command.PerformListener<ProxyCommand<?, ?, ?>>() {
                    @Override
                    public void commandStarted(ProxyCommand<?, ?, ?> command) {

                    }

                    @Override
                    public void commandFinished(ProxyCommand<?, ?, ?> command) {
                        latch.countDown();
                    }

                    @Override
                    public void commandFailed(ProxyCommand<?, ?, ?> command, String error) {
                        latch.countDown();
                    }
                });

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new HousemateException("Failed to wait for command to complete before returning", e);
                }

                return null;
            }
        }

        private class ParameterSerialiser {

            private final String key;
            private final int minValues;
            private final int maxValues;
            private final TypeSerialiser serialiser;

            private ParameterSerialiser(String key, int minValues, int maxValues, TypeSerialiser serialiser) {
                this.key = key;
                this.minValues = minValues;
                this.maxValues = maxValues;
                this.serialiser = serialiser;
            }

            public String getKey() {
                return key;
            }

            public Type.Instances serialise(Object object) {
                if(object instanceof List)
                    return serialiseList((List<?>) object);
                else if (object instanceof Array)
                    return serialiseList(Arrays.asList((Array) object));
                else
                    return serialiseList(Lists.newArrayList(object));
            }

            public Type.Instances serialiseList(List<?> values) {
                if(values == null)
                    return null;
                if(values.size() < minValues)
                    throw new RuntimeException("There are less elements in the list (" + values.size() + ") than the configured minValues (" + minValues + ")");
                if(maxValues >= 0 && values.size() > maxValues)
                    throw new RuntimeException("There are more elements in the list (" + values.size() + ") than the configured maxValues (" + minValues + ")");
                Type.Instances result = new Type.Instances();
                for(Object typedValue : values)
                    result.getElements().add(serialiser.serialise(typedValue));
                return result;
            }
        }

        private class PropertySetter implements MethodInvocationHandler {

            private final ProxyList<? extends ProxyProperty<?, ?, ?>, ?> properties;
            private final String propertyId;
            private final PropertyValueSerialiser valueSerialiser;

            private PropertySetter(ProxyList<? extends ProxyProperty<?, ?, ?>, ?> properties, String propertyId, PropertyValueSerialiser valueSerialiser) {
                this.properties = properties;
                this.propertyId = propertyId;
                this.valueSerialiser = valueSerialiser;
            }

            @Override
            public Object handle(Object[] args) {
                final CountDownLatch latch = new CountDownLatch(1);
                ProxyProperty<?, ?, ?> property = properties.get(propertyId);
                if(property == null)
                    throw new HousemateException("Could not find property " + propertyId);
                Type.InstanceMap values = new Type.InstanceMap();
                values.getChildren().put("value", valueSerialiser.serialise(args[0]));
                property.getSetCommand().perform(values, new com.intuso.housemate.client.api.internal.object.Command.PerformListener<ProxyCommand<?, ?, ?>>() {
                    @Override
                    public void commandStarted(ProxyCommand<?, ?, ?> command) {

                    }

                    @Override
                    public void commandFinished(ProxyCommand<?, ?, ?> command) {
                        latch.countDown();
                    }

                    @Override
                    public void commandFailed(ProxyCommand<?, ?, ?> command, String error) {
                        latch.countDown();
                    }
                });

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new HousemateException("Failed to wait for property to complete before returning", e);
                }

                return null;
            }
        }

        private class PropertyValueSerialiser {

            private final int minValues;
            private final int maxValues;
            private final TypeSerialiser serialiser;

            private PropertyValueSerialiser(int minValues, int maxValues, TypeSerialiser serialiser) {
                this.minValues = minValues;
                this.maxValues = maxValues;
                this.serialiser = serialiser;
            }

            public Type.Instances serialise(Object object) {
                if(object instanceof List)
                    return serialiseList((List<?>) object);
                else if (object instanceof Array)
                    return serialiseList(Arrays.asList((Array) object));
                else
                    return serialiseList(Lists.newArrayList(object));
            }

            public Type.Instances serialiseList(List<?> values) {
                if(values == null)
                    return null;
                if(values.size() < minValues)
                    throw new RuntimeException("There are less elements in the list (" + values.size() + ") than the configured minValues (" + minValues + ")");
                if(maxValues >= 0 && values.size() > maxValues)
                    throw new RuntimeException("There are more elements in the list (" + values.size() + ") than the configured maxValues (" + minValues + ")");
                Type.Instances result = new Type.Instances();
                for(Object typedValue : values)
                    result.getElements().add(serialiser.serialise(typedValue));
                return result;
            }
        }

        private class ValueGetter implements MethodInvocationHandler {

            private final ProxyList<? extends ProxyValue<?, ?>, ?> values;
            private final String valueId;
            private final ValueDeserialiser valueDeserialiser;

            private ValueGetter(ProxyList<? extends ProxyValue<?, ?>, ?> values, String valueId, ValueDeserialiser valueDeserialiser) {
                this.values = values;
                this.valueId = valueId;
                this.valueDeserialiser = valueDeserialiser;
            }

            @Override
            public Object handle(Object[] args) {
                ProxyValue<?, ?> value = values.get(valueId);
                if(value == null)
                    throw new HousemateException("Could not find value " + valueId);
                Type.Instances values = value.getValue();
                return valueDeserialiser.deserialise(values);
            }
        }

        private class AddListener implements MethodInvocationHandler {

            private final ManagedCollection listeners;

            public AddListener(ManagedCollection listeners) {
                this.listeners = listeners;
            }

            @Override
            public Object handle(Object[] args) {
                return listeners.add(args[0]);
            }
        }

        private class ValueDeserialiser {

            private final boolean isList;
            private final TypeSerialiser serialiser;
            private final Object defaultValue;

            private ValueDeserialiser(boolean isList, TypeSerialiser serialiser, Object defaultValue) {
                this.isList = isList;
                this.serialiser = serialiser;
                this.defaultValue = defaultValue;
            }

            public Object deserialise(Type.Instances instances) {
                List<Object> result = Lists.newArrayList();
                if(instances != null && instances.getElements() != null)
                    for(Type.Instance instance : instances.getElements())
                        result.add(serialiser.deserialise(instance));
                if(!isList) {
                    if(result.size() == 0)
                        return defaultValue;
                    else if(result.size() == 1) {
                        return result.get(0) == null ? defaultValue : result.get(0);
                    } else
                        throw new HousemateException("Cannot convert list of multiple values to a single value");
                }
                return result;
            }
        }
    }

    private interface MethodInvocationHandler {
        Object handle(Object[] args);
    }

    private class ParameterDefaultImpl implements Parameter {

        @Override
        public String restriction() {
            return "";
        }

        @Override
        public int minValues() {
            return -1;
        }

        @Override
        public int maxValues() {
            return -1;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Parameter.class;
        }
    }
}
