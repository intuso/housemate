package com.intuso.housemate.sample.plugin.type;

import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;

public class LocationType extends RealCompoundType<Location> {

    public final static String ID = "location";
    public final static String NAME = "Location";
    public final static String DESCRIPTION = "A location specified by a latitude and longitude";

    public final static String LATITUDE_ID = "latitude";
    public final static String LATITUDE_NAME = "Latitude";
    public final static String LATITUDE_DESCRIPTION = "The location's latitude";

    public final static String LONGITUDE_ID = "longitude";
    public final static String LONGITUDE_NAME = "Longitude";
    public final static String LONGITUDE_DESCRIPTION = "The location's longitude";

    protected LocationType(RealResources resources) {
        super(resources, ID, NAME, DESCRIPTION, makeLatitudeSubType(resources), makeLongitudeSubType(resources));
    }

    private static RealSubType<Double> makeLatitudeSubType(RealResources resources) {
        return new RealSubType<Double>(resources, LATITUDE_ID, LATITUDE_NAME, LATITUDE_DESCRIPTION, new DoubleType(resources));
    }

    private static RealSubType<Double> makeLongitudeSubType(RealResources resources) {
        return new RealSubType<Double>(resources, LONGITUDE_ID, LONGITUDE_NAME, LONGITUDE_DESCRIPTION, new DoubleType(resources));
    }

    @Override
    public TypeInstance serialise(Location location) {
        TypeInstance result = new TypeInstance();
        result.getChildValues().put(LATITUDE_ID, new TypeInstance(Double.toString(location.getLatitiude())));
        result.getChildValues().put(LONGITUDE_ID, new TypeInstance(Double.toString(location.getLongitude())));
        return result;
    }

    @Override
    public Location deserialise(TypeInstance instance) {
        if(instance == null
                || instance.getChildValues().get(LATITUDE_ID) == null
                || instance.getChildValues().get(LONGITUDE_ID) == null)
            return null;
        try {
            return new Location(Double.parseDouble(instance.getChildValues().get(LATITUDE_ID).getValue()),
                    Double.parseDouble(instance.getChildValues().get(LONGITUDE_ID).getValue()));
        } catch(NumberFormatException e) {
            getLog().e("Failed to deserialise location lat/long to double form");
            return null;
        }
    }
}
