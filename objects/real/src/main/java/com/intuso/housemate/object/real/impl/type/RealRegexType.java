package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.RegexTypeWrappable;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

/**
 * Type for text input restricted to text that matches a regex
 */
public abstract class RealRegexType<O> extends RealType<RegexTypeWrappable, NoChildrenWrappable, O> {

    /**
     * @param resources the resources
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param regexPattern the regex pattern that values must match
     */
    protected RealRegexType(RealResources resources, String id, String name, String description, String regexPattern) {
        super(resources, new RegexTypeWrappable(id, name, description, regexPattern));
    }
}
