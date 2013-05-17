package com.intuso.housemate.real;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.type.option.Option;
import com.intuso.housemate.core.object.type.option.OptionListener;
import com.intuso.housemate.core.object.type.option.OptionWrappable;

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
}
