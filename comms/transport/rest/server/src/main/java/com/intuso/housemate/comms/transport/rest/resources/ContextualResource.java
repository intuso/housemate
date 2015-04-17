package com.intuso.housemate.comms.transport.rest.resources;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.server.ServerData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.simple.SimpleProxyCommand;
import com.intuso.housemate.object.proxy.simple.SimpleProxyDevice;
import com.intuso.housemate.object.proxy.simple.SimpleProxyRoot;
import com.intuso.housemate.object.proxy.simple.SimpleProxyServer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/10/13
 * Time: 09:23
 * To change this template use File | Settings | File Templates.
 */
@Path("/contextual")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContextualResource implements RootListener<SimpleProxyRoot>, CommandPerformListener<SimpleProxyCommand> {

    // TODO hack until figure out why session doesn't work
    private static SimpleProxyRoot ROOT;

    private final Injector injector;

    public ContextualResource(Injector injector) {
        this.injector = injector;
    }

    // Hack to allow logging in via a browser
    @Path("/requestAccess")
    @GET
    public void getRequestAccess(@Context HttpServletRequest request,
                                 @QueryParam("appId") String appId,
                                 @QueryParam("component") String component,
                                 @QueryParam("instanceId") String instanceId) {
        postRequestAccess(request, appId, component, instanceId);
    }

    @Path("/requestAccess")
    @POST
    public void postRequestAccess(@Context HttpServletRequest request,
                                  @QueryParam("appId") String appId,
                                  @QueryParam("component") String component,
                                  @QueryParam("instanceId") String instanceId) {
        if(request.getAttribute("root") != null) {
            SimpleProxyRoot root = (SimpleProxyRoot)request.getAttribute("root");
            root.unregister();
        }
        SimpleProxyRoot root = injector.getInstance(SimpleProxyRoot.class);
        root.addObjectListener(this);
        root.register(new ApplicationDetails(appId, appId, appId), component);
        request.setAttribute("root", root);
        ROOT = root;
    }

    @Path("clients")
    @GET
    public java.util.List<ServerData> getClients(@Context HttpServletRequest request) {
        java.util.List<ServerData> result = Lists.newArrayList();
        for(SimpleProxyServer client : getRoot(request).getServers())
            result.add((ServerData)client.getData().clone());
        return result;
    }

    @Path("clients/{clientId}")
    @GET
    public ServerData getClient(@Context HttpServletRequest request,
                                @PathParam("clientId") String clientId) {
        SimpleProxyServer client = getRoot(request).getServers().get(clientId);
        return client != null ? client.getData() : null;
    }

    @Path("clients/{clientId}/devices")
    @GET
    public java.util.List<DeviceData> getDevices(@Context HttpServletRequest request,
                                                 @PathParam("clientId") String clientId) {
        java.util.List<DeviceData> result = Lists.newArrayList();
        for(SimpleProxyDevice device : getRoot(request).getServers().get(clientId).getDevices())
            result.add((DeviceData)device.getData().clone());
        return result;
    }

    @Path("clients/{clientId}/devices/{deviceId}")
    @GET
    public DeviceData getDevice(@Context HttpServletRequest request,
                                @PathParam("clientId") String clientId,
                                @PathParam("deviceId") String deviceId) {
        SimpleProxyDevice device = getRoot(request).getServers().get(clientId).getDevices().get(deviceId);
        return device != null ? device.getData() : null;
    }

    @Path("clients/{clientId}/devices/{deviceId}/commands/{commandId}")
    @POST
    public void performDeviceCommand(@Context HttpServletRequest request,
                                     @PathParam("clientId") String clientId,
                                     @PathParam("deviceId") String deviceId,
                                     @PathParam("commandId") String commandId,
                                     TypeInstanceMap typeInstanceMap) {
        SimpleProxyDevice device = getRoot(request).getServers().get(clientId).getDevices().get(deviceId);
        if(device != null) {
            SimpleProxyCommand command = device.getCommands().get(commandId);
            if(command != null) {
                command.perform(typeInstanceMap, this);
            }
        }
    }

    private SimpleProxyRoot getRoot(HttpServletRequest request) {
        return ROOT;
//        return (SimpleProxyRoot)request.getAttribute("root");
    }

    @Override
    public void serverConnectionStatusChanged(SimpleProxyRoot root, ServerConnectionStatus serverConnectionStatus) {
        // todo let the client know?
    }

    @Override
    public void applicationStatusChanged(SimpleProxyRoot root, ApplicationStatus applicationStatus) {
        // todo let the client know?
    }

    @Override
    public void applicationInstanceStatusChanged(SimpleProxyRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
        if(applicationInstanceStatus == ApplicationInstanceStatus.Allowed) {
            ROOT.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(List<String> errors) {
                    // TODO tell the client somehow
                }

                @Override
                public void succeeded() {
                    // TODO, anything?
                }
            }, new HousemateObject.TreeLoadInfo("devices",
                    new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
    }

    @Override
    public void newApplicationInstance(SimpleProxyRoot root, String instanceId) {
        // TODO let the client know somehow
    }

    @Override
    public void newServerInstance(SimpleProxyRoot root, String serverId) {
        // TODO let the client know somehow
    }

    @Override
    public void commandStarted(SimpleProxyCommand command) {
        // TODO let the client know somewhow
    }

    @Override
    public void commandFinished(SimpleProxyCommand command) {
        // TODO let the client know somewhow
    }

    @Override
    public void commandFailed(SimpleProxyCommand command, String error) {
        // TODO let the client know somewhow
    }
}
