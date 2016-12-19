package com.intuso.housemate.server.object.real;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.api.internal.plugin.PluginListener;
import com.intuso.housemate.client.api.internal.plugin.PluginResource;
import com.intuso.housemate.client.real.impl.internal.type.*;
import com.intuso.housemate.plugin.host.internal.PluginHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 19/03/15.
 */
public class FactoryPluginListener implements PluginListener {

    private final static Logger logger = LoggerFactory.getLogger(FactoryPluginListener.class);

    private final RegisteredTypes types;
    private final ConditionDriverType conditionDriverType;
    private final FeatureDriverType featureDriverType;
    private final HardwareDriverType hardwareDriverType;
    private final TaskDriverType taskDriverType;

    @Inject
    public FactoryPluginListener(RegisteredTypes types, ConditionDriverType conditionDriverType, FeatureDriverType featureDriverType, HardwareDriverType hardwareDriverType, TaskDriverType taskDriverType, PluginHost pluginHost) {
        this.types = types;
        this.conditionDriverType = conditionDriverType;
        this.featureDriverType = featureDriverType;
        this.hardwareDriverType = hardwareDriverType;
        this.taskDriverType = taskDriverType;
        pluginHost.addPluginListener(this, true);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        addTypes(pluginInjector);
        addConditionDriverFactories(pluginInjector);
        addDeviceDriverFactories(pluginInjector);
        addHardwareDriverFactories(pluginInjector);
        addTaskDriverFactories(pluginInjector);
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        removeTypes(pluginInjector);
        removeConditionDriverFactories(pluginInjector);
        removeDeviceDriverFactories(pluginInjector);
        removeHardwareDriverFactories(pluginInjector);
        removeTaskDriverFactories(pluginInjector);
    }

    private void addTypes(Injector pluginInjector) {
        for(PluginResource<Class<?>> typeResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<Class<?>>>>() {})) {
            logger.debug("Adding type " + getClass().getName());
            types.typeAvailable(pluginInjector, typeResource.getResource());
        }
    }

    private void removeTypes(Injector pluginInjector) {
        for(PluginResource<Class<?>> typeResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<Class<?>>>>() {})) {
            logger.debug("Removing type " + getClass().getName());
            types.typeUnavailable(typeResource.getId().value());
        }
    }

    private void addConditionDriverFactories(Injector pluginInjector) {
        for(PluginResource<? extends ConditionDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends ConditionDriver.Factory<?>>>>() {})) {
            logger.debug("Adding condition factory for type " + factoryResource.getId().value());
            conditionDriverType.factoryAvailable(factoryResource.getId().value(),
                    factoryResource.getId().name(), factoryResource.getId().description(),
                    factoryResource.getResource());
        }
    }

    private void removeConditionDriverFactories(Injector pluginInjector) {
        for(PluginResource<? extends ConditionDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends ConditionDriver.Factory<?>>>>() {})) {
            logger.debug("Removing condition factory for type " + factoryResource.getId().value());
            conditionDriverType.factoryUnavailable(factoryResource.getId().value());
        }
    }

    private void addDeviceDriverFactories(Injector pluginInjector) {
        for(PluginResource<? extends FeatureDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends FeatureDriver.Factory<?>>>>() {})) {
            logger.debug("Adding device factory for type " + factoryResource.getId().value());
            featureDriverType.factoryAvailable(factoryResource.getId().value(),
                    factoryResource.getId().name(), factoryResource.getId().description(),
                    factoryResource.getResource());
        }
    }

    private void removeDeviceDriverFactories(Injector pluginInjector) {
        for(PluginResource<? extends FeatureDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends FeatureDriver.Factory<?>>>>() {})) {
            logger.debug("Removing device factory for type " + factoryResource.getId().value());
            featureDriverType.factoryUnavailable(factoryResource.getId().value());
        }
    }

    private void addHardwareDriverFactories(Injector pluginInjector) {
        for(PluginResource<? extends HardwareDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends HardwareDriver.Factory<?>>>>() {})) {
            logger.debug("Adding hardware factory for type " + factoryResource.getId().value());
            hardwareDriverType.factoryAvailable(factoryResource.getId().value(),
                    factoryResource.getId().name(), factoryResource.getId().description(),
                    factoryResource.getResource());
        }
    }

    private void removeHardwareDriverFactories(Injector pluginInjector) {
        for(PluginResource<? extends HardwareDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends HardwareDriver.Factory<?>>>>() {})) {
            logger.debug("Removing hardware factory for type " + factoryResource.getId().value());
            hardwareDriverType.factoryUnavailable(factoryResource.getId().value());
        }
    }

    private void addTaskDriverFactories(Injector pluginInjector) {
        for(PluginResource<? extends TaskDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends TaskDriver.Factory<?>>>>() {})) {
            logger.debug("Adding task factory for type " + factoryResource.getId().value());
            taskDriverType.factoryAvailable(factoryResource.getId().value(),
                    factoryResource.getId().name(), factoryResource.getId().description(),
                    factoryResource.getResource());
        }
    }

    private void removeTaskDriverFactories(Injector pluginInjector) {
        for(PluginResource<? extends TaskDriver.Factory<?>> factoryResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends TaskDriver.Factory<?>>>>() {})) {
            logger.debug("Removing task factory for type " + factoryResource.getId().value());
            taskDriverType.factoryUnavailable(factoryResource.getId().value());
        }
    }
}
