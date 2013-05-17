package com.intuso.housemate.core.object.list;

import com.intuso.housemate.core.object.HousemateObject;
import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.resources.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 11/02/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public interface ListFactory<
            R extends Resources,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends HousemateObject<?, ? extends SWBL, ?, ?, ?>,
            L extends List<SWR>>
        extends HousemateObjectFactory<R, ListWrappable<SWBL>, L> {
}
