package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

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
        super(log, listenersFactory, WriteableMapPropertyRepository.newEmptyRepository(listenersFactory, properties));
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

    public void start() throws HousemateException {
        this.routerRegistration = router.registerReceiver(this);
        _start();
    }

    public void stop() {
        routerRegistration.unregister();
        _stop();
    }

    protected abstract void _start() throws HousemateException;
    protected abstract void _stop();
}
