package com.intuso.housemate.sample.plugin.comparator;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonType;
import com.intuso.housemate.sample.plugin.type.Location;

public class LocationComparator implements Comparator<RealValue<Location>> {

    private final LocationType operator = new LocationType();

    @Override
    public ComparisonType getComparisonType() {
        return operator;
    }

    @Override
    public String getTypeId() {
        return com.intuso.housemate.sample.plugin.type.LocationType.ID;
    }

    @Override
    public boolean compare(RealValue<Location> first, RealValue<Location> second) throws HousemateException {
        // do some comparison between locations here. Depends on what you're comparing ... eg Equal?
        return true;
    }
}
