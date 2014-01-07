package com.intuso.housemate.annotations.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.annotations.basic.*;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.HasCommands;
import com.intuso.housemate.api.object.property.HasProperties;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.value.HasValues;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.device.feature.RealFeature;
import com.intuso.utilities.log.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * Processor of annotated devices etc
 */
public class AnnotationProcessor {

    private final Log log;

    @Inject
    public AnnotationProcessor(Log log) {
        this.log = log;
    }

    public void process(RealList<TypeData<?>, RealType<?, ?, ?>> types, RealObject<?, ?, ?, ?> object) throws HousemateException {
        if(object instanceof HasCommands)
            parseCommands(types, object, ((HasCommands<RealList<CommandData, RealCommand>>)object).getCommands());
        if(object instanceof HasProperties)
            parseProperties(types, object, ((HasProperties<RealList<PropertyData, RealProperty<?>>>) object).getProperties());
        if(object instanceof HasValues)
            parseValues(types, object, ((HasValues<RealList<ValueData, RealValue<?>>>) object).getValues());
        if(object instanceof RealDevice)
            parseFeatures((RealDevice) object, (Class<? extends RealDevice>) object.getClass());
    }
    
    private void parseCommands(RealList<TypeData<?>, RealType<?, ?, ?>> types, RealObject<?, ?, ?, ?> object, RealList<CommandData, RealCommand> commands) throws HousemateException {
        for(Map.Entry<Method, Command> commandMethod : getAnnotatedMethods(object.getClass(), Command.class).entrySet())
            commands.add(new CommandImpl(object.getLog(), commandMethod.getValue().id(),
                    commandMethod.getValue().name(), commandMethod.getValue().description(),
                    parseParameters(types, object.getLog(), commandMethod.getKey()), commandMethod.getKey(), object));
    }

    private List<RealParameter<?>> parseParameters(RealList<TypeData<?>, RealType<?, ?, ?>> types, Log log, Method method) throws HousemateException {
        List<RealParameter<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < parameterAnnotations.length; a++) {
            Parameter parameterAnnotation = getAnnotation(parameterAnnotations[a], Parameter.class);
            if(parameterAnnotation == null)
                throw new HousemateException("Parameter " + a + " of command method " + method.getName()
                        + " is not annotated with " + Parameter.class.getName());
            if(types.get(parameterAnnotation.typeId()) == null)
                throw new HousemateException(parameterAnnotation.typeId() + " type does not exist");
            result.add(new RealParameter(log, parameterAnnotation.id(), parameterAnnotation.name(),
                    parameterAnnotation.description(), types.get(parameterAnnotation.typeId())));
        }
        return result;
    }

    private void parseProperties(RealList<TypeData<?>, RealType<?, ?, ?>> types, RealObject<?, ?, ?, ?> object, RealList<PropertyData, RealProperty<?>> properties) throws HousemateException {
        for(Map.Entry<Field, Property> propertyField : getAnnotatedFields(object.getClass(), Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(object);
            } catch(IllegalAccessException e) {
                log.w("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(types.get(propertyField.getValue().typeId()) == null)
                throw new HousemateException(propertyField.getValue().typeId() + " type does not exist");
            properties.add(new PropertyImpl(object.getLog(), propertyField.getValue().id(),
                    propertyField.getValue().name(), propertyField.getValue().description(),
                    (RealType<?, ?, Object>) types.get(propertyField.getValue().typeId()), value,
                    propertyField.getKey(), object));
        }
    }

    private void parseFeatures(RealDevice device, Class<? extends RealDevice> deviceClass) throws HousemateException {
        for(Class<?> interfaceClass : deviceClass.getInterfaces())
            if(RealFeature.class.isAssignableFrom(interfaceClass))
                addFeatureId(device, interfaceClass);
        if(RealDevice.class.isAssignableFrom(deviceClass.getSuperclass()))
            parseFeatures(device, (Class<? extends RealDevice>) deviceClass.getSuperclass());
    }

    private void addFeatureId(RealDevice device, Class<?> featureClass) throws HousemateException {
        Id id = featureClass.getAnnotation(Id.class);
        if(id != null)
            device.getData().getFeatureIds().add(id.value());
        else {
            try {
                Field field = featureClass.getField("ID");
                Object value = field.get(null);
                if(value instanceof String)
                    device.getData().getFeatureIds().add((String)value);
                else
                    throw new HousemateException("Could not get id for feature class. ID field is not of type string " + featureClass.getName());
            } catch(NoSuchFieldException e) {
                throw new HousemateException("Could not get id for feature class " + featureClass.getName(), e);
            } catch(IllegalAccessException e) {
                throw new HousemateException("Could not get id for feature class " + featureClass.getName(), e);
            }
        }
    }

    private void parseValues(RealList<TypeData<?>, RealType<?, ?, ?>> types, RealObject<?, ?, ?, ?> object, RealList<ValueData, RealValue<?>> values) throws HousemateException {
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
                if(types.get(valueMethod.getValue().typeId()) == null)
                    throw new HousemateException(valueMethod.getValue().typeId() + " type does not exist");
                RealValue<Object> value = new RealValue<Object>(object.getLog(), valueMethod.getValue().id(),
                        valueMethod.getValue().name(), valueMethod.getValue().description(),
                        (RealType<?,?,Object>) types.get(valueMethod.getValue().typeId()), (List)null);
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
        for(Class<?> interfaceClass : objectClass.getInterfaces())
            getAnnotatedMethods(interfaceClass, annotationClass, methods);
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
