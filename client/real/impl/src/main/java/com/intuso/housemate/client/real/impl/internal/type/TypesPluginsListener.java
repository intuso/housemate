package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.api.internal.plugin.PluginListener;
import com.intuso.housemate.client.api.internal.plugin.PluginResource;
import com.intuso.housemate.client.api.internal.plugin.RegexType;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 19/03/15.
 */
public class TypesPluginsListener implements PluginListener {

    private final static Logger logger = LoggerFactory.getLogger(TypesPluginsListener.class);

    private final Logger typeLogger;
    private final TypeRepository types;
    private final RealRegexType.Factory regexTypeFactory;
    private final ConditionDriverType conditionDriverType;
    private final FeatureDriverType featureDriverType;
    private final HardwareDriverType hardwareDriverType;
    private final TaskDriverType taskDriverType;

    @Inject
    public TypesPluginsListener(@Type Logger typeLogger, TypeRepository types, RealRegexType.Factory regexTypeFactory, ConditionDriverType conditionDriverType, FeatureDriverType featureDriverType, HardwareDriverType hardwareDriverType, TaskDriverType taskDriverType) {
        this.typeLogger = typeLogger;
        this.types = types;
        this.regexTypeFactory = regexTypeFactory;
        this.conditionDriverType = conditionDriverType;
        this.featureDriverType = featureDriverType;
        this.hardwareDriverType = hardwareDriverType;
        this.taskDriverType = taskDriverType;
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
        for(RegexType regexType : pluginInjector.getInstance(new Key<Iterable<RegexType>>() {})) {
            logger.debug("Adding regex type " + getClass().getName());
            types.typeAvailable(regexTypeFactory.create(
                    ChildUtil.logger(typeLogger, new TypeSpec(String.class, regexType.id().value()).toString()),
                    regexType.id().value(),
                    regexType.id().name(),
                    regexType.id().description(),
                    regexType.regex()
            ));
        }
    }

    private void removeTypes(Injector pluginInjector) {
        for(RegexType regexType : pluginInjector.getInstance(new Key<Iterable<RegexType>>() {})) {
            logger.debug("Adding regex type " + getClass().getName());
            types.typeUnavailable(new TypeSpec(String.class, regexType.id().value()));
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
