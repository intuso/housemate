package com.intuso.housemate.core.object.type.option;

import com.intuso.housemate.core.object.BaseObject;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public interface Option extends BaseObject<OptionListener> {
    public String getName();
    public String getDescription();
}
