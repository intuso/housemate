package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.primary.PrimaryListener;

/**
 * Interface for classes wishing to listen to a condition
 * @author tclabon
 *
 */
public interface AutomationListener<A extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends PrimaryListener<A> {
    public void satisfied(A automation, boolean satisfied);
}