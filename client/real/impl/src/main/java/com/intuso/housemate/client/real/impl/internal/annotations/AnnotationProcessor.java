package com.intuso.housemate.client.real.impl.internal.annotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.impl.internal.*;
import com.intuso.housemate.client.real.impl.internal.type.RegisteredTypes;
import com.intuso.housemate.plugin.api.internal.annotations.*;
import com.intuso.housemate.plugin.api.internal.annotations.Parameter;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * Processor of annotated devices etc
 */
public class AnnotationProcessor {

    private final RegisteredTypes types;
    private final RealCommandImpl.Factory commandFactory;
    private final RealFeatureImpl.Factory featureFactory;

    @Inject
    public AnnotationProcessor(RegisteredTypes types,
                               RealCommandImpl.Factory commandFactory,
                               RealFeatureImpl.Factory featureFactory) {
        this.types = types;
        this.commandFactory = commandFactory;
        this.featureFactory = featureFactory;
    }

    public Iterable<RealCommandImpl> findCommands(Logger logger, Object object) {
        return findCommands(logger, object, object.getClass());
    }

    public Iterable<RealCommandImpl> findCommands(Logger logger, Object object, Class<?> clazz) {
        return findV1_0Commands(logger, object, clazz);
    }

    private Iterable<RealCommandImpl> findV1_0Commands(Logger logger, Object object, Class<?> clazz) {
        List<RealCommandImpl> commands = Lists.newArrayList();
        for(Map.Entry<Method, Command> commandMethod : getAnnotatedMethods(clazz, Command.class).entrySet()) {
            Id id = commandMethod.getKey().getAnnotation(Id.class);
            if(id == null)
                throw new HousemateException("No " + Id.class.getName() + " on command method " + commandMethod.getKey().getName() + " of class " + clazz);
            List<RealParameterImpl<?>> parameters = parseV1_0Parameters(logger, clazz, commandMethod.getKey());
            commands.add(commandFactory.create(ChildUtil.logger(logger, id.value()),
                    id.value(),
                    id.name(),
                    id.description(),
                    new MethodCommandPerformer(commandMethod.getKey(), object, parameters),
                    parameters));
        }
        return commands;
    }

