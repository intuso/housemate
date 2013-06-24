package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for options
 */
public interface ConditionListener<C extends Condition<?, ?, ?, ?, ?, ?>> extends ObjectListener {

    /**
     * Notifies that a condition has become (un)satisfied
     * @param condition the now (un)satisfied condition
     * @param satisfied true if the condition is now satisfied
     */
    public void conditionSatisfied(C condition, boolean satisfied);

    /**
     * Notifies that a condition is in error (or not)
     * @param condition the condition that is in error (or not)
     * @param error description of the error or null if not in error
     */
    public void conditionError(C condition, String error);
}
