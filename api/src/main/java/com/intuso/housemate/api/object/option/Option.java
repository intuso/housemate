package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.subtype.HasSubTypes;
import com.intuso.housemate.api.object.subtype.SubType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public interface Option<STL extends List<? extends SubType<?>>> extends BaseObject<OptionListener>, HasSubTypes<STL> {
    public final static String SUB_TYPES = "sub-types";
}
