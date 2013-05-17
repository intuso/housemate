package com.intuso.housemate.core.object.consequence;

import com.intuso.housemate.core.object.ObjectListener;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 04/03/12
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public interface ConsequenceListener<C extends Consequence<?, ?, ?, ?, ?>>
        extends ObjectListener {

    /**
     * Called when a consequence starts/stops executing
     * @param consequence the conseqeunce that has started/stopped execution
     * @param executing true if the condition is now executing
     */
    public void consequenceExecuting(C consequence, boolean executing);

    /**
     * Called when a consequence is in error (or not)
     * @param consequence the consequence that is in error (or not)
     * @param error description of the error or null if not in error
     */
    public void consequenceError(C consequence, String error);
}
