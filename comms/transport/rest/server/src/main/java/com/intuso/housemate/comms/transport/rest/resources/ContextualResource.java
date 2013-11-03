package com.intuso.housemate.comms.transport.rest.resources;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.proxy.simple.SimpleProxyResources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/10/13
 * Time: 09:23
 * To change this template use File | Settings | File Templates.
 */
@Path("/contextual")
public class ContextualResource
        implements RootListener<SimpleProxyObject.Root>,
            CommandListener<SimpleProxyObject.Command> {

    // TODO hack until figure out why session doesn't work
    private static SimpleProxyObject.Root ROOT;

    private final SimpleProxyResources<SimpleProxyFactory.All> resources;

    public ContextualResource(SimpleProxyResources<SimpleProxyFactory.All> resources) {
        this.resources = resources;
    }

    // Hack to allow logging in via a browser
    @Path("/login")
    @GET
    public void getLogin(@Context HttpServletRequest request,
                      @QueryParam("username") String username,
                      @QueryParam("password") String password) {
        login(request, username, password);
    }

    @Path("/login")
    @POST
    public void login(@Context HttpServletRequest request,
                      @QueryParam("username") String username,
                      @QueryParam("password") String password) {
        if(request.getAttribute("root") != null) {
            SimpleProxyObject.Root root = (SimpleProxyObject.Root)request.getAttribute("root");
            root.logout();
        }
        SimpleProxyObject.Root root = new SimpleProxyObject.Root(resources, resources);
        root.addObjectListener(this);
        root.login(new UsernamePassword(username, password, false));
        request.setAttribute("root", root); ROOT = root;
    }

    @Path("/devices")
    @GET
    public java.util.List<DeviceData> getDevices(@Context HttpServletRequest request) {
        java.util.List<DeviceData> result = Lists.newArrayList();
        for(SimpleProxyObject.Device device : getRoot(request).getDevices())
            result.add((DeviceData)device.getData().clone());
        return result;
    }

    @Path("/devices/{deviceId}")
    @GET
    public DeviceData getDevice(@Context HttpServletRequest request,
                                @PathParam("deviceId") String deviceId) {
        SimpleProxyObject.Device device = getRoot(request).getDevices().get(deviceId);
        return device != null ? device.getData() : null;
    }

    @Path("/devices/{deviceId}/commands/{commandId}")
    @POST
    public void performDeviceCommand(@Context HttpServletRequest request,
                                     @PathParam("deviceId") String deviceId,
                                     @PathParam("commandId") String commandId,
                                     TypeInstanceMap typeInstanceMap) {
        SimpleProxyObject.Device device = getRoot(request).getDevices().get(deviceId);
        if(device != null) {
            SimpleProxyObject.Command command = device.getCommands().get(commandId);
            if(command != null) {
                command.perform(typeInstanceMap, this);
            }
        }
    }

    private SimpleProxyObject.Root getRoot(HttpServletRequest request) {
        return ROOT;
//        return (SimpleProxyObject.Root)request.getAttribute("root");
    }

    @Override
    public void connectionStatusChanged(SimpleProxyObject.Root root, ConnectionStatus status) {
        if(status == ConnectionStatus.Authenticated) {
            root.load(new LoadManager("REST Client", new HousemateObject.TreeLoadInfo("devices",
                    new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))) {
                @Override
                protected void failed(HousemateObject.TreeLoadInfo path) {
                    // TODO tell the client somehow
                }

                @Override
                protected void allLoaded() {
                    // TODO, anything?
                }
            });
        }
    }

    @Override
    public void brokerInstanceChanged(SimpleProxyObject.Root root) {
        // TODO let the client know somehow
    }

    @Override
    public void commandStarted(SimpleProxyObject.Command command) {
        // TODO let the client know somewhow
    }

    @Override
    public void commandFinished(SimpleProxyObject.Command command) {
        // TODO let the client know somewhow
    }

    @Override
    public void commandFailed(SimpleProxyObject.Command command, String error) {
        // TODO let the client know somewhow
    }
}
