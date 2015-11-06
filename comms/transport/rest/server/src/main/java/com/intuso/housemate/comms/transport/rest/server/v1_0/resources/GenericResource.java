package com.intuso.housemate.comms.transport.rest.server.v1_0.resources;

import com.google.inject.Inject;
import com.intuso.housemate.comms.v1_0.api.*;
import com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus;
import com.intuso.housemate.comms.v1_0.api.payload.StringPayload;

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

    private final Router<?> router;

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
            Router.Registration registration = router.registerReceiver(cache);
            request.setAttribute("registration", registration);
            cache.setRegistration(registration);
        }
        return (Router.Registration) request.getAttribute("registration");
    }

    private MessageCache getMessageCache(HttpServletRequest request) {
        return (MessageCache)request.getAttribute("cache");
    }

    private class MessageCache implements Router.Receiver {

        private Router.Registration registration;
        private final LinkedBlockingQueue<Message<Message.Payload>> cache = new LinkedBlockingQueue<>();
        private final MessageSequencer messageSequencer = new MessageSequencer(new Message.Receiver<Message.Payload>() {
            @Override
            public void messageReceived(Message<Message.Payload> message) {
                try {
                    cache.put(message);
                } catch (InterruptedException e) {
                    throw new HousemateCommsException("Failed to cache message for client");
                }
            }
        });

        public void setRegistration(Router.Registration registration) {
            this.registration = registration;
        }

        @Override
        public synchronized void messageReceived(Message message) {
            messageSequencer.messageReceived(message);
            if(message.getSequenceId() != null)
                registration.sendMessage(new Message<Message.Payload>(new String[] {""}, Message.RECEIVED_TYPE, new Message.ReceivedPayload(message.getSequenceId())));
        }

        @Override
        public void serverConnectionStatusChanged(ClientConnection clientConnection, ServerConnectionStatus serverConnectionStatus) {
            messageReceived(new Message(new String[] {""}, ClientConnection.SERVER_CONNECTION_STATUS_TYPE, serverConnectionStatus));
        }

        @Override
        public void newServerInstance(ClientConnection clientConnection, String serverId) {
            messageReceived(new Message(new String[] {""}, ClientConnection.SERVER_INSTANCE_ID_TYPE, new StringPayload(serverId)));
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
