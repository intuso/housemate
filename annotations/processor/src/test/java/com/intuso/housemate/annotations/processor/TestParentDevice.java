package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.basic.Parameter;
import com.intuso.housemate.annotations.basic.Property;
import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.basic.Values;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;

/**
 */
public class TestParentDevice extends RealDevice {

    @Values
    TestDeviceValues values;

    @Property(id = "switch-number", name = "Switch Number", description = "Switch number", typeId = "integer")
    int switchNumber = 0;

    public TestParentDevice(RealResources resources, String id, String name, String description) {
        super(resources, id, name, description);
    }

    @Command(id = "command", name = "Command", description = "Command to do something")
    public void command(@Parameter(id="on", name="On", description = "True to turn on", typeId = "boolean") boolean on) {
        values.lastCommand((on ? "On " : "Off ") + switchNumber);
    }

    protected interface TestDeviceValues {
        @Value(id = "last-command", name = "Last Command", description = "Last command performed", typeId = "string")
        void lastCommand(String value);
    }
}
