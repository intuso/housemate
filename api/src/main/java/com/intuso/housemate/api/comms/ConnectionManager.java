package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.object.root.Root;
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
    private final ClientType clientType;
    private final Sender sender;

    private String serverInstanceId = null;

    private ServerConnectionStatus serverConnectionStatus = ServerConnectionStatus.DisconnectedPermanently;
    private ApplicationStatus applicationStatus = ApplicationStatus.Unregistered;
    private ApplicationInstanceStatus applicationInstanceStatus = ApplicationInstanceStatus.Unregistered;

    /**
     * @param listenersFactory
     * @param clientType the type of the client's connection
     * @param sender
     */
    public ConnectionManager(ListenersFactory listenersFactory, PropertyRepository properties, ClientType clientType, Sender sender) {
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
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    /**
     * Gets the current application instance status
     * @return the current connections application instance status
     */
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return applicationInstanceStatus;
    }

    /**
     * Requests access to the server
     */
    public void register(ApplicationDetails applicationDetails) {
        switch (serverConnectionStatus) {
            case DisconnectedPermanently:
            case Connecting:
            case ConnectedToRouter:
                throw new HousemateRuntimeException("Cannot request access until a server connection has been established");
            case ConnectedToServer:
            case DisconnectedTemporarily:
                if(applicationInstanceStatus != ApplicationInstanceStatus.Unregistered)
                    throw new HousemateRuntimeException("Registration already in progress or done");
                else if(applicationDetails != null) {
                    setApplicationInstanceStatus(ApplicationInstanceStatus.Pending);
                    sender.sendMessage(new Message<ApplicationRegistration>(ROOT_PATH, Root.APPLICATION_REGISTRATION_TYPE,
                            new ApplicationRegistration(applicationDetails, properties.get(APPLICATION_INSTANCE_ID), clientType)));
                } else
                    throw new HousemateRuntimeException("Null application or instance details");
        }
    }

    /**
     * Logs out of the server
     */
    public void unregister() {
        sender.sendMessage(new Message<NoPayload>(ROOT_PATH, Root.APPLICATION_UNREGISTRATION_TYPE, NoPayload.INSTANCE));
        properties.remove(APPLICATION_INSTANCE_ID);
        setApplicationStatus(ApplicationStatus.Unregistered);
        setApplicationInstanceStatus(ApplicationInstanceStatus.Unregistered);
    }

    public void setServerInstanceId(String serverInstanceId) {
        if(this.serverInstanceId != null && !this.serverInstanceId.equals(serverInstanceId)) {
            for(ConnectionListener listener : listeners)
                listener.newServerInstance(serverInstanceId);
            setApplicationStatus(ApplicationStatus.Unregistered);
            setApplicationInstanceStatus(ApplicationInstanceStatus.Unregistered);
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
            for (ConnectionListener listener : listeners)
                listener.serverConnectionStatusChanged(serverConnectionStatus);
        }
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        if(this.applicationStatus != applicationStatus) {
            this.applicationStatus = applicationStatus;
            for (ConnectionListener listener : listeners)
                listener.applicationStatusChanged(applicationStatus);
        }
    }

    public void setApplicationInstanceStatus(ApplicationInstanceStatus applicationInstanceStatus) {
        if(this.applicationInstanceStatus != applicationInstanceStatus) {
            this.applicationInstanceStatus = applicationInstanceStatus;
            for (ConnectionListener listener : listeners)
                listener.applicationInstanceStatusChanged(applicationInstanceStatus);
        }
    }
}
