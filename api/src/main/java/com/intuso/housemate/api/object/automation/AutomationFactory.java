package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for automations
 */
public interface AutomationFactory<
            A extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<AutomationData, A> {
}
