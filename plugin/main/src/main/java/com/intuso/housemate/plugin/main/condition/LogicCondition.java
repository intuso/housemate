package com.intuso.housemate.plugin.main.condition;

import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import org.slf4j.Logger;

import java.util.Map;

public abstract class LogicCondition implements ConditionDriver {

    @Override
    public boolean hasChildConditions() {
        return true;
    }

    @Override
    public void init(Logger logger, ConditionDriver.Callback callback) {
        callback.conditionSatisfied(checkIfSatisfied(callback.getChildSatisfied()));
    }

    @Override
    public void uninit() {
        // do nothing
    }

    /**
     * Checks if this condition is satisfied, given the satisfied state of the children
     * @param satisfiedMap the map of children to their satisfied value
     * @return true if this condition is currently satisfied
     */
    protected abstract boolean checkIfSatisfied(Map<String, Boolean> satisfiedMap);
}

