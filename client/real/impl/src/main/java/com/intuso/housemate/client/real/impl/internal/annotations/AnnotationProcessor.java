package com.intuso.housemate.client.real.impl.internal.annotations;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.annotations.*;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.FeatureData;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.utilities.log.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * Processor of annotated devices etc
 */
public class AnnotationProcessor {

    private final Log log;
    private final RealList<RealType<?>> types;
    private final MethodCommand.Factory commandFactory;
    private final RealParameter.Factory parameterFactory;
    private final FieldProperty.Factory fieldPropertyFactory;
    private final MethodProperty.Factory methodPropertyFactory;
    private final RealValue.Factory valueFactory;
    private final RealFeature.Factory featureFactory;

    @Inject
    public AnnotationProcessor(Log log, RealList<RealType<?>> types, MethodCommand.Factory commandFactory, RealParameter.Factory parameterFactory, FieldProperty.Factory fieldPropertyFactory, MethodProperty.Factory methodPropertyFactory, RealValue.Factory valueFactory, RealFeature.Factory featureFactory) {
        this.log = log;
        this.types = types;
        this.commandFactory = commandFactory;
        this.parameterFactory = parameterFactory;
        this.fieldPropertyFactory = fieldPropertyFactory;
        this.methodPropertyFactory = methodPropertyFactory;
        this.valueFactory = valueFactory;
        this.featureFactory = featureFactory;
    }

    public Iterable<RealCommand> findCommands(Object object) {
        return findCommands(object, object.getClass());
    }

    public Iterable<RealCommand> findCommands(Object object, Class<?> clazz) {
        return Iterables.concat(findInternalCommands(object, clazz), findV1_0Commands(object, clazz));
    }

    private Iterable<RealCommand> findInternalCommands(Object object, Class<?> clazz) {
        List<RealCommand> commands = Lists.newArrayList();
        for(Map.Entry<Method, Command> commandMethod : getAnnotatedMethods(clazz, Command.class).entrySet()){
            TypeInfo typeInfo = commandMethod.getKey().getAnnotation(TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateCommsException("No " + TypeInfo.class.getName() + " on command method " + commandMethod.getKey().getName() + " of class " + clazz);
            commands.add(commandFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    parseInternalParameters(clazz, commandMethod.getKey()), commandMethod.getKey(), object));
        }
        return commands;
    }

