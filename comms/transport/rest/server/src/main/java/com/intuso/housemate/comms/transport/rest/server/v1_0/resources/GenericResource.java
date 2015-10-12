package com.intuso.housemate.comms.transport.rest.server.v1_0.resources;

import com.google.inject.Inject;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/10/13
 * Time: 09:23
 * To change this template use File | Settings | File Templates.
 */
@Path("/generic")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenericResource {

    private final Router router;

    @Inject
    public GenericResource(Router<?> router) {
        this.router = router;
    }

    @Path("/sendMessage")
    @POST
    public void sendMessage(@Context HttpServletRequest request,
                            @QueryParam("path") String path,
                            @QueryParam("type") String type,
                            Message.Payload payload) {
        getRouterRegistration(request).sendMessage(new Message(path.split("/"), type, payload));
    }

    @Path("/getMessage")
    @GET
    public Message<Message.Payload> getMessage(@Context HttpServletRequest request) {
        MessageCache cache = getMessageCache(request);
        return cache != null ? cache.getMessage() : null;
    }

    private Router.Registration getRouterRegistration(HttpServletRequest request) {
        if(request.getAttribute("registration") == null) {
            MessageCache cache = new MessageCache();
            request.setAttribute("cache", cache);
            request.setAttribute("registration", router.registerReceiver(cache));
        }
        return (Router.Registration) request.getAttribute("registration");
    }

    private MessageCache getMessageCache(HttpServletRequest request) {
        return (MessageCache)request.getAttribute("cache");
    }

    private class MessageCache implements Message.Receiver<Message.Payload> {

        private LinkedBlockingQueue<Message<Message.Payload>> cache = new LinkedBlockingQueue<>();

        @Override
        public synchronized void messageReceived(Message<Message.Payload> message) {
            try {
                cache.put(message);
            } catch (InterruptedException e) {
                throw new HousemateCommsException("Failed to cache message for client");
            }
        }

        private Message<Message.Payload> getMessage() {
            try {
                return cache.poll(50, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                return null;
            }
        }
    }
}
