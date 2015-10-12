package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.client.real.api.internal.annotations.Values;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;

/**
 */
public class TestDeviceDriver implements DeviceDriver {

    @Values
    public MyValues values;

    @Inject
    public TestDeviceDriver(@Assisted DeviceDriver.Callback callback) {}

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public interface MyValues {
        @Value(id = "dv", name = "DV", description = "DV", typeId = "double")
        void doubleValue(double value);
    }
}
