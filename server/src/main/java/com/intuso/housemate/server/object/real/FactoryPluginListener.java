package com.intuso.housemate.server.object.real;

import com.google.common.collect.Sets;
import com.google.inject.*;
import com.intuso.housemate.client.real.api.internal.RealRoot;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.impl.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.Set;

/**
 * Created by tomc on 19/03/15.
 */
public class FactoryPluginListener implements PluginListener {

    private final Log log;
    private final RealRoot root;
    private final HardwareFactoryType hardwareFactoryType;
    private final DeviceFactoryType deviceFactoryType;
    private final ConditionFactoryType conditionFactoryType;
    private final TaskFactoryType taskFactoryType;

    @Inject
    public FactoryPluginListener(Log log, HardwareFactoryType hardwareFactoryType, DeviceFactoryType deviceFactoryType, ConditionFactoryType conditionFactoryType, TaskFactoryType taskFactoryType, PluginManager pluginManager, RealRoot root) {
        this.log = log;
        this.hardwareFactoryType = hardwareFactoryType;
        this.deviceFactoryType = deviceFactoryType;
        this.conditionFactoryType = conditionFactoryType;
        this.taskFactoryType = taskFactoryType;
        this.root = root;
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        addTypes(pluginInjector);
        addHardwareFactories(pluginInjector);
        addDeviceFactories(pluginInjector);
        addConditionFactories(pluginInjector);
        addTaskFactories(pluginInjector);
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        removeTypes(pluginInjector);
        removeHardwareFactories(pluginInjector);
        removeDeviceFactories(pluginInjector);
        removeConditionFactories(pluginInjector);
        removeTaskFactories(pluginInjector);
    }

    private void addTypes(Injector pluginInjector) {
        for(RealType<?> type : pluginInjector.getInstance(new Key<Set<RealType<?>>>() {})) {
            log.d("Adding type " + type.getId());
            root.addType(type);
        }
    }

    private void removeTypes(Injector pluginInjector) {
        for(RealType<?> type : pluginInjector.getInstance(new Key<Set<RealType<?>>>() {})) {
            log.d("Removing type " + type.getId());
            root.removeType(type);
        }
    }

    private void addHardwareFactories(Injector pluginInjector) {
        Set<Entry<HardwareDriver.Factory<?>>> factoryEntries = getEntries(pluginInjector, HardwareDriver.Factory.class);
        for(Entry<HardwareDriver.Factory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new hardware factory for type " + factoryEntry.getTypeInfo().id());
            hardwareFactoryType.factoryAvailable(factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description(),
                    factoryEntry.getInjector().getInstance(factoryEntry.getFactoryKey()));
        }
    }
    
    private void removeHardwareFactories(Injector pluginInjector) {
        Set<Entry<HardwareDriver.Factory<?>>> factoryEntries = getEntries(pluginInjector, HardwareDriver.Factory.class);
        for(Entry<HardwareDriver.Factory<?>> factoryEntry : factoryEntries)
            hardwareFactoryType.factoryUnavailable(factoryEntry.getTypeInfo().id());
    }

    private void addDeviceFactories(Injector pluginInjector) {
        Set<Entry<DeviceDriver.Factory<?>>> factoryEntries = getEntries(pluginInjector, DeviceDriver.Factory.class);
        for(Entry<DeviceDriver.Factory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new device factory for type " + factoryEntry.getTypeInfo().id());
            deviceFactoryType.factoryAvailable(factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description(),
                    factoryEntry.getInjector().getInstance(factoryEntry.getFactoryKey()));
        }
    }

    private void removeDeviceFactories(Injector pluginInjector) {
        Set<Entry<DeviceDriver.Factory<?>>> factoryEntries = getEntries(pluginInjector, DeviceDriver.Factory.class);
        for(Entry<DeviceDriver.Factory<?>> factoryEntry : factoryEntries)
            deviceFactoryType.factoryUnavailable(factoryEntry.getTypeInfo().id());
    }

    private void addConditionFactories(Injector pluginInjector) {
        Set<Entry<ConditionDriver.Factory<?>>> factoryEntries = getEntries(pluginInjector, ConditionDriver.Factory.class);
        for(Entry<ConditionDriver.Factory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new condition factory for type " + factoryEntry.getTypeInfo().id());
            conditionFactoryType.factoryAvailable(factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description(),
                    factoryEntry.getInjector().getInstance(factoryEntry.getFactoryKey()));
        }
    }

    private void removeConditionFactories(Injector pluginInjector) {
        Set<Entry<ConditionDriver.Factory<?>>> factoryEntries = getEntries(pluginInjector, ConditionDriver.Factory.class);
        for(Entry<ConditionDriver.Factory<?>> factoryEntry : factoryEntries)
            conditionFactoryType.factoryUnavailable(factoryEntry.getTypeInfo().id());
    }

    private void addTaskFactories(Injector pluginInjector) {
        Set<Entry<TaskDriver.Factory<?>>> factoryEntries = getEntries(pluginInjector, TaskDriver.Factory.class);
        for(Entry<TaskDriver.Factory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new task factory for type " + factoryEntry.getTypeInfo().id());
            taskFactoryType.factoryAvailable(factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description(),
                    factoryEntry.getInjector().getInstance(factoryEntry.getFactoryKey()));
        }
    }

    private void removeTaskFactories(Injector pluginInjector) {
        Set<Entry<TaskDriver.Factory<?>>> factoryEntries = getEntries(pluginInjector, TaskDriver.Factory.class);
        for(Entry<TaskDriver.Factory<?>> factoryEntry : factoryEntries)
            taskFactoryType.factoryUnavailable(factoryEntry.getTypeInfo().id());
    }
    
    private <T, C> Set<Entry<T>> getEntries(Injector injector, Class<C> factoryClass) {
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
                    result.add(new Entry<>(injector, typeInfo, (Key<T>) entry.getKey()));
                else
                    log.e("Factory class " + typeClass.getName() + " has no " + TypeInfo.class.getName() + " annotation");
            }
        }
        return result;
    }

    private class Entry<T> {

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
