package com.intuso.housemate.client.real.api.internal.impl.type;

import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.RegexTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Type for text input restricted to text that matches a regex
 */
public abstract class RealRegexType<O> extends RealType<RegexTypeData, NoChildrenData, O> {

    /**
     * @param log the log
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param regexPattern the regex pattern that values must match
     */
    protected RealRegexType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                            int maxValues, String regexPattern) {
        super(log, listenersFactory, new RegexTypeData(id, name, description, minValues, maxValues, regexPattern));
    }
}
