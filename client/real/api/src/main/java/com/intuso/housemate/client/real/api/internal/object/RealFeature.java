package com.intuso.housemate.client.real.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Feature;
import com.intuso.housemate.client.real.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginDependency;

/**
 * Base class for all devices
 */
public interface RealFeature<COMMAND extends RealCommand<?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DRIVER_PROPERTY extends RealProperty<PluginDependency<FeatureDriver.Factory<?>>, ?, ?, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        FEATURE extends RealFeature<COMMAND, COMMANDS, BOOLEAN_VALUE, STRING_VALUE, VALUES, DRIVER_PROPERTY, PROPERTIES, FEATURE>>
        extends Feature<COMMAND,
        COMMAND,
        COMMAND,
        BOOLEAN_VALUE,
        STRING_VALUE,
        DRIVER_PROPERTY,
        BOOLEAN_VALUE,
        COMMANDS,
        VALUES,
        PROPERTIES,
        FEATURE>,
        FeatureDriver.Callback {

    <DRIVER extends FeatureDriver> DRIVER getDriver();

    boolean isDriverLoaded();

    interface Container<FEATURE extends RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, FEATURES extends RealList<? extends FEATURE, ?>> extends Feature.Container<FEATURES>, RemoveCallback<FEATURE> {
        void addFeature(FEATURE feature);
    }

    interface RemoveCallback<FEATURE extends RealFeature<?, ?, ?, ?, ?, ?, ?, ?>> {
        void removeFeature(FEATURE feature);
    }
}
