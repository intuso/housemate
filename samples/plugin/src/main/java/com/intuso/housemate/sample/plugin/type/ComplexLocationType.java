package com.intuso.housemate.sample.plugin.type;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;

public class ComplexLocationType extends RealCompoundType<Location> {

    public final static String ID = "complex-location-type";
    public final static String NAME = "Complex Location Type";
    public final static String DESCRIPTION = "A location type that is complex";

    public ComplexLocationType(RealResources resources, Object extraArg) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1, /* Add sub types here */ Lists.<RealSubType<?>>newArrayList());
    }

    @Override
    public TypeInstance serialise(Location o) {
        // this is not a real type, just a demo of how to include complex types in a plugin
        return null;
    }

    @Override
    public Location deserialise(TypeInstance instance) {
        // this is not a real type, just a demo of how to include complex types in a plugin
        return null;
    }
}
