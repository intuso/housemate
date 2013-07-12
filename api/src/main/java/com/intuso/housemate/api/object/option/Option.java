package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.subtype.HasSubTypes;
import com.intuso.housemate.api.object.subtype.SubType;

/**
 * @param <SUB_TYPES> the type of the sub types list
 */
public interface Option<SUB_TYPES extends List<? extends SubType<?>>>
        extends BaseHousemateObject<OptionListener>, HasSubTypes<SUB_TYPES> {
    public final static String SUB_TYPES_ID = "sub-types";
}
