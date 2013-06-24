package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;
import com.intuso.housemate.web.client.event.CredentialsSubmittedEvent;

/**
 */
public interface CredentialsSubmittedHandler extends EventHandler {
    public void onCredentialsSubmitted(CredentialsSubmittedEvent event);
}
