package com.intuso.housemate.api.object.application;

import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for automations
 */
public interface ApplicationListener<A extends Application<?, ?, ?, ?, ?>> extends ObjectListener {}