package com.intuso.housemate.client.real.impl.internal.annotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.client.real.api.internal.annotations.*;
import com.intuso.housemate.client.real.api.internal.device.feature.RealFeature;
import com.intuso.housemate.client.real.impl.internal.*;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.utilities.listener.ListenersFactory;
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
    private final ListenersFactory listenersFactory;
    private final RealList<RealType<?>> types;

    @Inject
    public AnnotationProcessor(Log log, ListenersFactory listenersFactory, RealList<RealType<?>> types) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.types = types;
    }

    public void process(RealObject<?, ?, ?, ?> object, Object driver) {
        if(object instanceof com.intuso.housemate.object.v1_0.api.Command.Container)
            parseCommands(object, driver, ((com.intuso.housemate.object.api.internal.Command.Container<RealListImpl<CommandData, RealCommandImpl>>)object).getCommands());
        if(object instanceof com.intuso.housemate.object.v1_0.api.Property.Container)
            parseProperties(object, driver, ((com.intuso.housemate.object.api.internal.Property.Container<RealListImpl<PropertyData, RealPropertyImpl<?>>>) object).getProperties());
        if(object instanceof com.intuso.housemate.object.v1_0.api.Value.Container)
            parseValues(object, driver, ((com.intuso.housemate.object.api.internal.Value.Container<RealListImpl<ValueData, RealValueImpl<?>>>) object).getValues());
        if(object instanceof RealDeviceImpl)
            parseFeatures((RealDeviceImpl) object, object.getClass());
    }
    
    private void parseCommands(RealObject<?, ?, ?, ?> object, Object driver, RealListImpl<CommandData, RealCommandImpl> commands) {
        for(Map.Entry<Method, Command> commandMethod : getAnnotatedMethods(driver.getClass(), Command.class).entrySet())
            commands.add(new CommandImpl(object.getLog(), listenersFactory,
                    commandMethod.getValue().id(), commandMethod.getValue().name(),
                    commandMethod.getValue().description(), parseParameters(object.getLog(), commandMethod.getKey()), commandMethod.getKey(), object));
    }

    private List<RealParameterImpl<?>> parseParameters(Log log, Method method) {
        List<RealParameterImpl<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < parameterAnnotations.length; a++) {
            Parameter parameterAnnotation = getAnnotation(parameterAnnotations[a], Parameter.class);
            if(parameterAnnotation == null)
                throw new HousemateCommsException("Parameter " + a + " of command method " + method.getName()
                        + " is not annotated with " + Parameter.class.getName());
            if(types.get(parameterAnnotation.typeId()) == null)
                throw new HousemateCommsException(parameterAnnotation.typeId() + " type does not exist");
            result.add(new RealParameterImpl(log, listenersFactory, parameterAnnotation.id(),
                    parameterAnnotation.name(), parameterAnnotation.description(), types.get(parameterAnnotation.typeId())));
        }
        return result;
    }

    private void parseProperties(RealObject<?, ?, ?, ?> object, Object driver, RealListImpl<PropertyData, RealPropertyImpl<?>> properties) {
        for(Map.Entry<Field, Property> propertyField : getAnnotatedFields(driver.getClass(), Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(object);
            } catch(IllegalAccessException e) {
                log.w("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(types.get(propertyField.getValue().typeId()) == null)
                throw new HousemateCommsException(propertyField.getValue().typeId() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyField.getValue().typeId());
            if(value == null && propertyField.getValue().initialValue().length() > 0)
                value = type.deserialise(new TypeInstance(propertyField.getValue().initialValue()));
            properties.add(new FieldPropertyImpl(object.getLog(),
                    listenersFactory,
                    propertyField.getValue().id(),
                    propertyField.getValue().name(),
                    propertyField.getValue().description(),
                    type,
                    value,
                    propertyField.getKey(),
                    object));
        }
        for(Map.Entry<Method, Property> propertyMethod : getAnnotatedMethods(driver.getClass(), Property.class).entrySet()) {
            if(propertyMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateCommsException(propertyMethod.getKey().getName() + " must take a single argument");
            if(types.get(propertyMethod.getValue().typeId()) == null)
                throw new HousemateCommsException(propertyMethod.getValue().typeId() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyMethod.getValue().typeId());
            Object value = getInitialValue(object, propertyMethod.getValue(), type, propertyMethod.getKey().getName());
            properties.add(new MethodPropertyImpl(object.getLog(),
                    listenersFactory,
                    propertyMethod.getValue().id(),
                    propertyMethod.getValue().name(),
                    propertyMethod.getValue().description(),
                    type,
                    value,
                    propertyMethod.getKey(),
                    object));
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
                log.e("Problem getting proeprty initial value using getter " + getterName + " of " + object.getClass().getName());
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

    private void parseFeatures(RealDeviceImpl<?> device, Class<?> driverClass) {
        for(Class<?> interfaceClass : driverClass.getInterfaces())
            if(RealFeature.class.isAssignableFrom(interfaceClass))
                addFeatureId(device, interfaceClass);
        if(RealDevice.class.isAssignableFrom(driverClass.getSuperclass()))
            parseFeatures(device, driverClass.getSuperclass());
    }

    private void addFeatureId(RealDeviceImpl<?> device, Class<?> featureClass) {
        FeatureId featureId = featureClass.getAnnotation(FeatureId.class);
        if(featureId != null) {
            List<String> featureIds = device.getFeatureIds() == null ? Lists.<String>newArrayList() : Lists.newArrayList(device.getFeatureIds());
            featureIds.add(featureId.value());
            device.getData().setFeatureIds(featureIds);
        } else {
            try {
                Field field = featureClass.getField("ID");
                Object value = field.get(null);
                if(value instanceof String) {
                    List<String> featureIds = device.getFeatureIds() == null ? Lists.<String>newArrayList() : Lists.newArrayList(device.getFeatureIds());
                    featureIds.add(featureId.value());
                    device.getData().setFeatureIds(featureIds);
                } else
                    throw new HousemateCommsException("Could not get id for feature class. ID field is not of type string " + featureClass.getName());
            } catch(NoSuchFieldException e) {
                throw new HousemateCommsException("Could not get id for feature class " + featureClass.getName(), e);
            } catch(IllegalAccessException e) {
                throw new HousemateCommsException("Could not get id for feature class " + featureClass.getName(), e);
            }
        }
    }

    private void parseValues(RealObject<?, ?, ?, ?> object, Object driver, RealListImpl<ValueData, RealValueImpl<?>> values) {
        for(Map.Entry<Field, Values> valuesField : getAnnotatedFields(driver.getClass(), Values.class).entrySet()) {
            Map<Method, RealValue<Object>> valuesFunctions = Maps.newHashMap();
            InvocationHandler invocationHandler = new ValuesInvocationHandler(valuesFunctions);
            Object instance = Proxy.newProxyInstance(valuesField.getKey().getType().getClassLoader(),
                    new Class<?>[] {valuesField.getKey().getType()},
                    invocationHandler);
            try {
                valuesField.getKey().set(object, instance);
            } catch(IllegalAccessException e) {
                throw new HousemateCommsException("Failed to assign proxy instance to " + valuesField.getKey().getName());
            }
            for(Map.Entry<Method, Value> valueMethod : getAnnotatedMethods(valuesField.getKey().getType(), Value.class).entrySet()) {
                if(types.get(valueMethod.getValue().typeId()) == null)
                    throw new HousemateCommsException(valueMethod.getValue().typeId() + " type does not exist");
                RealValueImpl<Object> value = new RealValueImpl<>(object.getLog(), listenersFactory,
                        valueMethod.getValue().id(), valueMethod.getValue().name(),
                        valueMethod.getValue().description(), (RealTypeImpl<?,?,Object>) types.get(valueMethod.getValue().typeId()), (List)null);
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
