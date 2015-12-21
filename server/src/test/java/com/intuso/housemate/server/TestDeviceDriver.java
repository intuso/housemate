package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.Feature;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.client.real.api.internal.annotations.Values;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import org.slf4j.Logger;

@TypeInfo(id = "test-device", name = "Test Device", description = "Test Device")
public class TestDeviceDriver implements DeviceDriver {

    public TestFeature.MyValues values;

    @Inject
    public TestDeviceDriver(@Assisted Logger logger, @Assisted DeviceDriver.Callback callback) {}

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Feature
    @TypeInfo(id = "feature", name = "Feature", description = "Feature")
    interface TestFeature {

        @Values
        interface MyValues {
            @Value("double")
            @TypeInfo(id = "value", name = "Value", description = "Value")
            void doubleValue(double value);
        }
    }
}
