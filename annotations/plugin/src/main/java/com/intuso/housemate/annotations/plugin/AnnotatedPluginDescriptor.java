package com.intuso.housemate.annotations.plugin;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;
import com.intuso.housemate.plugin.api.BrokerConsequenceFactory;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.RealDeviceFactory;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/06/13
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public class AnnotatedPluginDescriptor implements PluginDescriptor {

    private PluginInformation information;
    private final List<Constructor<? extends RealType<?, ?, ?>>> typeConstructors = Lists.newArrayList();
    private final List<RealDeviceFactory<?>> deviceFactories = Lists.newArrayList();
    private final List<BrokerConditionFactory<?>> conditionFactories = Lists.newArrayList();
    private final List<BrokerConsequenceFactory<?>> consequenceFactories = Lists.newArrayList();

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
        initDeviceFactories(resources);
        initConditionFactories(resources);
        initConsequenceFactories(resources);
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

    private void initConditionFactories(Resources resources) throws HousemateException {
        ConditionFactories conditionFactories = getClass().getAnnotation(ConditionFactories.class);
        if(conditionFactories != null) {
            for(Class<? extends BrokerConditionFactory<?>> factoryClass : conditionFactories.value())
                try {
                    this.conditionFactories.add(factoryClass.newInstance());
                } catch(Exception e) {
                    throw new HousemateException("Failed to create condition factory");
                }
        }
        Conditions conditions = getClass().getAnnotation(Conditions.class);
        for(Class<? extends BrokerRealCondition> conditionClass : conditions.value()) {
            FactoryInformation information = conditionClass.getAnnotation(FactoryInformation.class);
            if(information == null)
                throw new HousemateException("Condition class " + conditionClass.getName() + " has no "
                        + FactoryInformation.class.getName() + " annotation");
            Constructor<? extends BrokerRealCondition> constructor;
            try {
                constructor = conditionClass.getConstructor(
                        BrokerRealResources.class, String.class, String.class, String.class);
            } catch(NoSuchMethodException e) {
                throw new HousemateException("Condition class " + conditionClass.getName() + " does not have the correct constructor");
            }
            this.conditionFactories.add(new SimpleConditionFactory(information, constructor));
        }
    }

    private void initConsequenceFactories(Resources resources) throws HousemateException {
        ConsequenceFactories consequenceFactories = getClass().getAnnotation(ConsequenceFactories.class);
        if(consequenceFactories != null) {
            for(Class<? extends BrokerConsequenceFactory<?>> factoryClass : consequenceFactories.value())
                try {
                    this.consequenceFactories.add(factoryClass.newInstance());
                } catch(Exception e) {
                    throw new HousemateException("Failed to create consequence factory");
                }
        }
        Consequences consequences = getClass().getAnnotation(Consequences.class);
        for(Class<? extends BrokerRealConsequence> consequenceClass : consequences.value()) {
            FactoryInformation information = consequenceClass.getAnnotation(FactoryInformation.class);
            if(information == null)
                throw new HousemateException("Consequence class " + consequenceClass.getName() + " has no "
                        + FactoryInformation.class.getName() + " annotation");
            Constructor<? extends BrokerRealConsequence> constructor;
            try {
                constructor = consequenceClass.getConstructor(
                        BrokerRealResources.class, String.class, String.class, String.class);
            } catch(NoSuchMethodException e) {
                throw new HousemateException("Consequence class " + consequenceClass.getName() + " does not have the correct constructor");
            }
            this.consequenceFactories.add(new SimpleConsequenceFactory(information, constructor));
        }
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes(RealResources resources) throws HousemateException {
        List<RealType<?, ?, ?>> result = Lists.newArrayList();
        for(Constructor<? extends RealType<?, ?, ?>> constructor : typeConstructors) {
            try {
                result.add(constructor.newInstance(resources));
            } catch(Exception e) {
                throw new HousemateException("Failed to create type instance", e);
            }
        }
        return result;
    }

    @Override
    public List<RealDeviceFactory<?>> getDeviceFactories() {
        return deviceFactories;
    }

    @Override
    public List<BrokerConditionFactory<?>> getConditionFactories() {
        return conditionFactories;
    }

    @Override
    public List<BrokerConsequenceFactory<?>> getConsequenceFactories() {
        return consequenceFactories;
    }
}
