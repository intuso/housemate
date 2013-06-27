package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.basic.Property;
import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.basic.Values;
import com.intuso.housemate.object.real.RealResources;

/**
 */
public class TestChildDevice extends TestParentDevice {

    @Values
    TestDeviceValues values;

    @Property(id = "increment-amount", name = "Increment Amount", description = "Amount to increase/decrease volume by",
              typeId = "integer")
    int incrementAmount = 0;

    int currentVolume = 0;

    public TestChildDevice(RealResources resources, String id, String name, String description) {
        super(resources, id, name, description);
    }

    @Command(id = "turn-up", name = "Turn Up", description = "Increase the volume")
    protected void turnUp() {
        currentVolume += incrementAmount;
        values.volume(currentVolume);
    }

    @Command(id = "turn-down", name = "Turn Down", description = "Decrease the volume")
    protected void turnDown() {
        currentVolume -= incrementAmount;
        values.volume(currentVolume);
    }

    protected interface TestDeviceValues {
        @Value(id="volume", name = "Volume", description = "Current volume", typeId = "integer")
        void volume(int value);
    }
}
