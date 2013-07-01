package com.intuso.housemate.sample.plugin.comparator;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonOperator;
import com.intuso.housemate.sample.plugin.type.Location;
import com.intuso.housemate.sample.plugin.type.LocationType;

public class LocationComparator implements Comparator<Location> {

    private final LocationOperator operator = new LocationOperator();

    @Override
    public ComparisonOperator getOperator() {
        return operator;
    }

    @Override
    public String getTypeId() {
        return LocationType.ID;
    }

    @Override
    public boolean compare(Location first, Location second) throws HousemateException {
        // do some comparison between locations here. Depends on what you're comparing ... eg Equal?
        return true;
    }
}
