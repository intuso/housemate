package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.Reconnect;
import com.intuso.housemate.api.comms.message.AuthenticationRequest;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.comms.message.ReconnectResponse;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;

/**
 *
 * Utility class for managing a connection to a broker. Handles broker messages, reconnecting etc and calls the
 * appropriate callbacks as state changes
 */
public class ConnectionManager {

    private final Listeners<ConnectionStatusChangeListener> listeners = new Listeners<ConnectionStatusChangeListener>();

    private final Sender sender;
    private final String[] path;
    private final String connectMessageType;
    private final String disconnectMessageType;
    private final ConnectionType clientType;

    private String brokerInstanceId = null;
    private String connectionId = null;
    private ConnectionStatus status = null;

    /**
     * @param sender the message sender the connection manager can use to communicate with the broker
     * @param clientType the type of the client's connection
     * @param initialStatus the initial connection status
     */
    public ConnectionManager(Sender sender, ConnectionType clientType, ConnectionStatus initialStatus) {
        this.sender = sender;
        this.path = new String[] {""};
        this.connectMessageType = Root.CONNECTION_REQUEST_TYPE;
        this.disconnectMessageType = Root.DISCONNECT_TYPE;
        this.clientType = clientType;
        this.status = initialStatus;
    }

    /**
     * Adds listener to be notified of connection status changes
     * @param listener the listener to add
     * @return listener registration
     */
    public ListenerRegistration addStatusChangeListener(ConnectionStatusChangeListener listener) {
        return listeners.addListener(listener);
    }

    /**
     * Gets the current connection status
     * @return the current connections status
     */
    public ConnectionStatus getStatus() {
        return status;
    }

    /**
     * Gets the connection id
     * @return the connection id
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * Authenticates with the broker
     * @param method the method to authenticate with
     */
    public void login(AuthenticationMethod method) {
        if(status == ConnectionStatus.Disconnected || status == ConnectionStatus.Connecting)
            throw new HousemateRuntimeException("Cannot attempt authentication until connection is complete");
        else if(status == ConnectionStatus.Authenticated)
            throw new HousemateRuntimeException("Authentication already succeeded");
        else if(status == ConnectionStatus.Authenticating)
            throw new HousemateRuntimeException("Authentication already in progress");
        status = ConnectionStatus.Authenticating;
        for(ConnectionStatusChangeListener listener : listeners.getListeners())
            listener.connectionStatusChanged(status);
        sender.sendMessage(new Message<AuthenticationRequest>(path, connectMessageType, new AuthenticationRequest(clientType, method)));
    }

    /**
     * Logs out of the broker
     */
    public void logout() {
        sender.sendMessage(new Message<NoPayload>(path, disconnectMessageType, NoPayload.VALUE));
        status = ConnectionStatus.Unauthenticated;
        for(ConnectionStatusChangeListener listener : listeners.getListeners())
            listener.connectionStatusChanged(status);
    }

    /**
     * Processes a response to an authentication request
     * @param response the response received
     */
    public void authenticationResponseReceived(AuthenticationResponse response) {
        if(response instanceof ReconnectResponse)
            status = ConnectionStatus.Authenticated;
        else if(brokerInstanceId != null && !brokerInstanceId.equals(response.getBrokerInstanceId())) {
            connectionId = null;
            status = ConnectionStatus.Unauthenticated;
            for(ConnectionStatusChangeListener listener : listeners.getListeners())
                listener.brokerInstanceChanged();
        } else {
            brokerInstanceId = response.getBrokerInstanceId();
            connectionId = response.getConnectionId();
            status = connectionId != null ? ConnectionStatus.Authenticated : ConnectionStatus.AuthenticationFailed;
        }
        for(ConnectionStatusChangeListener listener : listeners.getListeners())
            listener.connectionStatusChanged(status);
    }

    /**
     * Updates the status of the router we use to connect to the broker
     * @param routerStatus the router's new status
     */
    public void routerStatusChanged(ConnectionStatus routerStatus) {
        switch(routerStatus) {
            case Disconnected:
            case Connecting:
            case Unauthenticated:
            case Authenticating:
            case AuthenticationFailed:
                status = ConnectionStatus.Unauthenticated;
                break;
            case Authenticated:
                if(connectionId != null) {
                    sender.sendMessage(new Message<AuthenticationRequest>(path, connectMessageType,
                            new AuthenticationRequest(ConnectionType.Proxy, new Reconnect(connectionId))));
                    status = ConnectionStatus.Authenticating;
                    return;
                } else
                    status = ConnectionStatus.Unauthenticated;
                break;
        }
        for(ConnectionStatusChangeListener listener : listeners.getListeners())
            listener.connectionStatusChanged(status);
    }
}
