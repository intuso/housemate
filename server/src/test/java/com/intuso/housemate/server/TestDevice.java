package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.client.real.api.internal.annotations.Values;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
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
        void doubleValue(double value);
    }
}
