package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.annotations.Value;
import com.intuso.housemate.object.real.annotations.Values;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class TestDevice extends RealDevice {

    @Values
    public MyValues values;

    @Inject
    public TestDevice(Log log,
                      ListenersFactory listenersFactory,
                      @Assisted DeviceData data) {
        super(log, listenersFactory, "test", data);
    }

    public interface MyValues {
        @Value(id = "dv", name = "DV", description = "DV", typeId = "double")
        public void doubleValue(double value);
    }
}
