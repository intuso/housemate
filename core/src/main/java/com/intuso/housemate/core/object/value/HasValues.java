package com.intuso.housemate.core.object.value;

import com.intuso.housemate.core.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 23:30
 * To change this template use File | Settings | File Templates.
 */
public interface HasValues<L extends List<? extends Value<?, ?>>> {
    public L getValues();
}
