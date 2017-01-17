package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.bridge.v1_0.driver.ConditionDriverFactoryBridge;
import com.intuso.housemate.client.api.bridge.v1_0.driver.FeatureDriverFactoryBridge;
import com.intuso.housemate.client.api.bridge.v1_0.driver.HardwareDriverFactoryBridge;
import com.intuso.housemate.client.api.bridge.v1_0.driver.TaskDriverFactoryBridge;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.driver.ConditionDriver;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;
import com.intuso.housemate.client.v1_0.api.plugin.Plugin;
import com.intuso.housemate.client.v1_0.api.plugin.PluginListener;
import com.intuso.housemate.client.v1_0.api.plugin.RegexType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 19/03/15.
 */
public class TypesV1_0PluginsListener implements PluginListener {

    private final static Logger logger = LoggerFactory.getLogger(TypesV1_0PluginsListener.class);

    private final Logger typeLogger;
    private final Injector injector;
    private final TypeRepository types;
    private final RealRegexType.Factory regexTypeFactory;
    private final ConditionDriverType conditionDriverType;
    private final FeatureDriverType featureDriverType;
    private final HardwareDriverType hardwareDriverType;
    private final TaskDriverType taskDriverType;
    private final ConditionDriverFactoryBridge.Factory conditionDriverFactoryBridgeFactory;
    private final FeatureDriverFactoryBridge.Factory featureDriverFactoryBridgeFactory;
    private final HardwareDriverFactoryBridge.Factory hardwareDriverFactoryBridgeFactory;
    private final TaskDriverFactoryBridge.Factory taskDriverFactoryBridgeFactory;

    @Inject
    public TypesV1_0PluginsListener(@Type Logger typeLogger, Injector injector, TypeRepository types, RealRegexType.Factory regexTypeFactory, ConditionDriverType conditionDriverType, FeatureDriverType featureDriverType, HardwareDriverType hardwareDriverType, TaskDriverType taskDriverType, ConditionDriverFactoryBridge.Factory conditionDriverFactoryBridgeFactory, FeatureDriverFactoryBridge.Factory featureDriverFactoryBridgeFactory, HardwareDriverFactoryBridge.Factory hardwareDriverFactoryBridgeFactory, TaskDriverFactoryBridge.Factory taskDriverFactoryBridgeFactory) {
        this.typeLogger = typeLogger;
        this.injector = injector;
        this.types = types;
        this.regexTypeFactory = regexTypeFactory;
        this.conditionDriverType = conditionDriverType;
        this.featureDriverType = featureDriverType;
        this.hardwareDriverType = hardwareDriverType;
        this.taskDriverType = taskDriverType;
        this.conditionDriverFactoryBridgeFactory = conditionDriverFactoryBridgeFactory;
        this.featureDriverFactoryBridgeFactory = featureDriverFactoryBridgeFactory;
        this.hardwareDriverFactoryBridgeFactory = hardwareDriverFactoryBridgeFactory;
        this.taskDriverFactoryBridgeFactory = taskDriverFactoryBridgeFactory;
    }

    @Override
    public void pluginAdded(Plugin plugin) {
        addTypes(plugin);
        addConditionDriverFactories(plugin);
        addFeatureDriverFactories(plugin);
        addHardwareDriverFactories(plugin);
        addTaskDriverFactories(plugin);
    }

    @Override
    public void pluginRemoved(Plugin plugin) {
        removeTypes(plugin);
        removeConditionDriverFactories(plugin);
        removeFeatureDriverFactories(plugin);
        removeHardwareDriverFactories(plugin);
        removeTaskDriverFactories(plugin);
    }

