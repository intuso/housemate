package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;

/**
 */
public interface Comparator<O> {
    public ComparisonOperator getOperator();
    public String getTypeId();
    public boolean compare(O first, O second) throws HousemateException;
}
