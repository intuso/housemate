package com.intuso.housemate.server.comms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.general.ServerGeneralRoot;
import com.intuso.housemate.server.object.proxy.ServerProxyRoot;
import com.intuso.housemate.server.object.proxy.ioc.ServerProxyModule;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public class RemoteClientManager {

    private final Logger logger;
    private final Injector injector;
    private final ServerGeneralRoot generalRoot;
    private final RootBridge bridgeRoot;
    
    private final RemoteClient rootClient;
    private final Map<ClientInstance, RemoteClient> clients = Maps.newHashMap();

    @Inject
    public RemoteClientManager(Logger logger, ListenersFactory listenersFactory, Injector injector, ServerGeneralRoot generalRoot, RootBridge bridgeRoot, MainRouter mainRouter) {
        this.logger = logger;
        this.injector = injector;
        this.generalRoot = generalRoot;
        this.bridgeRoot = bridgeRoot;
        rootClient = new RemoteClient(logger, listenersFactory, new ClientInstance.Router(true, ""), generalRoot, mainRouter);
        rootClient.setBaseRoute(Lists.<String>newArrayList());
    }

    public RemoteClient getClient(ClientInstance clientInstance, List<String> route) {

        // see if the client was previously known
        RemoteClient client = clients.get(clientInstance);
        if(client != null) {

            // if we think it's still connected somewhere else, then remove the current one
            if(client.getRoute() != null)
                client.remove();

            // set the new client route
            client.setBaseRoute(route);
            try {
                rootClient.addClient(client);
            } catch(Throwable e) {
                throw new HousemateCommsException("Failed to add reconnected client", e);
            }

            return client;
        } else if(clientInstance instanceof ClientInstance.Application) {
            ClientInstance.Application applicationInstance = (ClientInstance.Application) clientInstance;
            // try and add the client. If this fails, it's because one of the intermediate clients isn't allowed access
            // in which case it shouldn't have allowed this message through. We shouldn't reply to prevent misuse
            try {
                Message.Receiver<Message.Payload> receiver;
                switch(applicationInstance.getClientType()) {
                    case Real: // the server proxy objects are for remote real objects
                        receiver = injector.createChildInjector(new ServerProxyModule()).getInstance(ServerProxyRoot.class);
                        break;
                    case Proxy: // the server bridge objects are for remote proxy objects
                        receiver = bridgeRoot;
                        break;
                    default:
                        receiver = generalRoot;
                        break;
                }
                client = addClient(receiver, clientInstance, route);
                if(applicationInstance.getClientType() == ApplicationRegistration.ClientType.Real)
                    ((ServerProxyRoot) receiver).setClient(client);
                clients.put(clientInstance, client);
                client.setBaseRoute(route);
                return client;
            } catch(Throwable t) {
                logger.error("Failed to add client endpoint for " + Arrays.toString(route.toArray()), t);
                logger.debug("Maybe one of the intermediate clients isn't connected or isn't a router");
                throw new HousemateCommsException("Failed to add client endpoint for " + Arrays.toString(route.toArray()), t);
            }
        } else if(clientInstance instanceof ClientInstance.Router) {
            ClientInstance.Router routerInstance = (ClientInstance.Router) clientInstance;
            // try and add the client. If this fails, it's because one of the intermediate clients isn't allowed access
            // in which case it shouldn't have allowed this message through. We shouldn't reply to prevent misuse
            try {
                Message.Receiver<Message.Payload> receiver = generalRoot;
                client = addClient(receiver, clientInstance, route);
                clients.put(clientInstance, client);
                client.setBaseRoute(route);
                return client;
            } catch(Throwable t) {
                logger.error("Failed to add client endpoint for " + Arrays.toString(route.toArray()), t);
                logger.debug("Maybe one of the intermediate clients isn't connected or isn't a router");
                throw new HousemateCommsException("Failed to add client endpoint for " + Arrays.toString(route.toArray()), t);
            }
        } else
            throw new HousemateCommsException("Unknown client instance type " + clientInstance.getClass().getName());
    }

    private RemoteClient addClient(Message.Receiver<Message.Payload> receiver, ClientInstance clientInstance, List<String> route) {
        return rootClient.addClient(route, receiver, clientInstance);
    }

    public void clientUnregistered(List<String> route) {
        RemoteClient client = rootClient.getClient(route);
        if(client != null) {
            client.remove();
            unregisterClient(client);
        }
    }

    private void unregisterClient(RemoteClient client) {
        clients.remove(client.getClientInstance());
        client.unregister();
        for(RemoteClient child : client.getChildren())
            unregisterClient(child);
    }

    public void connectionLost(List<String> route) {
        RemoteClient client = rootClient.getClient(route);
        if(client != null) {
            client.remove();
            connectionLost(client);
        }
    }

    private void connectionLost(RemoteClient client) {
        client.connectionLost();
        // create iterator over other list to prevent ConcurrentModificationExceptions
        for(RemoteClient child : client.getChildren())
            connectionLost(child);
    }

    public RemoteClient getClient(List<String> route) {
        return rootClient.getClient(route);
    }
}
