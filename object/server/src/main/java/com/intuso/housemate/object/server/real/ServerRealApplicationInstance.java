package com.intuso.housemate.object.server.real;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.object.server.RemoteClient;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Set;

public class ServerRealApplicationInstance
        extends ServerRealObject<
            ApplicationInstanceData,
            HousemateData<?>,
            ServerRealObject<?, ? ,?, ?>,
            ApplicationInstanceListener<? super ServerRealApplicationInstance>>
        implements ApplicationInstance<ServerRealValue<ApplicationInstanceStatus>, ServerRealCommand, ServerRealApplicationInstance> {

    private final ServerRealCommand allowCommand;
    private final ServerRealCommand rejectCommand;
    private final ServerRealValue<ApplicationInstanceStatus> statusValue;

    private ApplicationStatus applicationStatus;
    private final Set<RemoteClient> clients = Sets.newHashSet();

    public ServerRealApplicationInstance(Log log, ListenersFactory listenersFactory, String instanceId,
                                         ApplicationInstanceStatusType applicationInstanceStatusType,
                                         ApplicationStatus initialApplicationStatus) {
        super(log, listenersFactory, new ApplicationInstanceData(instanceId, instanceId, instanceId));
        this.applicationStatus = initialApplicationStatus;
        allowCommand = new ServerRealCommand(log, listenersFactory, ALLOW_COMMAND_ID, ALLOW_COMMAND_ID, "Allow access to the application instance",
                Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                setStatus(ApplicationInstanceStatus.Allowed);
            }
        };
        rejectCommand = new ServerRealCommand(log, listenersFactory, REJECT_COMMAND_ID, REJECT_COMMAND_ID, "Reject access to the application instance",
                Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                setStatus(ApplicationInstanceStatus.Rejected);
            }
        };
        statusValue = new ServerRealValue<ApplicationInstanceStatus>(log, listenersFactory, STATUS_VALUE_ID, STATUS_VALUE_ID,
                "The status of the application instance", applicationInstanceStatusType, null);
        addChild(allowCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    public void addClient(RemoteClient client) {
        clients.add(client);
        client.setStatus(applicationStatus, statusValue.getTypedValue());
    }

    public void setStatus(ApplicationInstanceStatus status) {
        statusValue.setTypedValue(status);
        for(RemoteClient client : clients) {
            try {
                client.setStatus(applicationStatus, status);
                client.sendMessage(new String[]{""}, Root.CONNECTION_STATUS_TYPE,
                        new Root.ConnectionStatus(ServerConnectionStatus.ConnectedToServer, applicationStatus, status));
            } catch (HousemateException e) {
                getLog().e("Failed to send status to client as it is not connected");
            }
        }
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
        for(RemoteClient client : clients) {
            try {
                client.sendMessage(new String[]{""}, Root.CONNECTION_STATUS_TYPE,
                        new Root.ConnectionStatus(ServerConnectionStatus.ConnectedToServer, applicationStatus, getStatus()));
            } catch (HousemateException e) {
                getLog().e("Failed to send application status to client as it is not connected");
            }
        }
    }

    @Override
    public ServerRealCommand getAllowCommand() {
        return allowCommand;
    }

    @Override
    public ServerRealCommand getRejectCommand() {
        return rejectCommand;
    }

    @Override
    public ApplicationInstanceStatus getStatus() {
        return statusValue.getTypedValue();
    }

    @Override
    public ServerRealValue<ApplicationInstanceStatus> getStatusValue() {
        return statusValue;
    }
}
