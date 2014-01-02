package com.intuso.housemate.annotations.plugin;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.server.real.*;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.*;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Base class for all plugins that wish to use annotations to describe the provided features
 */
public class AnnotatedPluginDescriptor implements PluginDescriptor {

    private PluginInformation information;
    private final List<Constructor<? extends RealType<?, ?, ?>>> typeConstructors = Lists.newArrayList();
    private final List<Constructor<? extends Comparator<?>>> comparatorConstructors = Lists.newArrayList();
    private final List<Constructor<? extends Operator<?, ?>>> operatorConstructors = Lists.newArrayList();
    private final List<Constructor<? extends Transformer<?, ?>>> transformerConstructors = Lists.newArrayList();
    private final List<RealDeviceFactory<?>> deviceFactories = Lists.newArrayList();
    private final List<ServerConditionFactory<?>> conditionFactories = Lists.newArrayList();
    private final List<ServerTaskFactory<?>> taskFactories = Lists.newArrayList();

    @Override
    public final String getId() {
        return information.id();
    }

    @Override
    public final String getName() {
        return information.name();
    }

    @Override
    public final String getDescription() {
        return information.description();
    }

    @Override
    public final String getAuthor() {
        return information.author();
    }

    @Override
    public void init(Resources resources) throws HousemateException {
        initInformation();
        initTypes(resources);
        initComparators(resources);
        initOperators(resources);
        initTransformers(resources);
        initDeviceFactories(resources);
        initConditionFactories(resources);
        initTaskFactories(resources);
    }

    private void initInformation() throws HousemateException {
        Class<?> clazz = getClass();
        information = getClass().getAnnotation(PluginInformation.class);
        if(information == null)
            throw new HousemateException("Annotated plugin " + getClass().getName() + " has no "
                    + PluginInformation.class.getName() + " annotation");
    }

