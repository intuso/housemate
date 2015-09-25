package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.housemate.object.api.internal.Root;

/**
 * Created by tomc on 15/09/15.
 */
public interface ClientRoot<LISTENER extends ClientRoot.Listener<? super ROOT>,
        ROOT extends ClientRoot<?, ?>>
        extends Root<LISTENER, ROOT>, Message.Sender, Message.Receiver<Message.Payload> {

    Application.Status getApplicationStatus();
    ApplicationInstance.Status getApplicationInstanceStatus();

    /**
     * Logs in to the server
     * @param applicationDetails
     * @param component
     *
     */
    void register(ApplicationDetails applicationDetails, String component);

    /**
     * Logs out of the server
     */
    void unregister();

    interface Listener<ROOT extends ClientRoot<?, ?>> extends Root.Listener<ROOT> {
        void serverConnectionStatusChanged(ROOT root, ServerConnectionStatus serverConnectionStatus);

        void applicationStatusChanged(ROOT root, Application.Status applicationStatus);

        void applicationInstanceStatusChanged(ROOT root, ApplicationInstance.Status applicationInstanceStatus);

        void newApplicationInstance(ROOT root, String instanceId);

        /**
         * Notifies when the instance of the server has changed
         * @param root the root object the listener was attached to
         * @param serverId
         */
        void newServerInstance(ROOT root, String serverId);
    }
}