    private void addTypes(Plugin plugin) {
        for(RegexType regexType : plugin.getRegexTypes()) {
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

    private void removeTypes(Plugin plugin) {
        for(RegexType regexType : plugin.getRegexTypes()) {
            logger.debug("Adding regex type " + getClass().getName());
            types.typeUnavailable(new TypeSpec(String.class, regexType.id().value()));
        }
    }

    private void addConditionDriverFactories(Plugin plugin) {
        for(Class<? extends ConditionDriver> conditionDriverClass : plugin.getConditionDrivers()) {
            Id id = getClassAnnotation(conditionDriverClass, Id.class);
            if(id == null)
                logger.error("No " + Id.class + " annotation found on " + conditionDriverClass.getName());
            else
                conditionDriverType.addFactory(id.value(), id.name(), id.description(), conditionDriverFactoryBridgeFactory.create(asFactory(ConditionDriver.class, ConditionDriver.Factory.class, conditionDriverClass)));
        }
    }

    private void removeConditionDriverFactories(Plugin plugin) {
        for(Class<? extends ConditionDriver> conditionDriverClass : plugin.getConditionDrivers()) {
            Id id = getClassAnnotation(conditionDriverClass, Id.class);
            if(id != null) {
                logger.debug("Removing condition factory for type " + id.value());
                conditionDriverType.removeFactory(id.value());
            }
        }
    }

    private void addFeatureDriverFactories(Plugin plugin) {
        for(Class<? extends FeatureDriver> featureDriverClass : plugin.getFeatureDrivers()) {
            Id id = getClassAnnotation(featureDriverClass, Id.class);
            if(id == null)
                logger.error("No " + Id.class + " annotation found on " + featureDriverClass.getName());
            else
                featureDriverType.addFactory(id.value(), id.name(), id.description(), featureDriverFactoryBridgeFactory.create(asFactory(FeatureDriver.class, FeatureDriver.Factory.class, featureDriverClass)));
        }
    }

    private void removeFeatureDriverFactories(Plugin plugin) {
        for(Class<? extends FeatureDriver> featureDriverClass : plugin.getFeatureDrivers()) {
            Id id = getClassAnnotation(featureDriverClass, Id.class);
            if(id != null) {
                logger.debug("Removing feature factory for type " + id.value());
                featureDriverType.removeFactory(id.value());
            }
        }
    }

    private void addHardwareDriverFactories(Plugin plugin) {
        for(Class<? extends HardwareDriver> hardwareDriverClass : plugin.getHardwareDrivers()) {
            Id id = getClassAnnotation(hardwareDriverClass, Id.class);
            if(id == null)
                logger.error("No " + Id.class + " annotation found on " + hardwareDriverClass.getName());
            else
                hardwareDriverType.addFactory(id.value(), id.name(), id.description(), hardwareDriverFactoryBridgeFactory.create(asFactory(HardwareDriver.class, HardwareDriver.Factory.class, hardwareDriverClass)));
        }
    }

    private void removeHardwareDriverFactories(Plugin plugin) {
        for(Class<? extends HardwareDriver> hardwareDriverClass : plugin.getHardwareDrivers()) {
            Id id = getClassAnnotation(hardwareDriverClass, Id.class);
            if(id != null) {
                logger.debug("Removing hardware factory for type " + id.value());
                hardwareDriverType.removeFactory(id.value());
            }
        }
    }

    private void addTaskDriverFactories(Plugin plugin) {
        for(Class<? extends TaskDriver> taskDriverClass : plugin.getTaskDrivers()) {
            Id id = getClassAnnotation(taskDriverClass, Id.class);
            if(id == null)
                logger.error("No " + Id.class + " annotation found on " + taskDriverClass.getName());
            else
                taskDriverType.addFactory(id.value(), id.name(), id.description(), taskDriverFactoryBridgeFactory.create(asFactory(TaskDriver.class, TaskDriver.Factory.class, taskDriverClass)));
        }
    }

    private void removeTaskDriverFactories(Plugin plugin) {
        for(Class<? extends TaskDriver> taskDriverClass : plugin.getTaskDrivers()) {
            Id id = getClassAnnotation(taskDriverClass, Id.class);
            if(id != null) {
                logger.debug("Removing task factory for type " + id.value());
                taskDriverType.removeFactory(id.value());
            }
        }
    }

    private <FACTORY, RESOURCE> FACTORY asFactory(Class<?> ownerClass, Class<FACTORY> factoryClass, final Class<RESOURCE> resourceClass) {
        java.lang.reflect.Type type = Types.newParameterizedTypeWithOwner(ownerClass, factoryClass, resourceClass);
        TypeLiteral<FACTORY> typeLiteral = (TypeLiteral<FACTORY>) TypeLiteral.get(type);
        return injector.createChildInjector(new FactoryModuleBuilder().build(typeLiteral)).getInstance(Key.get(typeLiteral));
    }

    private <ANNOTATION extends Annotation> ANNOTATION getClassAnnotation(Class<?> annotatedClass, Class<ANNOTATION> annotationClass) {
        ANNOTATION result = annotatedClass.getAnnotation(annotationClass);
        if(result != null)
            return result;
        if(annotatedClass.getSuperclass() != null) {
            result = getClassAnnotation(annotatedClass.getSuperclass(), annotationClass);
            if(result != null)
                return result;
        }
        for(Class<?> implementedInterface : annotatedClass.getInterfaces()) {
            result = getClassAnnotation(implementedInterface, annotationClass);
            if(result != null)
                return result;
        }
        return null;
    }
}
