package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.FeatureId;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.client.real.api.internal.annotations.Values;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;

/**
 */
public class TestDeviceDriver implements DeviceDriver {

    @Values
    public TestFeature.MyValues values;

    @Inject
    public TestDeviceDriver(@Assisted DeviceDriver.Callback callback) {}

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @FeatureId("feature")
    interface TestFeature {
        interface MyValues {
            @Value(id = "value", name = "Value", description = "Value", typeId = "double")
            void doubleValue(double value);
        }
    }
}
