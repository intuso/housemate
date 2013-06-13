package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.Reconnect;
import com.intuso.housemate.api.comms.message.AuthenticationRequest;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.comms.message.ReconnectResponse;
import com.intuso.housemate.api.object.connection.ClientWrappable;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 12/06/13
 * Time: 19:57
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionManager {

    private final Listeners<ConnectionStatusChangeListener> listeners = new Listeners<ConnectionStatusChangeListener>();

    private final Sender sender;
    private final String[] path;
    private final String connectMessageType;
    private final String disconnectMessageType;
    private final ClientWrappable.Type clientType;

    private String brokerInstanceId = null;
    private String connectionId = null;
    private ConnectionStatus status = null;

    public ConnectionManager(Sender sender, ClientWrappable.Type clientType, ConnectionStatus initialStatus) {
        this.sender = sender;
        this.path = new String[] {""};
        this.connectMessageType = Root.CONNECTION_REQUEST;
        this.disconnectMessageType = Root.DISCONNECT;
        this.clientType = clientType;
        this.status = initialStatus;
    }

    public ListenerRegistration addStatusChangeListener(ConnectionStatusChangeListener listener) {
        return listeners.addListener(listener);
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public String getConnectionId() {
        return connectionId;
    }

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

    public void logout() {
        sender.sendMessage(new Message<NoPayload>(path, disconnectMessageType, NoPayload.VALUE));
        status = ConnectionStatus.Unauthenticated;
        for(ConnectionStatusChangeListener listener : listeners.getListeners())
            listener.connectionStatusChanged(status);
    }

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
                            new AuthenticationRequest(ClientWrappable.Type.Proxy, new Reconnect(connectionId))));
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
