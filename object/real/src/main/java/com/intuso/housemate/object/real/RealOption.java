package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.option.Option;
import com.intuso.housemate.api.object.type.option.OptionListener;
import com.intuso.housemate.api.object.type.option.OptionWrappable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class RealOption
        extends RealObject<OptionWrappable, NoChildrenWrappable, RealObject<NoChildrenWrappable, ?, ?, ?>, OptionListener>
        implements Option {

    public RealOption(RealResources resources, String id, String name, String description) {
        super(resources, new OptionWrappable(id, name,  description));
    }

    @Override
    public final List<String> getSubTypes() {
        return getWrappable().getSubTypes();
    }
}
