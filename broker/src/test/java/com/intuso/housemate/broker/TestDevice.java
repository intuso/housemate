package com.intuso.housemate.broker;

import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.basic.Values;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.DoubleType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 21/06/13
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
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
