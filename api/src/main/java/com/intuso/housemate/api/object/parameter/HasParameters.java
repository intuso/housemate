package com.intuso.housemate.api.object.parameter;

import com.intuso.housemate.api.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
public interface HasParameters<L extends List<? extends Parameter<?>>> {
    public L getParameters();
}
