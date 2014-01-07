package com.intuso.housemate.sample.plugin;

import com.intuso.housemate.annotations.plugin.*;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.housemate.plugin.api.ServerTaskFactory;
import com.intuso.housemate.sample.plugin.comparator.ComplexLocationComparator;
import com.intuso.housemate.sample.plugin.comparator.LocationComparator;
import com.intuso.housemate.sample.plugin.condition.ComplexConditionFactory;
import com.intuso.housemate.sample.plugin.condition.DaylightCondition;
import com.intuso.housemate.sample.plugin.condition.ReallyComplexConditionFactory;
import com.intuso.housemate.sample.plugin.device.ComplexDeviceFactory;
import com.intuso.housemate.sample.plugin.device.ReallyComplexDeviceFactory;
import com.intuso.housemate.sample.plugin.device.SimpleDevice;
import com.intuso.housemate.sample.plugin.task.ComplexTaskFactory;
import com.intuso.housemate.sample.plugin.task.DoYourThingTask;
import com.intuso.housemate.sample.plugin.task.ReallyComplexTaskFactory;
import com.intuso.housemate.sample.plugin.type.ComplexLocationType;
import com.intuso.housemate.sample.plugin.type.LocationType;
import com.intuso.utilities.log.Log;

import java.util.List;

@Types({LocationType.class}) // for types with simple constructors
@Comparators({LocationComparator.class}) // for comparators with simple constructors
@Devices({SimpleDevice.class}) // for devices with simple constructors
@DeviceFactories({ComplexDeviceFactory.class}) // for device factories with simple constructors
@Conditions({DaylightCondition.class}) // for conditions with simple constructors
@ConditionFactories({ComplexConditionFactory.class}) // for condition factories with simple constructors
@Tasks({DoYourThingTask.class}) // for tasks with simple constructors
@TaskFactories({ComplexTaskFactory.class}) // for task factories with simple constructors
public class SamplePlugin extends AnnotatedPluginDescriptor {

    private final Object complexArg = new Object();

    /**
     * Override to add types that do not have simple constructors
     */
    @Override
    public List<RealType<?, ?, ?>> getTypes(Log log) {
        List<RealType<?, ?, ?>> result = super.getTypes(log);
        Object extraArg = new Object();
        result.add(new ComplexLocationType(log, extraArg));
        return result;
    }

    /**
     * Override to add comparators that do not have simple constructors
     */
    @Override
    public List<Comparator<?>> getComparators(Log log) {
        List<Comparator<?>> result = super.getComparators(log);
        result.add(new ComplexLocationComparator(complexArg));
        return result;
    }

    /**
     * Override to add device factories that do not have simple constructors
     */
    @Override
    public List<RealDeviceFactory<?>> getDeviceFactories() {
        List<RealDeviceFactory<?>> result = super.getDeviceFactories();
        result.add(new ReallyComplexDeviceFactory(complexArg));
        return result;
    }

    /**
     * Override to add condition factories that do not have simple constructors
     */
    @Override
    public List<ServerConditionFactory<?>> getConditionFactories() {
        List<ServerConditionFactory<?>> result = super.getConditionFactories();
        result.add(new ReallyComplexConditionFactory(complexArg));
        return result;
    }

    /**
     * Override to add task factories that do not have simple constructors
     */
    @Override
    public List<ServerTaskFactory<?>> getTaskFactories() {
        List<ServerTaskFactory<?>> result = super.getTaskFactories();
        result.add(new ReallyComplexTaskFactory(complexArg));
        return result;
    }
}
