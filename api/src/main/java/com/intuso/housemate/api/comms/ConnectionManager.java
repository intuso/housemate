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
    private final static String[] ROOT_PATH = new String[] {""};

    private final Listeners<ConnectionListener> listeners;

    private final PropertyRepository properties;
    private final ClientType clientType;

    private String serverInstanceId = null;

    private ServerConnectionStatus serverConnectionStatus = ServerConnectionStatus.Disconnected;
    private ApplicationStatus applicationStatus = ApplicationStatus.Unregistered;
    private ApplicationInstanceStatus applicationInstanceStatus = ApplicationInstanceStatus.Unregistered;

    /**
     * @param listenersFactory
     * @param clientType the type of the client's connection
     */
    public ConnectionManager(ListenersFactory listenersFactory, PropertyRepository properties, ClientType clientType) {
        this.listeners = listenersFactory.create();
        this.properties = properties;
        this.clientType = clientType;
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
    public void register(ApplicationDetails applicationDetails, Sender sender) {
        if(serverConnectionStatus != ServerConnectionStatus.ConnectedToServer)
            throw new HousemateRuntimeException("Cannot request access until a server connection has been established");
        else if(applicationInstanceStatus != ApplicationInstanceStatus.Unregistered)
            throw new HousemateRuntimeException("Registration already in progress or done");
        if(applicationDetails != null) {
            updateStatus(serverConnectionStatus, applicationStatus, ApplicationInstanceStatus.Pending);
            sender.sendMessage(new Message<ApplicationRegistration>(ROOT_PATH, Root.APPLICATION_REGISTRATION_TYPE,
                    new ApplicationRegistration(applicationDetails, properties.get(APPLICATION_INSTANCE_ID), clientType)));
        } else {
            throw new HousemateRuntimeException("Null application or instance details");
        }
    }

    /**
     * Logs out of the server
     */
    public void unregister(Sender sender) {
        sender.sendMessage(new Message<NoPayload>(ROOT_PATH, Root.APPLICATION_UNREGISTRATION_TYPE, NoPayload.INSTANCE));
        properties.remove(APPLICATION_INSTANCE_ID);
        updateStatus(serverConnectionStatus, ApplicationStatus.Unregistered, ApplicationInstanceStatus.Unregistered);
    }

    public void setServerInstanceId(String serverInstanceId) {
        if(this.serverInstanceId != null && !this.serverInstanceId.equals(serverInstanceId)) {
            for(ConnectionListener listener : listeners)
                listener.newServerInstance(serverInstanceId);
            updateStatus(serverConnectionStatus, ApplicationStatus.Unregistered, ApplicationInstanceStatus.Unregistered);
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
    public void setConnectionStatus(ServerConnectionStatus serverConnectionStatus,
                                    ApplicationStatus applicationStatus,
                                    ApplicationInstanceStatus applicationInstanceStatus) {
        updateStatus(serverConnectionStatus, applicationStatus, applicationInstanceStatus);
    }

    private void updateStatus(ServerConnectionStatus serverConnectionStatus,
                              ApplicationStatus applicationStatus,
                              ApplicationInstanceStatus applicationInstanceStatus) {
        if(this.serverConnectionStatus != serverConnectionStatus
                || this.applicationStatus != applicationStatus
                || this.applicationInstanceStatus != applicationInstanceStatus) {
            this.serverConnectionStatus = serverConnectionStatus;
            this.applicationStatus = applicationStatus;
            this.applicationInstanceStatus = applicationInstanceStatus;
            for(ConnectionListener listener : listeners)
                listener.statusChanged(serverConnectionStatus, applicationStatus, applicationInstanceStatus);
        }
    }
}
