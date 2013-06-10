package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public interface HasSubTypes<L extends List<? extends SubType<?>>> {
    public L getSubTypes();
}
