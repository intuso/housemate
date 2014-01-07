package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.RegexTypeData;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.log.Log;

/**
 * Type for text input restricted to text that matches a regex
 */
public abstract class RealRegexType<O> extends RealType<RegexTypeData, NoChildrenData, O> {

    /**
     * @param log the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param regexPattern the regex pattern that values must match
     */
    protected RealRegexType(Log log, String id, String name, String description, int minValues,
                            int maxValues, String regexPattern) {
        super(log, new RegexTypeData(id, name, description, minValues, maxValues, regexPattern));
    }
}
