package com.intuso.housemate.server.comms;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.real.api.internal.RealApplication;
import com.intuso.housemate.client.real.api.internal.RealApplicationInstance;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.client.real.impl.internal.RealApplicationImpl;
import com.intuso.housemate.client.real.impl.internal.RealApplicationInstanceImpl;
import com.intuso.housemate.client.real.impl.internal.ServerRealRoot;
import com.intuso.housemate.client.real.impl.internal.type.ApplicationInstanceStatusType;
import com.intuso.housemate.client.real.impl.internal.type.ApplicationStatusType;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.payload.ApplicationData;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.RootData;
import com.intuso.housemate.comms.api.internal.payload.StringPayload;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.housemate.server.Server;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
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

    private final static Logger logger = LoggerFactory.getLogger(AccessManager.class);

    private final ListenersFactory listenersFactory;
    private final Injector injector;
    private final ServerRealRoot realRoot;
    private Map<RemoteClient, StatusListener> statusListeners = Maps.newHashMap();

    @Inject
    public AccessManager(ListenersFactory listenersFactory, Injector injector, ServerRealRoot realRoot) {
        this.listenersFactory = listenersFactory;
        this.injector = injector;
        this.realRoot = realRoot;
    }

    public ClientInstance getClientApplicationInstance(List<String> route, ApplicationRegistration registration) {

        // get the application
        if(isInternalRegistration(route, registration)) {
            String instanceId = registration.getApplicationInstanceId();
            if(instanceId == null)
                instanceId = UUID.randomUUID().toString();
            return new ClientInstance.Application(true, Server.INTERNAL_APPLICATION_DETAILS, instanceId, registration.getType());
        } else {
            String appId = registration.getApplicationDetails().getApplicationId();
            RealApplication application = realRoot.getApplications().get(appId);
            if (application == null) {
                application = new RealApplicationImpl(logger, listenersFactory, registration.getApplicationDetails(),
                        injector.getInstance(ApplicationStatusType.class));
                ((RealList<RealApplication>)realRoot.getApplications()).add(application);
                application.setStatus(getInitialStatus(appId));
            }

            // get the application instance supplied by the client
            String instanceId = registration.getApplicationInstanceId();
            // if there was no instance id or no app instance for that id, create a new id (don't want client choosing it)
            // and a new instance for the id
            if (instanceId == null || instanceId.length() == 0 || application.getApplicationInstances().get(instanceId) == null) {
                instanceId = UUID.randomUUID().toString();
                RealApplicationInstance applicationInstance =
                        new RealApplicationInstanceImpl(logger, listenersFactory, instanceId,
                                injector.getInstance(ApplicationInstanceStatusType.class));
                ((RealList<RealApplicationInstance>)application.getApplicationInstances()).add(applicationInstance);
                applicationInstance.getStatusValue().setTypedValues(getInitialStatus(application));
            }

            return new ClientInstance.Application(false, registration.getApplicationDetails(), instanceId, registration.getType());
        }
    }

    public ClientInstance getClientRouterInstance(List<String> route, String routerId) {

        if (routerId == null || routerId.length() == 0)
            routerId = UUID.randomUUID().toString();
        return new ClientInstance.Router(isInternalRegistration(route), routerId);
    }

    private boolean isInternalRegistration(List<String> route, ApplicationRegistration registration) {
        return registration.getApplicationDetails().getApplicationId().equals(Server.INTERNAL_APPLICATION_DETAILS.getApplicationId())
                && (route.size() == 0 || route.size() == 1);
    }

    private boolean isInternalRegistration(List<String> route) {
        return route.size() == 0 || route.size() == 1;
    }

    public void initialiseClient(RemoteClient client) {

        if(client.getClientInstance() instanceof ClientInstance.Application) {

            ClientInstance.Application clientApplicationInstance = (ClientInstance.Application) client.getClientInstance();

            if (client.getClientInstance().isInternal()) {

                try {
                    client.sendMessage(new String[]{""}, RootData.APPLICATION_INSTANCE_ID_TYPE, new StringPayload(clientApplicationInstance.getApplicationInstanceId()));
                    client.sendMessage(new String[]{""}, RootData.APPLICATION_STATUS_TYPE, new ApplicationData.StatusPayload(Application.Status.AllowInstances));
                    client.sendMessage(new String[]{""}, RootData.APPLICATION_INSTANCE_STATUS_TYPE, new ApplicationInstanceData.StatusPayload(ApplicationInstance.Status.Allowed));
                } catch (Throwable t) {
                    logger.error("Failed to tell application instance about statuses", t);
                }

                // ensure the client belongs to the application instance
                client.setApplicationAndInstanceStatus(Application.Status.AllowInstances, ApplicationInstance.Status.Allowed);

            } else {

                RealApplication application = realRoot.getApplications().get(clientApplicationInstance.getApplicationDetails().getApplicationId());
                RealApplicationInstance applicationInstance = application.getApplicationInstances().get(clientApplicationInstance.getApplicationInstanceId());

                // tell the client what access etc it has
                try {
                    client.sendMessage(new String[]{""}, RootData.APPLICATION_INSTANCE_ID_TYPE, new StringPayload(clientApplicationInstance.getApplicationInstanceId()));
                    client.sendMessage(new String[]{""}, RootData.APPLICATION_STATUS_TYPE, new ApplicationData.StatusPayload(application.getStatusValue().getTypedValue()));
                    client.sendMessage(new String[]{""}, RootData.APPLICATION_INSTANCE_STATUS_TYPE, new ApplicationInstanceData.StatusPayload(applicationInstance.getStatusValue().getTypedValue()));
                } catch (Throwable t) {
                    logger.error("Failed to tell application instance about statuses", t);
                }

                // ensure the client belongs to the application instance
                client.setApplicationAndInstanceStatus(application.getStatusValue().getTypedValue(), applicationInstance.getStatusValue().getTypedValue());
                statusListeners.put(client, new StatusListener(client, application, applicationInstance));
            }
        } else if(client.getClientInstance() instanceof ClientInstance.Router) {
            ClientInstance.Router clientRouterInstance = (ClientInstance.Router) client.getClientInstance();
            client.sendMessage(new String[]{""}, Router.ROUTER_ID, new StringPayload(clientRouterInstance.getRouterId()));
        }
    }

    private final static Set<String> allowedAllApps = Sets.newHashSet(
            "com.intuso.housemate.web.server",
            "com.intuso.housemate.web.client");
    private Application.Status getInitialStatus(String appId) {
        return allowedAllApps.contains(appId) ? Application.Status.AllowInstances : Application.Status.SomeInstances;
    }

    private ApplicationInstance.Status getInitialStatus(RealApplication application) {
        switch (application.getStatusValue().getTypedValue()) {
            case AllowInstances:
                return ApplicationInstance.Status.Allowed;
            case SomeInstances:
                return ApplicationInstance.Status.Pending;
            case RejectInstances:
                return ApplicationInstance.Status.Rejected;
            case Expired:
                return ApplicationInstance.Status.Expired;
            default:
                return ApplicationInstance.Status.Rejected;
        }
    }

    private class StatusListener implements Value.Listener<RealValue<?>> {

        private final RemoteClient client;
        private final RealValue<Application.Status> applicationStatus;
        private final RealValue<ApplicationInstance.Status> applicationInstanceStatus;


        private StatusListener(RemoteClient client, RealApplication application, RealApplicationInstance applicationInstance) {
            this.client = client;
            this.applicationStatus = application.getStatusValue();
            this.applicationInstanceStatus = applicationInstance.getStatusValue();
            applicationStatus.addObjectListener(this);
            applicationInstanceStatus.addObjectListener(this);
        }

        @Override
        public void valueChanging(RealValue<?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(RealValue<?> value) {
            client.setApplicationAndInstanceStatus(applicationStatus.getTypedValue(), applicationInstanceStatus.getTypedValue());
        }
    }
}
