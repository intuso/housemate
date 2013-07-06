package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for automations
 */
public interface AutomationFactory<
            R extends Resources,
            A extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<R, AutomationData, A> {
}
