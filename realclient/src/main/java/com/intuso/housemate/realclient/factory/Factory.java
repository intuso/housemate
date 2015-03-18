package com.intuso.housemate.realclient.factory;

import com.google.common.collect.Sets;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/01/14
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */
public class Factory {

    private Factory() {}

    public static <T, C> Set<Entry<T>> getEntries(Log log, Injector injector, Class<C> factoryClass) {
        Set<Entry<T>> result = Sets.newHashSet();
        for(Map.Entry<Key<?>, Binding<?>> entry : injector.getAllBindings().entrySet()) {
            TypeLiteral<?> typeLiteral = entry.getKey().getTypeLiteral();
            if(factoryClass.isAssignableFrom(typeLiteral.getRawType())) {
                String typeClassName = typeLiteral.toString().substring(typeLiteral.toString().indexOf("<") + 1, typeLiteral.toString().length() - 1);
                if(typeClassName.startsWith("?"))
                    continue;
                Class<?> typeClass;
                try {
                    typeClass = Class.forName(typeClassName, true, injector.getInstance(ClassLoader.class));
                } catch (ClassNotFoundException e) {
                    log.e("Could not find type class " + typeClassName);
                    continue;
                }
                TypeInfo typeInfo = typeClass.getAnnotation(TypeInfo.class);
                if(typeInfo != null)
                    result.add(new Entry<T>(injector, typeInfo, (Key<T>) entry.getKey()));
                else
                    log.e("Factory class " + typeClass.getName() + " has no " + TypeInfo.class.getName() + " annotation");
            }
        }
        return result;
    }

    public static class Entry<T> {

        private final Injector injector;
        private final TypeInfo typeInfo;
        private final Key<T> factoryKey;

        public Entry(Injector injector, TypeInfo typeInfo, Key<T> factoryKey) {
            this.injector = injector;
            this.typeInfo = typeInfo;
            this.factoryKey = factoryKey;
        }

        public Injector getInjector() {
            return injector;
        }

        public TypeInfo getTypeInfo() {
            return typeInfo;
        }

        public Key<T> getFactoryKey() {
            return factoryKey;
        }

        @Override
        public final int hashCode() {
            return typeInfo.id().hashCode();
        }
    }
}
