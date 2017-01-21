package com.intuso.housemate.client.real.impl.internal.annotation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.*;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.housemate.client.v1_0.api.annotation.*;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * Processor of annotated devices etc
 */
public class AnnotationParserV1_0 implements AnnotationParser {

    private final TypeRepository types;
    private final RealCommandImpl.Factory commandFactory;
    private final RealParameterImpl.Factory parameterFactory;
    private final RealPropertyImpl.Factory propertyFactory;
    private final RealValueImpl.Factory valueFactory;

    @Inject
    public AnnotationParserV1_0(TypeRepository types,
                                RealCommandImpl.Factory commandFactory, RealParameterImpl.Factory parameterFactory, RealPropertyImpl.Factory propertyFactory, RealValueImpl.Factory valueFactory) {
        this.types = types;
        this.commandFactory = commandFactory;
        this.parameterFactory = parameterFactory;
        this.propertyFactory = propertyFactory;
        this.valueFactory = valueFactory;
    }

    @Override
    public Iterable<RealCommandImpl> findCommands(Logger logger, String idPrefix, Object object) {
        return findCommands(logger, idPrefix, object, object.getClass());
    }

    private Iterable<RealCommandImpl> findCommands(Logger logger, String idPrefix, Object object, Class<?> clazz) {
        List<RealCommandImpl> commands = Lists.newArrayList();
        for(Map.Entry<Method, Command> commandMethod : getAnnotatedMethods(clazz, Command.class).entrySet()) {
            Id id = commandMethod.getKey().getAnnotation(Id.class);
            if(id == null)
                throw new HousemateException("No " + Id.class.getName() + " on command method " + commandMethod.getKey().getName() + " of class " + clazz);
            List<RealParameterImpl<?>> parameters = parseParameters(logger, clazz, commandMethod.getKey());
            commands.add(commandFactory.create(ChildUtil.logger(logger, idPrefix + id.value()),
                    idPrefix + id.value(),
                    id.name(),
                    id.description(),
                    new MethodCommandPerformer(commandMethod.getKey(), object, parameters),
                    parameters));
        }
        return commands;
    }

