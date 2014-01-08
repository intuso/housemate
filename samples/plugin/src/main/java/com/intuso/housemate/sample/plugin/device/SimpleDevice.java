package com.intuso.housemate.sample.plugin.device;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.basic.Parameter;
import com.intuso.housemate.annotations.basic.Property;
import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.basic.Values;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.sample.plugin.type.Location;
import com.intuso.utilities.log.Log;

/**
 * Example device with a standard constructor that can be used with the
 * {@link com.intuso.housemate.annotations.plugin.Devices} annotation
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginModule
 */
public class SimpleDevice extends RealDevice {

    @Values
    public MyValues values;

    @Property(id = "my-property", name = "My Property", description = "Property to control me", typeId = "double")
    public double myProperty = 1.0;

    public SimpleDevice(Log log, String id, String name, String description) {
        super(log, id, name, description);
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
