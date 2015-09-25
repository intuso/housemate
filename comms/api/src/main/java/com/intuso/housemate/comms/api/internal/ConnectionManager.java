package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.housemate.comms.api.internal.payload.NoPayload;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 *
 * Utility class for managing a connection to a server. Handles server messages, reconnecting etc and calls the
 * appropriate callbacks as state changes
 */
public class ConnectionManager {

    public final static String APPLICATION_INSTANCE_ID = "application.instance.id";
    public final static String[] ROOT_PATH = new String[] {""};

    private final Listeners<ConnectionListener> listeners;

    private final PropertyRepository properties;
    private final ApplicationRegistration.ClientType clientType;
    private final Message.Sender sender;

    private String serverInstanceId = null;

    private ServerConnectionStatus serverConnectionStatus = ServerConnectionStatus.DisconnectedPermanently;
    private Application.Status applicationStatus = Application.Status.Unregistered;
    private ApplicationInstance.Status applicationInstanceStatus = ApplicationInstance.Status.Unregistered;

    /**
     * @param listenersFactory
     * @param clientType the type of the client's connection
     * @param sender
     */
    public ConnectionManager(ListenersFactory listenersFactory, PropertyRepository properties, ApplicationRegistration.ClientType clientType, Message.Sender sender) {
        this.listeners = listenersFactory.create();
        this.properties = properties;
        this.clientType = clientType;
        this.sender = sender;
    }

    /**
     * Adds listener to be notified of status changes
     * @param listener the listener to add
     * @return listener registration
     */
    public ListenerRegistration addStatusChangeListener(ConnectionListener listener) {
        return listeners.addListener(listener);
    }

    public ServerConnectionStatus getServerConnectionStatus() {
        return serverConnectionStatus;
    }

    /**
     * Gets the current application status
     * @return the current connections application status
     */
    public Application.Status getApplicationStatus() {
        return applicationStatus;
    }

    /**
     * Gets the current application instance status
     * @return the current connections application instance status
     */
    public ApplicationInstance.Status getApplicationInstanceStatus() {
        return applicationInstanceStatus;
    }

    /**
     * Requests access to the server
     */
    public void register(ApplicationDetails applicationDetails, String component) {
        switch (serverConnectionStatus) {
            case DisconnectedPermanently:
            case Connecting:
            case ConnectedToRouter:
                throw new HousemateCommsException("Cannot request access until a server connection has been established");
            case ConnectedToServer:
            case DisconnectedTemporarily:
                if(applicationInstanceStatus != ApplicationInstance.Status.Unregistered)
                    throw new HousemateCommsException("Registration already in progress or done");
                else if(applicationDetails != null) {
                    setApplicationInstanceStatus(ApplicationInstance.Status.Registering);
                    sender.sendMessage(new Message<>(ROOT_PATH, ApplicationRegistration.APPLICATION_REGISTRATION_TYPE,
                            new ApplicationRegistration(applicationDetails, properties.get(APPLICATION_INSTANCE_ID), component, clientType)));
                } else
                    throw new HousemateCommsException("Null application or instance details");
        }
    }

    /**
     * Logs out of the server
     */
    public void unregister() {
        sender.sendMessage(new Message<>(ROOT_PATH, ApplicationRegistration.APPLICATION_UNREGISTRATION_TYPE, NoPayload.INSTANCE));
        properties.remove(APPLICATION_INSTANCE_ID);
        setApplicationStatus(Application.Status.Unregistered);
        setApplicationInstanceStatus(ApplicationInstance.Status.Unregistered);
    }

    public void setServerInstanceId(String serverInstanceId) {
        if(this.serverInstanceId != null && !this.serverInstanceId.equals(serverInstanceId)) {
            for(ConnectionListener listener : listeners)
                listener.newServerInstance(serverInstanceId);
            setApplicationStatus(Application.Status.Unregistered);
            setApplicationInstanceStatus(ApplicationInstance.Status.Unregistered);
        }
        this.serverInstanceId = serverInstanceId;
    }

    public void setApplicationInstanceId(String applicationInstanceId) {
        if(!properties.keySet().contains(APPLICATION_INSTANCE_ID)
                || properties.get(APPLICATION_INSTANCE_ID) == null
                || !properties.get(APPLICATION_INSTANCE_ID).equals(applicationInstanceId)) {
            properties.set(APPLICATION_INSTANCE_ID, applicationInstanceId);
            for(ConnectionListener listener : listeners)
                listener.newApplicationInstance(applicationInstanceId);
        }
    }

    /**
     * Updates the server connection status of the router we use to connect to the server
     * @param serverConnectionStatus the router's new server connection status
     */
    public void setServerConnectionStatus(ServerConnectionStatus serverConnectionStatus) {
        if(this.serverConnectionStatus != serverConnectionStatus) {
            this.serverConnectionStatus = serverConnectionStatus;
            if(serverConnectionStatus != ServerConnectionStatus.ConnectedToServer && serverConnectionStatus != ServerConnectionStatus.DisconnectedTemporarily)
                setApplicationInstanceStatus(ApplicationInstance.Status.Unregistered);
            for (ConnectionListener listener : listeners)
                listener.serverConnectionStatusChanged(serverConnectionStatus);
        }
    }

    public void setApplicationStatus(Application.Status applicationStatus) {
        if(this.applicationStatus != applicationStatus) {
            this.applicationStatus = applicationStatus;
            for (ConnectionListener listener : listeners)
                listener.applicationStatusChanged(applicationStatus);
        }
    }

    public void setApplicationInstanceStatus(ApplicationInstance.Status applicationInstanceStatus) {
        if(this.applicationInstanceStatus != applicationInstanceStatus) {
            this.applicationInstanceStatus = applicationInstanceStatus;
            for (ConnectionListener listener : listeners)
                listener.applicationInstanceStatusChanged(applicationInstanceStatus);
        }
    }
}
