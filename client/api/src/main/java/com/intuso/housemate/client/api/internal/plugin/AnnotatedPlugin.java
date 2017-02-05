package com.intuso.housemate.client.api.internal.plugin;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.api.internal.driver.TaskDriver;

/**
 * Base class for all plugins that wish to use annotations to describe the provided features
 */
public abstract class AnnotatedPlugin implements Plugin {

    @Override
    public Id getId() {
        return getClass().getAnnotation(Id.class);
    }

    @Override
    public Iterable<ChoiceType> getChoiceTypes() {
        ChoiceTypes types = getClass().getAnnotation(ChoiceTypes.class);
        if(types == null)
            return Lists.newArrayList();
        return Lists.newArrayList(types.value());
    }

    @Override
    public Iterable<Class<?>> getCompositeTypes() {
        CompositeTypes types = getClass().getAnnotation(CompositeTypes.class);
        if(types == null)
            return Lists.newArrayList();
        return Lists.newArrayList(types.value());
    }

    @Override
    public Iterable<DoubleRangeType> getDoubleRangeTypes() {
        DoubleRangeTypes types = getClass().getAnnotation(DoubleRangeTypes.class);
        if(types == null)
            return Lists.newArrayList();
        return Lists.newArrayList(types.value());
    }

    @Override
    public Iterable<IntegerRangeType> getIntegerRangeTypes() {
        IntegerRangeTypes types = getClass().getAnnotation(IntegerRangeTypes.class);
        if(types == null)
            return Lists.newArrayList();
        return Lists.newArrayList(types.value());
    }

    @Override
    public Iterable<RegexType> getRegexTypes() {
        RegexTypes types = getClass().getAnnotation(RegexTypes.class);
        if(types == null)
            return Lists.newArrayList();
        return Lists.newArrayList(types.value());
    }

    @Override
    public Iterable<Class<? extends ConditionDriver>> getConditionDrivers() {
        ConditionDrivers taskDrivers = getClass().getAnnotation(ConditionDrivers.class);
        if(taskDrivers == null)
            return Lists.newArrayList();
        return Lists.newArrayList(taskDrivers.value());
    }

    @Override
    public Iterable<Class<? extends HardwareDriver>> getHardwareDrivers() {
        HardwareDrivers taskDrivers = getClass().getAnnotation(HardwareDrivers.class);
        if(taskDrivers == null)
            return Lists.newArrayList();
        return Lists.newArrayList(taskDrivers.value());
    }

    @Override
    public Iterable<Class<? extends TaskDriver>> getTaskDrivers() {
        TaskDrivers taskDrivers = getClass().getAnnotation(TaskDrivers.class);
        if(taskDrivers == null)
            return Lists.newArrayList();
        return Lists.newArrayList(taskDrivers.value());
    }
}
