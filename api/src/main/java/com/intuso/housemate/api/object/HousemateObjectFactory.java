package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 19/02/13
 * Time: 07:40
 * To change this template use File | Settings | File Templates.
 */
public interface HousemateObjectFactory<
            R extends Resources,
            WBL extends HousemateObjectWrappable<?>,
            O extends BaseObject<?>> {
    public O create(R resources, WBL wrappable) throws HousemateException;
}
