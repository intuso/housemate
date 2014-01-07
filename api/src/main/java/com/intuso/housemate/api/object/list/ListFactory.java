package com.intuso.housemate.api.object.list;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 * Factory for lists
 */
public interface ListFactory<
            SWBL extends HousemateData<?>,
            SWR extends HousemateObject<? extends SWBL, ?, ?, ?>,
            L extends List<SWR>>
        extends HousemateObjectFactory<ListData<SWBL>, L> {
}
