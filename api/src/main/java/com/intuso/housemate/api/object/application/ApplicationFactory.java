package com.intuso.housemate.api.object.application;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for automations
 */
public interface ApplicationFactory<A extends Application<?, ?, ?, ?, ?>> extends HousemateObjectFactory<ApplicationData, A> {
}
