package com.intuso.housemate.sample.plugin.ioc;

import com.google.inject.Inject;
import com.intuso.housemate.plugin.api.*;
import com.intuso.housemate.sample.plugin.CustomArg;
import com.intuso.housemate.sample.plugin.comparator.ComplexLocationComparator;
import com.intuso.housemate.sample.plugin.comparator.LocationComparator;
import com.intuso.housemate.sample.plugin.condition.DaylightCondition;
import com.intuso.housemate.sample.plugin.device.CustomDevice;
import com.intuso.housemate.sample.plugin.task.DoYourThingTask;
import com.intuso.housemate.sample.plugin.type.LocationType;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

@Types({LocationType.class}) // types
@Comparators({LocationComparator.class, ComplexLocationComparator.class}) // comparators
@Devices({CustomDevice.class}) // devices
@Conditions({DaylightCondition.class}) // conditions
@Tasks({DoYourThingTask.class}) // tasks
public class SamplePluginModule extends PluginModule {

    @Inject
    public SamplePluginModule(Log log, WriteableMapPropertyRepository properties) {
        super(log);
        // setup default prop values
        properties.set(CustomArg.PROP_KEY, "your value"); // value
    }

    @Override
    public void configure() {
        // bind plugin-specific things here
    }
}
