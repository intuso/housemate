package com.intuso.housemate.client.real.impl.internal.type;

import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for text input restricted to text that matches a regex
 */
public abstract class RealRegexType<O> extends RealTypeImpl<O> {

    /**
     * @param logger the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param listenersFactory
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param regexPattern the regex pattern that values must match
     */
    protected RealRegexType(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, int minValues,
                            int maxValues, String regexPattern) {
        super(logger, new RegexData(id, name, description, minValues, maxValues, regexPattern), listenersFactory);
    }
}
