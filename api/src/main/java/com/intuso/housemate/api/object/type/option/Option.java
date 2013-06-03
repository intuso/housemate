package com.intuso.housemate.api.object.type.option;

import com.intuso.housemate.api.object.BaseObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public interface Option extends BaseObject<OptionListener> {
    public List<String> getSubTypes();
}
