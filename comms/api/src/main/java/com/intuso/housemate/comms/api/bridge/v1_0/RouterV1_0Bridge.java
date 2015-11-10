package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.inject.Inject;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 25/09/15.
 */
public class RouterV1_0Bridge implements Router<RouterV1_0Bridge> {

    private final MessageMapper messageMapper;
    private final ConnectionStatusMapper connectionStatusMapper;
    private final ReceiverBridgeReverse.Factory receiverBridgeReverseFactory;
    private final com.intuso.housemate.comms.api.internal.Router<?> router;
    private final RouterListenerBridgeReverse.Factory routerListenerBridgeReverseFactory;

    @Inject
    public RouterV1_0Bridge(MessageMapper messageMapper, ConnectionStatusMapper connectionStatusMapper, ReceiverBridgeReverse.Factory receiverBridgeReverseFactory, com.intuso.housemate.comms.api.internal.Router<?> router, RouterListenerBridgeReverse.Factory routerListenerBridgeReverseFactory) {
        this.messageMapper = messageMapper;
        this.connectionStatusMapper = connectionStatusMapper;
        this.receiverBridgeReverseFactory = receiverBridgeReverseFactory;
        this.router = router;
        this.routerListenerBridgeReverseFactory = routerListenerBridgeReverseFactory;
    }

    @Override
    public ConnectionStatus getServerConnectionStatus() {
        return connectionStatusMapper.map(router.getServerConnectionStatus());
    }

    @Override
    public ListenerRegistration addListener(Router.Listener<? super RouterV1_0Bridge> listener) {
        return router.addListener(routerListenerBridgeReverseFactory.create(this, listener));
    }

    @Override
    public Registration registerReceiver(Receiver<RouterV1_0Bridge> receiver) {
        return new RegistrationBridge(router.registerReceiver(receiverBridgeReverseFactory.create(this, receiver)));
    }

    @Override
    public void messageReceived(Message message) {
        router.messageReceived(messageMapper.map(message));
    }

    @Override
    public void connect() {
        router.connect();
    }

    @Override
    public void disconnect() {
        router.disconnect();
    }

    @Override
    public void sendMessage(Message<?> message) {
        router.sendMessage(messageMapper.map(message));
    }

    private final class RegistrationBridge implements Registration {

        private final com.intuso.housemate.comms.api.internal.Router.Registration registration;

        private RegistrationBridge(com.intuso.housemate.comms.api.internal.Router.Registration registration) {
            this.registration = registration;
        }

        @Override
        public void unregister() {
            registration.unregister();
        }

        @Override
        public void sendMessage(Message<?> message) {
            registration.sendMessage(messageMapper.map(message));
        }
    }
}
