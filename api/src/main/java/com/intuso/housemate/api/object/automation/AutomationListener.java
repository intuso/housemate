package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.primary.PrimaryListener;

/**
 *
 * Listener interface for automations
 */
public interface AutomationListener<A extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends PrimaryListener<A> {

    /**
     * Notifies that the automation's root condition is (un)satisfied
     * @param automation the automation that became (un)satisfied
     * @param satisfied true if now satisfied
     */
    public void satisfied(A automation, boolean satisfied);
}