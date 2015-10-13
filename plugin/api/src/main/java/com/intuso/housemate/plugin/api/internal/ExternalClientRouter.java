package com.intuso.housemate.plugin.api.internal;

import com.intuso.housemate.comms.api.internal.BaseRouter;
import com.intuso.housemate.comms.api.internal.ClientConnection;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

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
     * @param log {@inheritDoc}
     */
    public ExternalClientRouter(Log log, ListenersFactory listenersFactory, Router<?> router) {
        super(log, listenersFactory);
        this.router = router;
    }

    @Override
    public final void connect() {
        // do nothing
        connectionEstablished();
    }

    @Override
    public final void disconnect() {
        // do nothing
        connectionLost(false);
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
            public void serverConnectionStatusChanged(ClientConnection clientConnection, ServerConnectionStatus serverConnectionStatus) {
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
