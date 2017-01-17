package com.intuso.housemate.client.api.internal.plugin;

import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.api.internal.driver.TaskDriver;

/**
 * Created by tomc on 16/01/17.
 */
public interface Plugin {
    Id getId();
    Iterable<ChoiceType> getChoiceTypes();
    Iterable<Class<?>> getCompositeTypes();
    Iterable<DoubleRangeType> getDoubleRangeTypes();
    Iterable<IntegerRangeType> getIntegerRangeTypes();
    Iterable<RegexType> getRegexTypes();
    Iterable<Class<? extends ConditionDriver>> getConditionDrivers();
    Iterable<Class<? extends FeatureDriver>> getFeatureDrivers();
    Iterable<Class<? extends HardwareDriver>> getHardwareDrivers();
    Iterable<Class<? extends TaskDriver>> getTaskDrivers();
}

