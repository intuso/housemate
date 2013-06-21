package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/06/13
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public interface Comparator<O> {
    public ComparisonOperator getOperator();
    public String getTypeId();
    public boolean compare(O first, O second) throws HousemateException;
}
