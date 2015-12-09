package com.intuso.housemate.client.real.impl.internal.type;

import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.RegexTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for text input restricted to text that matches a regex
 */
public abstract class RealRegexType<O> extends RealTypeImpl<RegexTypeData, NoChildrenData, O> {

    /**
     * @param logger the logger
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param regexPattern the regex pattern that values must match
     */
    protected RealRegexType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                            int maxValues, String regexPattern) {
        super(logger, listenersFactory, new RegexTypeData(id, name, description, minValues, maxValues, regexPattern));
    }
}
