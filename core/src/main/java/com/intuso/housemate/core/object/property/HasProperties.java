package com.intuso.housemate.core.object.property;

import com.intuso.housemate.core.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public interface HasProperties<L extends List<? extends Property<?, ?, ?>>> {
    public L getProperties();
}