    private List<RealParameterImpl<?>> parseParameters(Logger logger, Class<?> clazz, Method method) {
        List<RealParameterImpl<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int p = 0; p < parameterAnnotations.length; p++) {
            Parameter parameter = getAnnotation(parameterAnnotations[p], Parameter.class);
            if(parameter == null)
                parameter = new ParameterDefaultImpl();
            Id id = getAnnotation(parameterAnnotations[p], Id.class);
            if(id == null)
                throw new HousemateException("No " + Id.class.getName() + " on parameter " + p + " of command method " + method.getName() + " of class " + clazz);
            result.add(parameterFactory.create(
                    ChildUtil.logger(logger, id.value()),
                    id.value(),
                    id.name(),
                    id.description(),
                    types.getType(new TypeSpec(method.getParameterTypes()[p], parameter.restriction())),
                    parameter.minValues(),
                    parameter.maxValues()));
        }
        return result;
    }

    @Override
    public Iterable<RealValueImpl<?>> findValues(Logger logger, String idPrefix, Object object) {
        return findValues(logger, idPrefix, object, object.getClass());
    }

    public Iterable<RealValueImpl<?>> findValues(Logger logger, String idPrefix, Object object, Class<?> clazz) {
        List<RealValueImpl<?>> values = Lists.newArrayList();
        for(Method addListenerMethod : getAnnotatedMethods(clazz, AddListener.class).keySet())
            findAddListenerMethodValues(logger, idPrefix, object, clazz, addListenerMethod, values);
        return Lists.newArrayList();
    }

    private void findAddListenerMethodValues(Logger logger, String idPrefix, Object object, Class<?> clazz, Method addListenerMethod, List<RealValueImpl<?>> values) {
        if(addListenerMethod.getParameterTypes().length != 1) {
            logger.warn("{} annotated method {} on {} should have a single parameter", AddListener.class.getName(), addListenerMethod.getName(), clazz.getName());
            return;
        }
        Map<Method, RealValueImpl<?>> valuesFunctions = Maps.newHashMap();
        for(Map.Entry<Method, Value> valueMethod : getAnnotatedMethods(addListenerMethod.getParameterTypes()[0], Value.class).entrySet()) {
            Id id = valueMethod.getKey().getAnnotation(Id.class);
            if(valueMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateException(clazz.getName() + " value method should have a single argument");
            RealValueImpl<?> value = valueFactory.create(
                    ChildUtil.logger(logger, idPrefix + id.value()),
                    idPrefix + id.value(),
                    id.name(),
                    id.description(),
                    types.getType(new TypeSpec(valueMethod.getKey().getParameterTypes()[0], valueMethod.getValue().restriction())),
                    valueMethod.getValue().minValues(),
                    valueMethod.getValue().maxValues(),
                    Lists.newArrayList());
            valuesFunctions.put(valueMethod.getKey(), value);
            values.add(value);
        }
        Object listener = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{addListenerMethod.getParameterTypes()[0]}, new ValuesInvocationHandler(valuesFunctions));
        try {
            addListenerMethod.invoke(object, listener);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Failed to invoke {} method {} on {}", AddListener.class.getName(), addListenerMethod.getName(), clazz.getName(), e);
        }
    }

    @Override
    public Iterable<RealPropertyImpl<?>> findProperties(Logger logger, String idPrefix, Object object) {
        return findProperties(logger, idPrefix, object, object.getClass());
    }

    private Iterable<RealPropertyImpl<?>> findProperties(Logger logger, String idPrefix, Object object, Class<?> clazz) {
        List<RealPropertyImpl<?>> properties = Lists.newArrayList();
        for(Map.Entry<Field, Property> propertyField : getAnnotatedFields(clazz, Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(object);
            } catch(IllegalAccessException e) {
                logger.warn("Failed to get initial value of annotated property field {}", propertyField.getKey().getName());
            }
            Id id = propertyField.getKey().getAnnotation(Id.class);
            if(id == null)
                throw new HousemateException("No " + Id.class.getName() + " on property field" + propertyField.getKey().getName() + " of class " + clazz);
            RealPropertyImpl<Object> property = (RealPropertyImpl<Object>) propertyFactory.create(
                    ChildUtil.logger(logger, idPrefix + id.value()),
                    idPrefix + id.value(),
                    id.name(),
                    id.description(),
                    types.getType(new TypeSpec(propertyField.getKey().getType(), propertyField.getValue().restriction())),
                    propertyField.getValue().minValues(),
                    propertyField.getValue().maxValues(),
                    Lists.newArrayList(value));
            property.addObjectListener(new FieldPropertySetter<>(ChildUtil.logger(logger, idPrefix + id.value()), propertyField.getKey(), object));
            properties.add(property);
        }
        for(Map.Entry<Method, Property> propertyMethod : getAnnotatedMethods(clazz, Property.class).entrySet()) {
            if(propertyMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateException(propertyMethod.getKey().getName() + " must take a single argument");
            Id id = propertyMethod.getKey().getAnnotation(Id.class);
            if(id == null)
                throw new HousemateException("No " + Id.class.getName() + " on property field" + propertyMethod.getKey().getName() + " of class " + clazz);
            Object value = getInitialValue(logger, object, clazz, propertyMethod.getKey().getName());
            RealPropertyImpl<Object> property = (RealPropertyImpl<Object>) propertyFactory.create(
                    ChildUtil.logger(logger, idPrefix + id.value()),
                    idPrefix + id.value(),
                    id.name(),
                    id.description(),
                    types.getType(new TypeSpec(propertyMethod.getKey().getParameterTypes()[0], propertyMethod.getValue().restriction())),
                    propertyMethod.getValue().minValues(),
                    propertyMethod.getValue().maxValues(),
                    Lists.newArrayList(value));
            property.addObjectListener(new MethodPropertySetter(ChildUtil.logger(logger, idPrefix + id.value()), propertyMethod.getKey(), object));
            properties.add(property);
        }
        return properties;
    }

    private Object getInitialValue(Logger logger, Object object, Class<?> clazz, String methodName) {
        if(methodName.startsWith("set")) {
            String fieldName = methodName.substring(3);
            String getterName = "get" + fieldName;
            try {
                Method getter = clazz.getMethod(getterName);
                return getter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                logger.error("Problem getting property initial value using getter {} of {}", getterName, clazz.getName());
            }
            String isGetterName = "is" + fieldName;
            try {
                Method isGetter = clazz.getMethod(isGetterName);
                return isGetter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                logger.error("Problem getting property initial value using isGetter {} of {}", isGetterName, clazz.getName());
            }
        }
        logger.warn("No equivalent getter found for initial value for {} method {} of {}", Property.class.getName(), methodName, clazz.getName());
        return null;
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
