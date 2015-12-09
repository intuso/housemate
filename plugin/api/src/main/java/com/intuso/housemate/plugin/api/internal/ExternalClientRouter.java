package com.intuso.housemate.plugin.api.internal;

import com.intuso.housemate.comms.api.internal.BaseRouter;
import com.intuso.housemate.comms.api.internal.ClientConnection;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.access.ConnectionStatus;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class ExternalClientRouter<ROUTER extends ExternalClientRouter<?>> extends BaseRouter<ROUTER> {

    private final Router<?> router;
    private Router.Registration routerRegistration;

    /**
     * @param logger {@inheritDoc}
     */
    public ExternalClientRouter(Logger logger, ListenersFactory listenersFactory, Router<?> router) {
        super(logger, listenersFactory);
        this.router = router;
    }

    @Override
    public final void connect() {
        // do nothing
    }

    @Override
    public final void disconnect() {
        // do nothing
    }

    @Override
    protected void sendMessageNow(Message<?> message) {
        routerRegistration.sendMessage(message);
    }

    @Override
    public final void sendMessage(Message<?> message) {
        sendMessageNow(message);
    }

    public void start() {
        this.routerRegistration = router.registerReceiver(new Receiver() {
            @Override
            public void serverConnectionStatusChanged(ClientConnection clientConnection, ConnectionStatus connectionStatus) {
                // do nothing
            }

            @Override
            public void newServerInstance(ClientConnection clientConnection, String serverId) {
                // do nothing
            }

            @Override
            public void messageReceived(Message message) {
                ExternalClientRouter.this.messageReceived(message);
            }
        });
        _start();
        connectionEstablished();
    }

    public void stop() {
        routerRegistration.unregister();
        _stop();
        connectionLost(false);
    }

    protected abstract void _start();
    protected abstract void _stop();
}