    private List<RealParameter<?>> parseInternalParameters(Class<?> clazz, Method method) {
        List<RealParameter<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < parameterAnnotations.length; a++) {
            Parameter parameterAnnotation = getAnnotation(parameterAnnotations[a], Parameter.class);
            if(parameterAnnotation == null)
                throw new HousemateCommsException("Parameter " + a + " of command method " + method.getName()
                        + " is not annotated with " + Parameter.class.getName());
            if(types.get(parameterAnnotation.value()) == null)
                throw new HousemateCommsException(parameterAnnotation.value() + " type does not exist");
            TypeInfo typeInfo = getAnnotation(parameterAnnotations[a], TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateCommsException("No " + TypeInfo.class.getName() + " on parameter " + a + " of command method " + method.getName() + " of class " + clazz);
            result.add(parameterFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    types.get(parameterAnnotation.value())));
        }
        return result;
    }

    private Iterable<RealCommand> findV1_0Commands(Object object, Class<?> clazz) {
        List<RealCommand> commands = Lists.newArrayList();
        for(Map.Entry<Method, com.intuso.housemate.client.v1_0.real.api.annotations.Command> commandMethod : getAnnotatedMethods(clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Command.class).entrySet()) {
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = commandMethod.getKey().getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateCommsException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on command method " + commandMethod.getKey().getName() + " of class " + clazz);
            commands.add(commandFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    parseV1_0Parameters(clazz, commandMethod.getKey()), commandMethod.getKey(), object));
        }
        return commands;
    }

    private List<RealParameter<?>> parseV1_0Parameters(Class<?> clazz, Method method) {
        List<RealParameter<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < parameterAnnotations.length; a++) {
            com.intuso.housemate.client.v1_0.real.api.annotations.Parameter parameterAnnotation = getAnnotation(parameterAnnotations[a], com.intuso.housemate.client.v1_0.real.api.annotations.Parameter.class);
            if(parameterAnnotation == null)
                throw new HousemateCommsException("Parameter " + a + " of command method " + method.getName()
                        + " is not annotated with " + com.intuso.housemate.client.v1_0.real.api.annotations.Parameter.class.getName());
            if(types.get(parameterAnnotation.value()) == null)
                throw new HousemateCommsException(parameterAnnotation.value() + " type does not exist");
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = getAnnotation(parameterAnnotations[a], com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateCommsException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on parameter " + a + " of command method " + method.getName() + " of class " + clazz);
            result.add(parameterFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    types.get(parameterAnnotation.value())));
        }
        return result;
    }

    public Iterable<RealProperty<?>> findProperties(Object object) {
        return findProperties(object, object.getClass());
    }

    public Iterable<RealProperty<?>> findProperties(Object object, Class<?> clazz) {
        return Iterables.concat(findInternalProperties(object, clazz), findV1_0Properties(object, clazz));
    }

    private Iterable<RealProperty<?>> findInternalProperties(Object object, Class<?> clazz) {
        List<RealProperty<?>> properties = Lists.newArrayList();
        for(Map.Entry<Field, Property> propertyField : getAnnotatedFields(clazz, Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(object);
            } catch(IllegalAccessException e) {
                log.w("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(types.get(propertyField.getValue().value()) == null)
                throw new HousemateCommsException(propertyField.getValue().value() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyField.getValue().value());
            if(value == null && propertyField.getValue().initialValue().length() > 0)
                value = type.deserialise(new TypeInstance(propertyField.getValue().initialValue()));
            TypeInfo typeInfo = propertyField.getKey().getAnnotation(TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateCommsException("No " + TypeInfo.class.getName() + " on property field" + propertyField.getKey().getName() + " of class " + clazz);
            properties.add(fieldPropertyFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    type, value, propertyField.getKey(), object));
        }
        for(Map.Entry<Method, Property> propertyMethod : getAnnotatedMethods(clazz, Property.class).entrySet()) {
            if(propertyMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateCommsException(propertyMethod.getKey().getName() + " must take a single argument");
            if(types.get(propertyMethod.getValue().value()) == null)
                throw new HousemateCommsException(propertyMethod.getValue().value() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyMethod.getValue().value());
            Object value = getInitialValue(object, clazz, propertyMethod.getValue(), type, propertyMethod.getKey().getName());
            TypeInfo typeInfo = propertyMethod.getKey().getAnnotation(TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateCommsException("No " + TypeInfo.class.getName() + " on property field" + propertyMethod.getKey().getName() + " of class " + clazz);
            properties.add(methodPropertyFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    type, value, propertyMethod.getKey(), object));
        }
        return properties;
    }

    private Object getInitialValue(Object object, Class<?> clazz, Property property, RealTypeImpl<?, ?, Object> type, String methodName) {
        if(property.initialValue().length() > 0)
            return type.deserialise(new TypeInstance(property.initialValue()));
        else if(methodName.startsWith("set")) {
            String fieldName = methodName.substring(3);
            String getterName = "get" + fieldName;
            try {
                Method getter = clazz.getMethod(getterName);
                return getter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                log.e("Problem getting property initial value using getter " + getterName + " of " + clazz.getName());
            }
            String isGetterName = "is" + fieldName;
            try {
                Method isGetter = clazz.getMethod(isGetterName);
                return isGetter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                log.e("Problem getting property initial value using isGetter " + isGetterName + " of " + clazz.getName());
            }
        }
        log.w("No initial value or getter found for " + Property.class.getSimpleName() + " method " + methodName + " of " + clazz.getName());
        return null;
    }

    private Iterable<RealProperty<?>> findV1_0Properties(Object object, Class<?> clazz) {
        List<RealProperty<?>> properties = Lists.newArrayList();
        for(Map.Entry<Field, com.intuso.housemate.client.v1_0.real.api.annotations.Property> propertyField : getAnnotatedFields(clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(object);
            } catch(IllegalAccessException e) {
                log.w("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(types.get(propertyField.getValue().value()) == null)
                throw new HousemateCommsException(propertyField.getValue().value() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyField.getValue().value());
            if(value == null && propertyField.getValue().initialValue().length() > 0)
                value = type.deserialise(new TypeInstance(propertyField.getValue().initialValue()));
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = propertyField.getKey().getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateCommsException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on property field" + propertyField.getKey().getName() + " of class " + clazz);
            properties.add(fieldPropertyFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    type, value, propertyField.getKey(), object));
        }
        for(Map.Entry<Method, com.intuso.housemate.client.v1_0.real.api.annotations.Property> propertyMethod : getAnnotatedMethods(clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Property.class).entrySet()) {
            if(propertyMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateCommsException(propertyMethod.getKey().getName() + " must take a single argument");
            if(types.get(propertyMethod.getValue().value()) == null)
                throw new HousemateCommsException(propertyMethod.getValue().value() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyMethod.getValue().value());
            Object value = getV1_0Value(object, clazz, propertyMethod.getValue(), type, propertyMethod.getKey().getName());
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = propertyMethod.getKey().getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateCommsException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on property field" + propertyMethod.getKey().getName() + " of class " + clazz);
            properties.add(methodPropertyFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    type, value, propertyMethod.getKey(), object));
        }
        return properties;
    }

    private Object getV1_0Value(Object object, Class<?> clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Property property, RealTypeImpl<?, ?, Object> type, String methodName) {
        if(property.initialValue().length() > 0)
            return type.deserialise(new TypeInstance(property.initialValue()));
        else if(methodName.startsWith("set")) {
            String fieldName = methodName.substring(3);
            String getterName = "get" + fieldName;
            try {
                Method getter = clazz.getMethod(getterName);
                return getter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                log.e("Problem getting property initial value using getter " + getterName + " of " + clazz.getName());
            }
            String isGetterName = "is" + fieldName;
            try {
                Method isGetter = clazz.getMethod(isGetterName);
                return isGetter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                log.e("Problem getting property initial value using isGetter " + isGetterName + " of " + clazz.getName());
            }
        }
        log.w("No initial value or getter found for " + Property.class.getSimpleName() + " method " + methodName + " of " + clazz.getName());
        return null;
    }

    public Iterable<RealFeature> findFeatures(Object object) {
        return findFeatures(object, object.getClass());
    }

    private Iterable<RealFeature> findFeatures(Object object, Class<?> clazz) {
        List<RealFeature> features = Lists.newArrayList();
        findFeatures(object, clazz, features);
        return features;
    }
    
    private void findFeatures(Object object, Class<?> clazz, List<RealFeature> features) {
        for(Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (Feature.class.equals(annotation.annotationType())) {
                features.add(processInternalFeatureClass(object, clazz));
                return;
            } else if(com.intuso.housemate.client.v1_0.real.api.annotations.Feature.class.equals(annotation.annotationType())) {
                features.add(processV1_0FeatureClass(object, clazz));
                return;
            }
        }
        for(Class<?> interfaceClass : clazz.getInterfaces())
            findFeatures(object, interfaceClass, features);
        if(clazz.getSuperclass() != null)
            findFeatures(object, clazz.getSuperclass(), features);
    }

    private RealFeature processInternalFeatureClass(Object object, Class<?> clazz) {
        TypeInfo typeInfo = clazz.getAnnotation(TypeInfo.class);
        if(typeInfo == null)
            throw new HousemateCommsException("No " + TypeInfo.class.getName() + " on feature class " + clazz.getName());
        RealFeature feature = featureFactory.create(new FeatureData(typeInfo.id(), typeInfo.name(), typeInfo.description()));
        for(RealCommand command : findCommands(object, clazz))
            feature.getCommands().add(command);
        for(RealValue<?> value : findValues(object, clazz))
            feature.getValues().add(value);
        return feature;
    }

    private RealFeature processV1_0FeatureClass(Object object, Class<?> clazz) {
        com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = clazz.getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
        if(typeInfo == null)
            throw new HousemateCommsException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on feature class " + clazz.getName());
        RealFeature feature = featureFactory.create(new FeatureData(typeInfo.id(), typeInfo.name(), typeInfo.description()));
        for(RealCommand command : findCommands(object, clazz))
            feature.getCommands().add(command);
        for(RealValue<?> value : findValues(object, clazz))
            feature.getValues().add(value);
        return feature;
    }

    private List<RealValue<?>> findValues(Object object, Class<?> clazz) {
        for(Class<?> interfaceClass : clazz.getClasses()) {
            if (interfaceClass.getAnnotation(Values.class) != null)
                return findObjectValues(object, object.getClass(), interfaceClass);
            else if(interfaceClass.getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.Values.class) != null)
                return findObjectValues(object, object.getClass(), interfaceClass);
        }
        return Lists.newArrayList();
    }

    private List<RealValue<?>> findObjectValues(Object object, Class<?> lookInClass, Class<?> valuesClass) {
        for(Field field : lookInClass.getDeclaredFields()) {
            if(valuesClass.isAssignableFrom(field.getType())) {
                return getValuesImpl(object, field, valuesClass);
            }
        }
        if(lookInClass.getSuperclass() != null)
            return findObjectValues(object, lookInClass.getSuperclass(), valuesClass);
        return Lists.newArrayList();
    }

    private List<RealValue<?>> getValuesImpl(Object object, Field field, Class<?> clazz) {
        List<RealValue<?>> values = Lists.newArrayList();
        Map<Method, RealValue<?>> valuesFunctions = Maps.newHashMap();
        InvocationHandler invocationHandler = new ValuesInvocationHandler(valuesFunctions);
        Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
        try {
            field.setAccessible(true);
            field.set(object, instance);
        } catch(IllegalAccessException e) {
            throw new HousemateCommsException("Failed to assign proxy instance to field " + field.getName() + " of class " + object.getClass().getName(), e);
        }
        findInternalValues(values, valuesFunctions, clazz);
        findV1_0Values(values, valuesFunctions, clazz);
        return values;
    }

    private void findInternalValues(List<RealValue<?>> values, Map<Method, RealValue<?>> valuesFunctions, Class<?> clazz) {
        for(Map.Entry<Method, Value> valueMethod : getAnnotatedMethods(clazz, Value.class).entrySet()) {
            if(types.get(valueMethod.getValue().value()) == null)
                throw new HousemateCommsException(valueMethod.getValue().value() + " type does not exist");
            TypeInfo typeInfo = valueMethod.getKey().getAnnotation(TypeInfo.class);
            RealValue<?> value = valueFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    types.get(valueMethod.getValue().value()), null);
            valuesFunctions.put(valueMethod.getKey(), value);
            values.add(value);
        }
    }

    private void findV1_0Values(List<RealValue<?>> values, Map<Method, RealValue<?>> valuesFunctions, Class<?> clazz) {
        for(Map.Entry<Method, com.intuso.housemate.client.v1_0.real.api.annotations.Value> valueMethod : getAnnotatedMethods(clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Value.class).entrySet()) {
            if(types.get(valueMethod.getValue().value()) == null)
                throw new HousemateCommsException(valueMethod.getValue().value() + " type does not exist");
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = valueMethod.getKey().getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            RealValue<?> value = valueFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    types.get(valueMethod.getValue().value()), null);
            valuesFunctions.put(valueMethod.getKey(), value);
            values.add(value);
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