    private void initTypes(Resources resources) throws HousemateException {
        Types types = getClass().getAnnotation(Types.class);
        if(types != null) {
            for(Class<? extends RealType<?, ?, ?>> typeClass : types.value()) {
                try {
                    typeConstructors.add(typeClass.getConstructor(RealResources.class));
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Failed to find " + typeClass.getName() + " constructor with single "
                            + RealResources.class.getName() + " parameter");
                }
            }
        }
    }

    private void initComparators(Resources resources) throws HousemateException {
        Comparators comparators = getClass().getAnnotation(Comparators.class);
        if(comparators != null) {
            for(Class<? extends Comparator<?>> comparatorClass : comparators.value()) {
                try {
                    comparatorConstructors.add(comparatorClass.getConstructor());
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Failed to find " + comparatorClass.getName() + " constructor with single "
                            + RealResources.class.getName() + " parameter");
                }
            }
        }
    }

    private void initOperators(Resources resources) throws HousemateException {
        Operators operators = getClass().getAnnotation(Operators.class);
        if(operators != null) {
            for(Class<? extends Operator<?, ?>> operatorClass : operators.value()) {
                try {
                    operatorConstructors.add(operatorClass.getConstructor());
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Failed to find " + operatorClass.getName() + " constructor with single "
                            + RealResources.class.getName() + " parameter");
                }
            }
        }
    }

    private void initTransformers(Resources resources) throws HousemateException {
        Transformers transformers = getClass().getAnnotation(Transformers.class);
        if(transformers != null) {
            for(Class<? extends Transformer<?, ?>> transformerClass : transformers.value()) {
                try {
                    transformerConstructors.add(transformerClass.getConstructor());
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Failed to find " + transformerClass.getName() + " constructor with single "
                            + RealResources.class.getName() + " parameter");
                }
            }
        }
    }
    
    private void initDeviceFactories(Resources resources) throws HousemateException {
        DeviceFactories deviceFactories = getClass().getAnnotation(DeviceFactories.class);
        if(deviceFactories != null) {
            for(Class<? extends RealDeviceFactory<?>> factoryClass : deviceFactories.value())
                try {
                    this.deviceFactories.add(factoryClass.newInstance());
                } catch(Exception e) {
                    throw new HousemateException("Failed to create device factory");
                }
        }
        Devices devices = getClass().getAnnotation(Devices.class);
        if(devices != null) {
            for(Class<? extends RealDevice> deviceClass : devices.value()) {
                FactoryInformation information = deviceClass.getAnnotation(FactoryInformation.class);
                if(information == null)
                    throw new HousemateException("Device class " + deviceClass.getName() + " has no "
                            + FactoryInformation.class.getName() + " annotation");
                Constructor<? extends RealDevice> constructor;
                try {
                     constructor = deviceClass.getConstructor(
                            RealResources.class, String.class, String.class, String.class);
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Device class " + deviceClass.getName() + " does not have the correct constructor");
                }
                this.deviceFactories.add(new SimpleDeviceFactory(information, constructor));
            }
        }
    }

    private void initConditionFactories(Resources resources) throws HousemateException {
        ConditionFactories conditionFactories = getClass().getAnnotation(ConditionFactories.class);
        if(conditionFactories != null) {
            for(Class<? extends ServerConditionFactory<?>> factoryClass : conditionFactories.value())
                try {
                    this.conditionFactories.add(factoryClass.newInstance());
                } catch(Exception e) {
                    throw new HousemateException("Failed to create condition factory");
                }
        }
        Conditions conditions = getClass().getAnnotation(Conditions.class);
        if(conditions != null) {
            for(Class<? extends ServerRealCondition> conditionClass : conditions.value()) {
                FactoryInformation information = conditionClass.getAnnotation(FactoryInformation.class);
                if(information == null)
                    throw new HousemateException("Condition class " + conditionClass.getName() + " has no "
                            + FactoryInformation.class.getName() + " annotation");
                Constructor<? extends ServerRealCondition> constructor;
                try {
                    constructor = conditionClass.getConstructor(
                            ServerRealResources.class, String.class, String.class, String.class,
                            ServerRealConditionOwner.class);
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Condition class " + conditionClass.getName() + " does not have the correct constructor");
                }
                this.conditionFactories.add(new SimpleConditionFactory(information, constructor));
            }
        }
    }

    private void initTaskFactories(Resources resources) throws HousemateException {
        TaskFactories taskFactories = getClass().getAnnotation(TaskFactories.class);
        if(taskFactories != null) {
            for(Class<? extends ServerTaskFactory<?>> factoryClass : taskFactories.value())
                try {
                    this.taskFactories.add(factoryClass.newInstance());
                } catch(Exception e) {
                    throw new HousemateException("Failed to create task factory");
                }
        }
        Tasks tasks = getClass().getAnnotation(Tasks.class);
        if(tasks != null) {
            for(Class<? extends ServerRealTask> taskClass : tasks.value()) {
                FactoryInformation information = taskClass.getAnnotation(FactoryInformation.class);
                if(information == null)
                    throw new HousemateException("Task class " + taskClass.getName() + " has no "
                            + FactoryInformation.class.getName() + " annotation");
                Constructor<? extends ServerRealTask> constructor;
                try {
                    constructor = taskClass.getConstructor(
                            ServerRealResources.class, String.class, String.class, String.class, ServerRealTaskOwner.class);
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Task class " + taskClass.getName() + " does not have the correct constructor");
                }
                this.taskFactories.add(new SimpleTaskFactory(information, constructor));
            }
        }
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes(RealResources resources) {
        List<RealType<?, ?, ?>> result = Lists.newArrayList();
        for(Constructor<? extends RealType<?, ?, ?>> constructor : typeConstructors) {
            try {
                result.add(constructor.newInstance(resources));
            } catch(Exception e) {
                resources.getLog().e("Failed to create type instance");
                resources.getLog().st(e);
            }
        }
        return result;
    }

    @Override
    public List<Comparator<?>> getComparators(RealResources resources) {
        List<Comparator<?>> result = Lists.newArrayList();
        for(Constructor<? extends Comparator<?>> constructor : comparatorConstructors) {
            try {
                result.add(constructor.newInstance());
            } catch(Exception e) {
                resources.getLog().e("Failed to create type comparator");
                resources.getLog().st(e);
            }
        }
        return result;
    }

    @Override
    public List<Operator<?, ?>> getOperators(RealResources resources) {
        List<Operator<?, ?>> result = Lists.newArrayList();
        for(Constructor<? extends Operator<?, ?>> constructor : operatorConstructors) {
            try {
                result.add(constructor.newInstance());
            } catch(Exception e) {
                resources.getLog().e("Failed to create type operator");
                resources.getLog().st(e);
            }
        }
        return result;
    }

    @Override
    public List<Transformer<?, ?>> getTransformers(RealResources resources) {
        List<Transformer<?, ?>> result = Lists.newArrayList();
        for(Constructor<? extends Transformer<?, ?>> constructor : transformerConstructors) {
            try {
                result.add(constructor.newInstance());
            } catch(Exception e) {
                resources.getLog().e("Failed to create type transformer");
                resources.getLog().st(e);
            }
        }
        return result;
    }

    @Override
    public List<RealDeviceFactory<?>> getDeviceFactories() {
        return deviceFactories;
    }

    @Override
    public List<ServerConditionFactory<?>> getConditionFactories() {
        return conditionFactories;
    }

    @Override
    public List<ServerTaskFactory<?>> getTaskFactories() {
        return taskFactories;
    }
}
