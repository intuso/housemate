package com.intuso.housemate.annotations.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.annotations.basic.Argument;
import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.basic.Property;
import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.basic.Values;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.command.HasCommands;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.property.HasProperties;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.value.HasValues;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.real.RealArgument;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealObject;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.utilities.log.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/06/13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class AnnotationProcessor {

    private final Log log;
    private final Map<Class<?>, RealType<?, ?, ?>> registeredTypes = Maps.newHashMap();

    public AnnotationProcessor(Log log, RealList<TypeWrappable<?>, RealType<?, ?, ?>> registeredTypes) {
        this.log = log;
        registeredTypes.addObjectListener(new ListListener<RealType<?, ?, ?>>() {
            @Override
            public void elementAdded(RealType<?, ?, ?> type) {
                AnnotationProcessor.this.registeredTypes.put(type.getClass(), type);
            }

            @Override
            public void elementRemoved(RealType<?, ?, ?> type) {
                AnnotationProcessor.this.registeredTypes.remove(type.getClass());
            }
        }, true);
    }

    public void process(RealObject<?, ?, ?, ?> object) throws HousemateException {
        if(object instanceof HasCommands)
            parseCommands(object, ((HasCommands<RealList<CommandWrappable, RealCommand>>)object).getCommands());
        if(object instanceof HasProperties)
            parseProperties(object, ((HasProperties<RealList<PropertyWrappable, RealProperty<?>>>) object).getProperties());
        if(object instanceof HasValues)
            parseValues(object, ((HasValues<RealList<ValueWrappable, RealValue<?>>>) object).getValues());
    }
    
    private void parseCommands(RealObject<?, ?, ?, ?> object, RealList<CommandWrappable, RealCommand> commands) throws HousemateException {
        for(Map.Entry<Method, Command> commandMethod : getAnnotatedMethods(object.getClass(), Command.class).entrySet())
            commands.add(new CommandImpl(object.getResources(), commandMethod.getValue().id(),
                    commandMethod.getValue().name(), commandMethod.getValue().description(),
                    parseArguments(object.getResources(), commandMethod.getKey()), commandMethod.getKey(), object));
    }

    private List<RealArgument<?>> parseArguments(RealResources resources, Method method) throws HousemateException {
        List<RealArgument<?>> result = Lists.newArrayList();
        Annotation[][] argumentAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < argumentAnnotations.length; a++) {
            Argument argumentAnnotation = getAnnotation(argumentAnnotations[a], Argument.class);
            if(argumentAnnotation == null)
                throw new HousemateException("Argument " + a + " of command method " + method.getName()
                        + " is not annotated with " + Argument.class.getName());
            if(registeredTypes.get(argumentAnnotation.type()) == null)
                throw new HousemateException(argumentAnnotation.type().getName() + " does not exist");
            result.add(new RealArgument(resources, argumentAnnotation.id(), argumentAnnotation.name(),
                    argumentAnnotation.description(), registeredTypes.get(argumentAnnotation.type())));
        }
        return result;
    }

    private void parseProperties(RealObject<?, ?, ?, ?> object, RealList<PropertyWrappable, RealProperty<?>> properties) throws HousemateException {
        for(Map.Entry<Field, Property> propertyField : getAnnotatedFields(object.getClass(), Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(object);
            } catch(IllegalAccessException e) {
                log.w("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(registeredTypes.get(propertyField.getValue().type()) == null)
                throw new HousemateException(propertyField.getValue().type().getName() + " does not exist");
            properties.add(new PropertyImpl(object.getResources(), propertyField.getValue().id(),
                    propertyField.getValue().name(), propertyField.getValue().description(),
                    (RealType<?, ?, Object>) registeredTypes.get(propertyField.getValue().type()), value,
                    propertyField.getKey(), object));
        }
    }

    private void parseValues(RealObject<?, ?, ?, ?> object, RealList<ValueWrappable, RealValue<?>> values) throws HousemateException {
        for(Map.Entry<Field, Values> valuesField : getAnnotatedFields(object.getClass(), Values.class).entrySet()) {
            Map<Method, RealValue<Object>> valuesFunctions = Maps.newHashMap();
            InvocationHandler invocationHandler = new ValuesInvocationHandler(valuesFunctions);
            Object instance = Proxy.newProxyInstance(valuesField.getKey().getType().getClassLoader(),
                    new Class<?>[] {valuesField.getKey().getType()},
                    invocationHandler);
            try {
                valuesField.getKey().set(object, instance);
            } catch(IllegalAccessException e) {
                throw new HousemateException("Failed to assign proxy instance to " + valuesField.getKey().getName());
            }
            for(Map.Entry<Method, Value> valueMethod : getAnnotatedMethods(valuesField.getKey().getType(), Value.class).entrySet()) {
                if(registeredTypes.get(valueMethod.getValue().type()) == null)
                    throw new HousemateException(valueMethod.getValue().type().getName() + " does not exist");
                RealValue<Object> value = new RealValue<Object>(object.getResources(), valueMethod.getValue().id(),
                        valueMethod.getValue().name(), valueMethod.getValue().description(),
                        (RealType<?,?,Object>) registeredTypes.get(valueMethod.getValue().type()), null);
                valuesFunctions.put(valueMethod.getKey(), value);
                values.add(value);
            }
        }
    }

    private <A extends Annotation> Map<Method, A> getAnnotatedMethods(Class<?> objectClass, Class<A> annotationClass) {
        Map<Method, A> result = Maps.newHashMap();
        getAnnotatedMethods(objectClass, annotationClass, result);
        return result;
    }

    private <A extends Annotation> void getAnnotatedMethods(Class<?> objectClass, Class<A> annotationClass,
                                            Map<Method, A> methods) {
        for(Method method : objectClass.getDeclaredMethods())
            if(method.getAnnotation(annotationClass) != null)
                methods.put(method, method.getAnnotation(annotationClass));
        if(objectClass.getSuperclass() != null)
            getAnnotatedMethods(objectClass.getSuperclass(), annotationClass, methods);
    }

    private <A extends Annotation> Map<Field, A> getAnnotatedFields(Class<?> objectClass, Class<A> annotationClass) {
        Map<Field, A> result = Maps.newHashMap();
        getAnnotatedFields(objectClass, annotationClass, result);
        return result;
    }

    private <A extends Annotation> void getAnnotatedFields(Class<?> objectClass, Class<A> annotationClass,
                                                            Map<Field, A> fields) {
        for(Field field : objectClass.getDeclaredFields())
            if(field.getAnnotation(annotationClass) != null)
                fields.put(field, field.getAnnotation(annotationClass));
        if(objectClass.getSuperclass() != null)
            getAnnotatedFields(objectClass.getSuperclass(), annotationClass, fields);
    }
    
    private <A extends Annotation> A getAnnotation(Annotation[] annotations, Class<A> annotationClass) {
        for(Annotation annotation : annotations)
            if(annotation.annotationType().equals(annotationClass))
                return (A)annotation;
        return null;
    }
}
