package com.intuso.housemate.server;

import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.basic.Values;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;

/**
 */
public class TestDevice extends RealDevice {

    @Values
    public MyValues values;

    public TestDevice(RealResources resources, String id, String name, String description) {
        super(resources, id, name, description);
    }

    public interface MyValues {
        @Value(id = "dv", name = "DV", description = "DV", typeId = "double")
        public void doubleValue(double value);
    }
}
