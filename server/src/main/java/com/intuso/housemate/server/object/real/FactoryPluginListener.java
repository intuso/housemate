package com.intuso.housemate.server.object.real;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
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
import com.intuso.housemate.plugin.api.internal.PluginResource;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 19/03/15.
 */
public class FactoryPluginListener implements PluginListener {

    private final Log log;
    private final RealRoot root;
    private final ConditionFactoryType conditionFactoryType;
    private final DeviceFactoryType deviceFactoryType;
    private final HardwareFactoryType hardwareFactoryType;
    private final TaskFactoryType taskFactoryType;

    @Inject
    public FactoryPluginListener(Log log, ConditionFactoryType conditionFactoryType, DeviceFactoryType deviceFactoryType, HardwareFactoryType hardwareFactoryType, TaskFactoryType taskFactoryType, PluginManager pluginManager, RealRoot root) {
        this.log = log;
        this.conditionFactoryType = conditionFactoryType;
        this.deviceFactoryType = deviceFactoryType;
        this.hardwareFactoryType = hardwareFactoryType;
        this.taskFactoryType = taskFactoryType;
        this.root = root;
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        addConditionFactories(pluginInjector);
        addDeviceFactories(pluginInjector);
        addHardwareFactories(pluginInjector);
        addTaskFactories(pluginInjector);
        addTypes(pluginInjector);
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        removeConditionFactories(pluginInjector);
        removeDeviceFactories(pluginInjector);
        removeHardwareFactories(pluginInjector);
        removeTaskFactories(pluginInjector);
        removeTypes(pluginInjector);
    }

    private void addConditionFactories(Injector pluginInjector) {
        for(PluginResource<? extends ConditionDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends ConditionDriver.Factory<?>>>>() {})) {
            log.d("Adding new condition factory for type " + factoryResource.getTypeInfo().id());
            conditionFactoryType.factoryAvailable(factoryResource.getTypeInfo().id(),
                    factoryResource.getTypeInfo().name(), factoryResource.getTypeInfo().description(),
                    factoryResource.getResource());
        }
    }

    private void removeConditionFactories(Injector pluginInjector) {
        for(PluginResource<? extends ConditionDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends ConditionDriver.Factory<?>>>>() {}))
            conditionFactoryType.factoryUnavailable(factoryResource.getTypeInfo().id());
    }

    private void addDeviceFactories(Injector pluginInjector) {
        for(PluginResource<? extends DeviceDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends DeviceDriver.Factory<?>>>>() {})) {
            log.d("Adding new device factory for type " + factoryResource.getTypeInfo().id());
            deviceFactoryType.factoryAvailable(factoryResource.getTypeInfo().id(),
                    factoryResource.getTypeInfo().name(), factoryResource.getTypeInfo().description(),
                    factoryResource.getResource());
        }
    }

    private void removeDeviceFactories(Injector pluginInjector) {
        for(PluginResource<? extends DeviceDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends DeviceDriver.Factory<?>>>>() {}))
            deviceFactoryType.factoryUnavailable(factoryResource.getTypeInfo().id());
    }

    private void addHardwareFactories(Injector pluginInjector) {
        for(PluginResource<? extends HardwareDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends HardwareDriver.Factory<?>>>>() {})) {
            log.d("Adding new hardware factory for type " + factoryResource.getTypeInfo().id());
            hardwareFactoryType.factoryAvailable(factoryResource.getTypeInfo().id(),
                    factoryResource.getTypeInfo().name(), factoryResource.getTypeInfo().description(),
                    factoryResource.getResource());
        }
    }

    private void removeHardwareFactories(Injector pluginInjector) {
        for(PluginResource<? extends HardwareDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends HardwareDriver.Factory<?>>>>() {}))
            hardwareFactoryType.factoryUnavailable(factoryResource.getTypeInfo().id());
    }

    private void addTaskFactories(Injector pluginInjector) {
        for(PluginResource<? extends TaskDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends TaskDriver.Factory<?>>>>() {})) {
            log.d("Adding new task factory for type " + factoryResource.getTypeInfo().id());
            taskFactoryType.factoryAvailable(factoryResource.getTypeInfo().id(),
                    factoryResource.getTypeInfo().name(), factoryResource.getTypeInfo().description(),
                    factoryResource.getResource());
        }
    }

    private void removeTaskFactories(Injector pluginInjector) {
        for(PluginResource<? extends TaskDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends TaskDriver.Factory<?>>>>() {}))
            taskFactoryType.factoryUnavailable(factoryResource.getTypeInfo().id());
    }

    private void addTypes(Injector pluginInjector) {
        for(RealType<?> type : pluginInjector.getInstance(new Key<Iterable<? extends RealType<?>>>() {})) {
            log.d("Adding type " + type.getId());
            root.addType(type);
        }
    }

    private void removeTypes(Injector pluginInjector) {
        for(RealType<?> type : pluginInjector.getInstance(new Key<Iterable<? extends RealType<?>>>() {})) {
            log.d("Removing type " + type.getId());
            root.removeType(type);
        }
    }
}
