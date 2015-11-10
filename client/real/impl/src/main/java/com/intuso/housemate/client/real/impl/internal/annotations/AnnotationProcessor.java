package com.intuso.housemate.client.real.impl.internal.annotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.annotations.*;
import com.intuso.housemate.client.real.impl.internal.RealDeviceImpl;
import com.intuso.housemate.client.real.impl.internal.RealObject;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
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

    @Inject
    public AnnotationProcessor(Log log, RealList<RealType<?>> types, MethodCommand.Factory commandFactory, RealParameter.Factory parameterFactory, FieldProperty.Factory fieldPropertyFactory, MethodProperty.Factory methodPropertyFactory, RealValue.Factory valueFactory) {
        this.log = log;
        this.types = types;
        this.commandFactory = commandFactory;
        this.parameterFactory = parameterFactory;
        this.fieldPropertyFactory = fieldPropertyFactory;
        this.methodPropertyFactory = methodPropertyFactory;
        this.valueFactory = valueFactory;
    }

    public void process(Object driver, RealObject<?, ?, ?, ?> object) {
        if(object instanceof com.intuso.housemate.object.api.internal.Command.Container)
            parseCommands(driver, ((com.intuso.housemate.object.api.internal.Command.Container<RealList<RealCommand>>)object).getCommands());
        if(object instanceof com.intuso.housemate.object.api.internal.Property.Container)
            parseProperties(driver, ((com.intuso.housemate.object.api.internal.Property.Container<RealList<RealProperty<?>>>) object).getProperties());
        if(object instanceof com.intuso.housemate.object.api.internal.Value.Container)
            parseValues(driver, ((com.intuso.housemate.object.api.internal.Value.Container<RealList<RealValue<?>>>) object).getValues());
        if(object instanceof com.intuso.housemate.object.api.internal.Feature.Container)
            parseFeatures(driver, ((com.intuso.housemate.object.api.internal.Feature.Container<RealList<RealFeature>>)object).getFeatures());
    }

    private void parseCommands(Object driver, RealList<RealCommand> commands) {
        for(Map.Entry<Method, Command> commandMethod : getAnnotatedMethods(driver.getClass(), Command.class).entrySet())
            commands.add(commandFactory.create(commandMethod.getValue().id(), commandMethod.getValue().name(),
                    commandMethod.getValue().description(), parseParameters(commandMethod.getKey()), commandMethod.getKey(), driver));
    }

    private List<RealParameter<?>> parseParameters(Method method) {
        List<RealParameter<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < parameterAnnotations.length; a++) {
            Parameter parameterAnnotation = getAnnotation(parameterAnnotations[a], Parameter.class);
            if(parameterAnnotation == null)
                throw new HousemateCommsException("Parameter " + a + " of command method " + method.getName()
                        + " is not annotated with " + Parameter.class.getName());
            if(types.get(parameterAnnotation.typeId()) == null)
                throw new HousemateCommsException(parameterAnnotation.typeId() + " type does not exist");
            result.add(parameterFactory.create(parameterAnnotation.id(), parameterAnnotation.name(),
                    parameterAnnotation.description(), types.get(parameterAnnotation.typeId())));
        }
        return result;
    }

    private void parseProperties(Object driver, RealList<RealProperty<?>> properties) {
        for(Map.Entry<Field, Property> propertyField : getAnnotatedFields(driver.getClass(), Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(driver);
            } catch(IllegalAccessException e) {
                log.w("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(types.get(propertyField.getValue().typeId()) == null)
                throw new HousemateCommsException(propertyField.getValue().typeId() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyField.getValue().typeId());
            if(value == null && propertyField.getValue().initialValue().length() > 0)
                value = type.deserialise(new TypeInstance(propertyField.getValue().initialValue()));
            properties.add(fieldPropertyFactory.create(propertyField.getValue().id(),
                    propertyField.getValue().name(),
                    propertyField.getValue().description(),
                    type,
                    value,
                    propertyField.getKey(),
                    driver));
        }
        for(Map.Entry<Method, Property> propertyMethod : getAnnotatedMethods(driver.getClass(), Property.class).entrySet()) {
            if(propertyMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateCommsException(propertyMethod.getKey().getName() + " must take a single argument");
            if(types.get(propertyMethod.getValue().typeId()) == null)
                throw new HousemateCommsException(propertyMethod.getValue().typeId() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyMethod.getValue().typeId());
            Object value = getInitialValue(driver, propertyMethod.getValue(), type, propertyMethod.getKey().getName());
            properties.add(methodPropertyFactory.create(
                    propertyMethod.getValue().id(),
                    propertyMethod.getValue().name(),
                    propertyMethod.getValue().description(),
                    type,
                    value,
                    propertyMethod.getKey(),
                    driver));
        }
    }

    private Object getInitialValue(Object object, Property property, RealTypeImpl<?, ?, Object> type, String methodName) {
        if(property.initialValue().length() > 0)
            return type.deserialise(new TypeInstance(property.initialValue()));
        else if(methodName.startsWith("set")) {
            String fieldName = methodName.substring(3);
            String getterName = "get" + fieldName;
            try {
                Method getter = object.getClass().getMethod(getterName);
                return getter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                log.e("Problem getting property initial value using getter " + getterName + " of " + object.getClass().getName());
            }
            String isGetterName = "is" + fieldName;
            try {
                Method isGetter = object.getClass().getMethod(isGetterName);
                return isGetter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                log.e("Problem getting property initial value using isGetter " + isGetterName + " of " + object.getClass().getName());
            }
        }
        log.w("No initial value or getter found for " + Property.class.getSimpleName() + " method " + methodName + " of " + object.getClass().getName());
        return null;
    }

    private void parseFeatures(Object driver, RealList<RealFeature> features) {
        /*for(Class<?> interfaceClass : driverClass.getInterfaces())
            if(RealFeature.class.isAssignableFrom(interfaceClass))
                addFeatureId(device, interfaceClass);
        if(RealDevice.class.isAssignableFrom(driverClass.getSuperclass()))
            parseFeatures(device, driverClass.getSuperclass());*/
    }

    private void addFeatureId(RealDeviceImpl<?> device, Class<?> featureClass) {
        String featureId = null;
        FeatureId featureIdAnnotation = featureClass.getAnnotation(FeatureId.class);
        if(featureId != null)
            featureId = featureIdAnnotation.value();
        else {
            try {
                Field field = featureClass.getField("ID");
                Object value = field.get(null);
                if (value instanceof String) {
                    featureId = (String) value;
                } else
                    throw new HousemateCommsException("Could not get id for feature class. ID field is not of type string " + featureClass.getName());
            } catch (NoSuchFieldException e) {
                throw new HousemateCommsException("Could not get id for feature class " + featureClass.getName(), e);
            } catch (IllegalAccessException e) {
                throw new HousemateCommsException("Could not get id for feature class " + featureClass.getName(), e);
            }
        }
    }

    private void parseValues(Object driver, RealList<RealValue<?>> values) {
        for(Map.Entry<Field, Values> valuesField : getAnnotatedFields(driver.getClass(), Values.class).entrySet()) {
            Map<Method, RealValue<?>> valuesFunctions = Maps.newHashMap();
            InvocationHandler invocationHandler = new ValuesInvocationHandler(valuesFunctions);
            Object instance = Proxy.newProxyInstance(valuesField.getKey().getType().getClassLoader(),
                    new Class<?>[] {valuesField.getKey().getType()},
                    invocationHandler);
            try {
                valuesField.getKey().set(driver, instance);
            } catch(IllegalAccessException e) {
                throw new HousemateCommsException("Failed to assign proxy instance to " + valuesField.getKey().getName());
            }
            for(Map.Entry<Method, Value> valueMethod : getAnnotatedMethods(valuesField.getKey().getType(), Value.class).entrySet()) {
                if(types.get(valueMethod.getValue().typeId()) == null)
                    throw new HousemateCommsException(valueMethod.getValue().typeId() + " type does not exist");
                RealValue<?> value = valueFactory.create(valueMethod.getValue().id(), valueMethod.getValue().name(),
                        valueMethod.getValue().description(), types.get(valueMethod.getValue().typeId()), null);
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
