package com.intuso.housemate.plugin.api.internal;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class ExternalClientRouter extends Router {

    private final Router router;
    private Router.Registration routerRegistration;

    /**
     * @param log {@inheritDoc}
     */
    public ExternalClientRouter(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router) {
        super(log, listenersFactory, properties);
        this.router = router;
        setServerConnectionStatus(ServerConnectionStatus.ConnectedToServer);
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
    public final void sendMessage(Message<?> message) {
        routerRegistration.sendMessage(message);
    }

    public void start() {
        this.routerRegistration = router.registerReceiver(this);
        _start();
    }

    public void stop() {
        routerRegistration.unregister();
        _stop();
    }

    protected abstract void _start();
    protected abstract void _stop();
}
