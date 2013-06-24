package com.intuso.housemate.broker;

import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.basic.Values;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.DoubleType;

/**
 */
public class TestDevice extends RealDevice {

    @Values
    public MyValues values;

    public TestDevice(RealResources resources, String id, String name, String description) {
        super(resources, id, name, description);
    }

    public interface MyValues {
        @Value(id = "dv", name = "DV", description = "DV", type = DoubleType.class)
        public void doubleValue(double value);
    }
}
