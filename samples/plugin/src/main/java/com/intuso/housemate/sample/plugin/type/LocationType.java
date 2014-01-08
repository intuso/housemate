package com.intuso.housemate.sample.plugin.type;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;
import com.intuso.utilities.log.Log;

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

    @Inject
    protected LocationType(Log log, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        super(log, ID, NAME, DESCRIPTION, 1, 1,
                makeLatitudeSubType(log, types), makeLongitudeSubType(log, types));
    }

    private static RealSubType<Double> makeLatitudeSubType(Log log,
                                                           RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        return new RealSubType<Double>(log, LATITUDE_ID, LATITUDE_NAME, LATITUDE_DESCRIPTION,
                SimpleTypeData.Type.Double.getId(), types);
    }

    private static RealSubType<Double> makeLongitudeSubType(Log log,
                                                            RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        return new RealSubType<Double>(log, LONGITUDE_ID, LONGITUDE_NAME, LONGITUDE_DESCRIPTION,
                SimpleTypeData.Type.Double.getId(), types);
    }

    @Override
    public TypeInstance serialise(Location location) {
        TypeInstance result = new TypeInstance();
        result.getChildValues().put(LATITUDE_ID, new TypeInstances(new TypeInstance(Double.toString(location.getLatitiude()))));
        result.getChildValues().put(LONGITUDE_ID, new TypeInstances(new TypeInstance(Double.toString(location.getLongitude()))));
        return result;
    }

    @Override
    public Location deserialise(TypeInstance instance) {
        if(instance == null
                || instance.getChildValues().get(LATITUDE_ID) == null
                || instance.getChildValues().get(LONGITUDE_ID) == null)
            return null;
        try {
            return new Location(Double.parseDouble(instance.getChildValues().get(LATITUDE_ID).getFirstValue()),
                    Double.parseDouble(instance.getChildValues().get(LONGITUDE_ID).getFirstValue()));
        } catch(NumberFormatException e) {
            getLog().e("Failed to deserialise location lat/long to double form");
            return null;
        }
    }
}
