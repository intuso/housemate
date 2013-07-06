package com.intuso.housemate.api.object.list;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 * Factory for lists
 */
public interface ListFactory<
            R extends Resources,
            SWBL extends HousemateData<?>,
            SWR extends HousemateObject<?, ? extends SWBL, ?, ?, ?>,
            L extends List<SWR>>
        extends HousemateObjectFactory<R, ListData<SWBL>, L> {
}
