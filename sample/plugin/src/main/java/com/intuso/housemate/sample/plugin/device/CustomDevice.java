package com.intuso.housemate.sample.plugin.device;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.annotations.*;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.sample.plugin.type.Location;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

@TypeInfo(id = "custom-device", name = "Custom Device", description = "Device that does some custom thing")
public class CustomDevice extends RealDevice {

    @Values
    public MyValues values;

    @Property(id = "my-property", name = "My Property", description = "Property to control me", typeId = "double")
    public double myProperty = 1.0;

    @Inject
    public CustomDevice(Log log,
                        ListenersFactory listenersFactory,
                        @Assisted DeviceData data) {
        super(log, listenersFactory, "custom-device", data);
        getCustomPropertyIds().add("my-property");
    }

    @Command(id = "do-me", name = "Do Me", description = "Do me")
    public void doMe(@Parameter(id = "where", name = "Where", description = "Where to do me", typeId = "location") Location where) {
        // do something meaningful and interesting here
        values.myValue((int)myProperty);
    }

    public interface MyValues {
        @Value(id = "myValue", name = "My Value", description = "Value to show the latest value of me", typeId = "integer")
        void myValue(int value);
    }
}
