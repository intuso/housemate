package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 04/03/12
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public interface ConditionListener<C extends Condition<?, ?, ?, ?, ?, ?>> extends ObjectListener {

    /**
     * Called when a condition becomes (un)satisfied
     * @param condition the now (un)satisfied condition
     * @param satisfied true if the condition is now satisfied
     */
    public void conditionSatisfied(C condition, boolean satisfied);

    /**
     * Called when a condition is in error (or not)
     * @param condition the condition that is in error (or not)
     * @param error description of the error or null if not in error
     */
    public void conditionError(C condition, String error);
}
