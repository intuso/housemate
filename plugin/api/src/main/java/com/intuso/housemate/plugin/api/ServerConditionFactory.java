package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;

/**
 * @param <CONDITION> the type of the conditions created by this factory
 */
public interface ServerConditionFactory<CONDITION extends ServerRealCondition> {

    /**
     * Creates a condition
     * @param data the condition's data
     * @param owner the owner that will be called if the condition is deleted
     * @return a new condition
     */
    public CONDITION create(ConditionData data, ServerRealConditionOwner owner);
}
