package com.intuso.housemate.core.object.type;

import com.intuso.housemate.core.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public interface HasTypes<L extends List<? extends Type>> {
    public L getTypes();
}
