package com.intuso.housemate.server.comms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ClientType;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.object.server.ClientInstance;
import com.intuso.housemate.object.server.RemoteClient;
import com.intuso.housemate.object.server.proxy.ServerProxyRoot;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.ServerProxyModule;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.general.ServerGeneralRoot;
import com.intuso.utilities.log.Log;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 */
public class RemoteClientManager {

    private final Log log;
    private final Injector injector;
    private final ServerGeneralRoot generalRoot;
    private final RootBridge bridgeRoot;
    
    private final RemoteClientImpl rootClient;
    private final Map<ClientInstance, RemoteClientImpl> clients = Maps.newHashMap();

    @Inject
    public RemoteClientManager(Log log, Injector injector, ServerGeneralRoot generalRoot, RootBridge bridgeRoot,
                               MainRouter mainRouter) {
        this.log = log;
        this.injector = injector;
        this.generalRoot = generalRoot;
        this.bridgeRoot = bridgeRoot;
        rootClient = new RemoteClientImpl(
                new ClientInstance(Server.INTERNAL_APPLICATION, UUID.randomUUID().toString(), ClientType.Router),
                generalRoot, mainRouter);
        rootClient.setBaseRoute(Lists.<String>newArrayList());
    }

    public RemoteClient getClient(ClientInstance clientInstance, List<String> route) {

        // see if the client was previously known
        RemoteClientImpl client = clients.get(clientInstance);
        if(client != null) {

            // if we think it's still connected somewhere else, then remove the current one
            if(client.getRoute() != null)
                client.remove();

            // set the new client route
            client.setBaseRoute(route);
            try {
                rootClient.addClient(client);
            } catch(HousemateException e) {
                throw new HousemateRuntimeException("Failed to add reconnected client", e);
            }

            return client;
        } else {
            // try and add the client. If this fails, it's because one of the intermediate clients isn't allowed access
            // in which case it shouldn't have allowed this message through. We shouldn't reply to prevent misuse
            try {
                Root<?> root;
                switch(clientInstance.getClientType()) {
                    case Real: // the server proxy objects are for remote real objects
                        root = injector.createChildInjector(new ServerProxyModule()).getInstance(ServerProxyRoot.class);
                        bridgeRoot.addProxyRoot((ServerProxyRoot) root);
                        break;
                    case Proxy: // the server bridge objects are for remote proxy objects
                        root = bridgeRoot;
                        break;
                    default:
                        root = generalRoot;
                        break;
                }
                client = addClient(root, clientInstance, route);
                clients.put(clientInstance, client);
                client.setBaseRoute(route);
                return client;
            } catch(HousemateException e) {
                log.e("Failed to add client endpoint for " + Message.routeToString(route), e);
                log.d("Maybe one of the intermediate clients isn't connected or isn't of type " + ClientType.Router);
                throw new HousemateRuntimeException("Failed to add client endpoint for " + Message.routeToString(route), e);
            }
        }
    }

    private RemoteClientImpl addClient(Root<?> root, ClientInstance clientInstance, List<String> route)
            throws HousemateException {
        return rootClient.addClient(route, root, clientInstance);
    }

    public void clientUnregistered(List<String> route) {
        RemoteClientImpl client = rootClient.getClient(route);
        if(client != null) {
            client.remove();
            unregisterClient(client);
        }
    }

    private void unregisterClient(RemoteClientImpl client) {
        clients.remove(client.getClientInstance());
        client.unregister();
        for(RemoteClientImpl child : client.getChildren())
            unregisterClient(child);
    }

    public void connectionLost(List<String> route) {
        RemoteClientImpl client = rootClient.getClient(route);
        if(client != null) {
            client.remove();
            connectionLost(client);
        }
    }

    private void connectionLost(RemoteClientImpl client) {
        client.connectionLost();
        // create iterator over other list to prevent ConcurrentModificationExceptions
        for(RemoteClientImpl child : client.getChildren())
            connectionLost(child);
    }

    public RemoteClientImpl getClient(List<String> route) {
        return rootClient.getClient(route);
    }
}
