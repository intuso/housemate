package com.intuso.housemate.client.real.api.internal.driver;

import java.util.Map;

public abstract class LogicCondition implements ConditionDriver {

    private final Callback conditionCallback;

    public LogicCondition(Callback conditionCallback) {
        this.conditionCallback = conditionCallback;
    }

    @Override
    public boolean hasChildConditions() {
        return true;
    }

    @Override
    public final void start() {
        conditionCallback.conditionSatisfied(checkIfSatisfied(conditionCallback.getChildSatisfied()));
    }

    @Override
    public final void stop() {
        // do nothing
    }

    /**
     * Checks if this condition is satisfied, given the satisfied state of the children
     * @param satisfiedMap the map of children to their satisfied value
     * @return true if this condition is currently satisfied
     */
    protected abstract boolean checkIfSatisfied(Map<String, Boolean> satisfiedMap);
}
