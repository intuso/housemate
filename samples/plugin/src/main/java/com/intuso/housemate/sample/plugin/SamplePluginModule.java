package com.intuso.housemate.sample.plugin;

import com.google.inject.Inject;
import com.intuso.housemate.annotations.plugin.*;
import com.intuso.housemate.sample.plugin.comparator.ComplexLocationComparator;
import com.intuso.housemate.sample.plugin.comparator.LocationComparator;
import com.intuso.housemate.sample.plugin.condition.ComplexConditionFactory;
import com.intuso.housemate.sample.plugin.condition.DaylightCondition;
import com.intuso.housemate.sample.plugin.device.ComplexDeviceFactory;
import com.intuso.housemate.sample.plugin.device.SimpleDevice;
import com.intuso.housemate.sample.plugin.task.ComplexTaskFactory;
import com.intuso.housemate.sample.plugin.task.DoYourThingTask;
import com.intuso.housemate.sample.plugin.type.LocationType;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;
import com.intuso.utilities.properties.api.PropertyValue;

@Types({LocationType.class}) // types
@Comparators({LocationComparator.class, ComplexLocationComparator.class}) // comparators
@Devices({SimpleDevice.class}) // for devices with standard constructor args
@DeviceFactories({ComplexDeviceFactory.class}) // factories for devices with non-standard constructor args
@Conditions({DaylightCondition.class}) // for conditions with standard constructor args
@ConditionFactories({ComplexConditionFactory.class}) // factories for conditions non-standard constructor args
@Tasks({DoYourThingTask.class}) // for tasks with standard constructor args
@TaskFactories({ComplexTaskFactory.class}) // factories for tasks with non-standard constructor args
public class SamplePluginModule extends AnnotatedPluginModule {

    @Inject
    public SamplePluginModule(Log log, PropertyContainer properties) {
        super(log);
        // setup default prop values
        properties.set(CustomArg.PROP_KEY, new PropertyValue(
                "default", // default source
                0, // low priority so any config file or command line settings take precedence
                "value")); // value
    }

    @Override
    public void configure() {
        // bind plugin-specific things here
    }
}
