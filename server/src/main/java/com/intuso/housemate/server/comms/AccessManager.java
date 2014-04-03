package com.intuso.housemate.server.comms;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.object.real.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.object.real.impl.type.ApplicationStatusType;
import com.intuso.housemate.object.server.ClientInstance;
import com.intuso.housemate.object.server.RemoteClient;
import com.intuso.housemate.object.server.real.ServerRealApplication;
import com.intuso.housemate.object.server.real.ServerRealApplicationInstance;
import com.intuso.housemate.object.server.real.ServerRealRoot;
import com.intuso.housemate.server.Server;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Set;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 11/02/14
 * Time: 08:50
 * To change this template use File | Settings | File Templates.
 */
public class AccessManager {

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final Injector injector;
    private final ServerRealRoot realRoot;

    @Inject
    public AccessManager(Log log, ListenersFactory listenersFactory, Injector injector, ServerRealRoot realRoot) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.injector = injector;
        this.realRoot = realRoot;
    }

    public ClientInstance getClientInstance(ApplicationRegistration request) {

        // get the application
        String appId = request.getApplicationDetails().getApplicationId();
        ServerRealApplication application = realRoot.getApplications().get(appId);
        if(application == null) {
            realRoot.getApplications().add(new ServerRealApplication(log, listenersFactory, request.getApplicationDetails(),
                    injector.getInstance(ApplicationStatusType.class), getInitialStatus(appId)));
            application = realRoot.getApplications().get(appId);
        }

        // get the application instance supplied by the client
        String instanceId = request.getApplicationInstanceId();
        // if there was no instance id or no app instance for that id, create a new id (don't want client choosing it)
        // and a new instance for the id
        if(instanceId == null || application.getApplicationInstances().get(instanceId) == null) {
            instanceId = UUID.randomUUID().toString();
            application.getApplicationInstances().add(
                    new ServerRealApplicationInstance(log, listenersFactory, instanceId,
                            injector.getInstance(ApplicationInstanceStatusType.class),
                            application.getStatus(), getInitialStatus(application)));
        }

        return new ClientInstance(request.getApplicationDetails(), instanceId, request.getType());
    }

    public void sendAccessStatusToClient(RemoteClient client) {

        ServerRealApplication application = realRoot.getApplications().get(client.getClientInstance().getApplicationDetails().getApplicationId());
        ServerRealApplicationInstance applicationInstance = application.getApplicationInstances().get(client.getClientInstance().getApplicationInstanceId());

                // ensure the client belongs to the application instance
        applicationInstance.addClient(client);

        // tell the client what access etc it has
        try {
            client.sendMessage(new String[]{""}, Root.SERVER_INSTANCE_ID_TYPE,
                    new StringPayload(Server.INSTANCE_ID));
            client.sendMessage(new String[] {""}, Root.APPLICATION_INSTANCE_ID_TYPE,
                    new StringPayload(client.getClientInstance().getApplicationInstanceId()));
            client.sendMessage(new String[]{""}, Root.CONNECTION_STATUS_TYPE,
                    new Root.ConnectionStatus(ServerConnectionStatus.ConnectedToServer, application.getStatus(), applicationInstance.getStatus()));
        } catch(HousemateException e) {
            log.e("Failed to tell application instance about statuses", e);
        }
    }

    private final static Set<String> allowedAllApps = Sets.newHashSet(
            Server.INTERNAL_APPLICATION.getApplicationId(),
            "com.intuso.housemate.web.server",
            "com.intuso.housemate.web.client");
    private ApplicationStatus getInitialStatus(String appId) {
        return allowedAllApps.contains(appId) ? ApplicationStatus.AllowInstances : ApplicationStatus.SomeInstances;
    }

    private ApplicationInstanceStatus getInitialStatus(ServerRealApplication application) {
        switch (application.getStatus()) {
            case AllowInstances:
                return ApplicationInstanceStatus.Allowed;
            case SomeInstances:
                return ApplicationInstanceStatus.Pending;
            case RejectInstances:
                return ApplicationInstanceStatus.Rejected;
            case Expired:
                return ApplicationInstanceStatus.Expired;
            default:
                return ApplicationInstanceStatus.Rejected;
        }
    }
}
