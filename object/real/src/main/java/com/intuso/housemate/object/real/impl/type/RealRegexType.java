package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.RegexTypeWrappable;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

/**
 */
public abstract class RealRegexType<O> extends RealType<RegexTypeWrappable, NoChildrenWrappable, O> {
    protected RealRegexType(RealResources resources, String id, String name, String description, String regexPattern) {
        super(resources, new RegexTypeWrappable(id, name, description, regexPattern));
    }
}
