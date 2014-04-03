package com.intuso.housemate.api.object.application.instance;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for automations
 */
public interface ApplicationInstanceFactory<A extends ApplicationInstance<?, ?, ?>> extends HousemateObjectFactory<ApplicationInstanceData, A> {
}