    private List<RealParameterImpl<?>> parseV1_0Parameters(Logger logger, Class<?> clazz, Method method) {
        List<RealParameterImpl<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < parameterAnnotations.length; a++) {
            Parameter parameterAnnotation = getAnnotation(parameterAnnotations[a], Parameter.class);
            if(parameterAnnotation == null)
                throw new HousemateException("Parameter " + a + " of command method " + method.getName()
                        + " is not annotated with " + Parameter.class.getName());
            if(!types.exists(parameterAnnotation.value()))
                throw new HousemateException(parameterAnnotation.value() + " type does not exist");
            Id id = getAnnotation(parameterAnnotations[a], Id.class);
            if(id == null)
                throw new HousemateException("No " + Id.class.getName() + " on parameter " + a + " of command method " + method.getName() + " of class " + clazz);
            result.add(types.createParameter(parameterAnnotation.value(),
                    ChildUtil.logger(logger, id.value()),
                    id.value(),
                    id.name(),
                    id.description(),
                    parameterAnnotation.minValues(),
                    parameterAnnotation.maxValues()));
        }
        return result;
    }

    public Iterable<RealPropertyImpl<?>> findProperties(Logger logger, Object object) {
        return findProperties(logger, object, object.getClass());
    }

    public Iterable<RealPropertyImpl<?>> findProperties(Logger logger, Object object, Class<?> clazz) {
        return findV1_0Properties(logger, object, clazz);
    }

    private Iterable<RealPropertyImpl<?>> findV1_0Properties(Logger logger, Object object, Class<?> clazz) {
        List<RealPropertyImpl<?>> properties = Lists.newArrayList();
        for(Map.Entry<Field, Property> propertyField : getAnnotatedFields(clazz, Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(object);
            } catch(IllegalAccessException e) {
                logger.warn("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(!types.exists(propertyField.getValue().value()))
                throw new HousemateException(propertyField.getValue().value() + " type does not exist");
            Id id = propertyField.getKey().getAnnotation(Id.class);
            if(id == null)
                throw new HousemateException("No " + Id.class.getName() + " on property field" + propertyField.getKey().getName() + " of class " + clazz);
            RealPropertyImpl<Object> property = types.createProperty(propertyField.getValue().value(), ChildUtil.logger(logger, id.value()),
                    id.value(),
                    id.name(),
                    id.description(),
                    propertyField.getValue().minValues(),
                    propertyField.getValue().maxValues(),
                    Lists.newArrayList(value));
            property.addObjectListener(new FieldPropertySetter<>(ChildUtil.logger(logger, id.value()), propertyField.getKey(), object));
            properties.add(property);
        }
        for(Map.Entry<Method, Property> propertyMethod : getAnnotatedMethods(clazz, Property.class).entrySet()) {
            if(propertyMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateException(propertyMethod.getKey().getName() + " must take a single argument");
            if(!types.exists(propertyMethod.getValue().value()))
                throw new HousemateException(propertyMethod.getValue().value() + " type does not exist");
            Id id = propertyMethod.getKey().getAnnotation(Id.class);
            if(id == null)
                throw new HousemateException("No " + Id.class.getName() + " on property field" + propertyMethod.getKey().getName() + " of class " + clazz);
            Object value = getV1_0InitialValue(logger, object, clazz, propertyMethod.getKey().getName());
            RealPropertyImpl<Object> property = types.createProperty(propertyMethod.getValue().value(),
                    ChildUtil.logger(logger, id.value()),
                    id.value(),
                    id.name(),
                    id.description(),
                    propertyMethod.getValue().minValues(),
                    propertyMethod.getValue().maxValues(),
                    Lists.newArrayList(value));
            property.addObjectListener(new MethodPropertySetter(ChildUtil.logger(logger, id.value()), propertyMethod.getKey(), object));
            properties.add(property);
        }
        return properties;
    }

    private Object getV1_0InitialValue(Logger logger, Object object, Class<?> clazz, String methodName) {
        if(methodName.startsWith("set")) {
            String fieldName = methodName.substring(3);
            String getterName = "get" + fieldName;
            try {
                Method getter = clazz.getMethod(getterName);
                return getter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                logger.error("Problem getting property initial value using getter " + getterName + " of " + clazz.getName());
            }
            String isGetterName = "is" + fieldName;
            try {
                Method isGetter = clazz.getMethod(isGetterName);
                return isGetter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                logger.error("Problem getting property initial value using isGetter " + isGetterName + " of " + clazz.getName());
            }
        }
        logger.warn("No equivalent getter found for initial value for " + Property.class.getSimpleName() + " method " + methodName + " of " + clazz.getName());
        return null;
    }

    public List<RealValueImpl<?>> findValues(Logger logger, Object object) {
        return findValues(logger, object, object.getClass());
    }

    public List<RealValueImpl<?>> findValues(Logger logger, Object object, Class<?> clazz) {
        for(Class<?> interfaceClass : clazz.getClasses()) {
            if (interfaceClass.getAnnotation(Values.class) != null)
                return findV1_0ObjectValues(logger, object, object.getClass(), interfaceClass);
        }
        return Lists.newArrayList();
    }

    private List<RealValueImpl<?>> findV1_0ObjectValues(Logger logger, Object object, Class<?> lookInClass, Class<?> valuesClass) {
        for(Field field : lookInClass.getDeclaredFields()) {
            if(valuesClass.isAssignableFrom(field.getType())) {
                return getV1_0ValuesImpl(logger, object, field, valuesClass);
            }
        }
        if(lookInClass.getSuperclass() != null)
            return findV1_0ObjectValues(logger, object, lookInClass.getSuperclass(), valuesClass);
        return Lists.newArrayList();
    }

    private List<RealValueImpl<?>> getV1_0ValuesImpl(Logger logger, Object object, Field field, Class<?> clazz) {
        List<RealValueImpl<?>> values = Lists.newArrayList();
        Map<Method, RealValueImpl<?>> valuesFunctions = Maps.newHashMap();
        InvocationHandler invocationHandler = new ValuesInvocationHandler(valuesFunctions);
        Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
        try {
            field.setAccessible(true);
            field.set(object, instance);
        } catch(IllegalAccessException e) {
            throw new HousemateException("Failed to assign proxy instance to field " + field.getName() + " of class " + object.getClass().getName(), e);
        }
        findV1_0Values(logger, values, valuesFunctions, clazz);
        return values;
    }

    private void findV1_0Values(Logger logger, List<RealValueImpl<?>> values, Map<Method, RealValueImpl<?>> valuesFunctions, Class<?> clazz) {
        for(Map.Entry<Method, Value> valueMethod : getAnnotatedMethods(clazz, Value.class).entrySet()) {
            if(!types.exists(valueMethod.getValue().value()))
                throw new HousemateException(valueMethod.getValue().value() + " type does not exist");
            Id id = valueMethod.getKey().getAnnotation(Id.class);
            RealValueImpl<?> value = types.createValue(valueMethod.getValue().value(),
                    ChildUtil.logger(logger, id.value()),
                    id.value(),
                    id.name(),
                    id.description(),
                    valueMethod.getValue().minValues(),
                    valueMethod.getValue().maxValues(),
                    Lists.newArrayList());
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
