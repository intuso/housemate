package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
import org.slf4j.Logger;

public class ServerProxyApplicationInstance
        extends ServerProxyObject<
        ApplicationInstanceData,
        HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyApplicationInstance,
            ApplicationInstance.Listener<? super ServerProxyApplicationInstance>>
        implements ApplicationInstance<ServerProxyValue, ServerProxyCommand, ServerProxyApplicationInstance> {

    private ServerProxyCommand allow;
    private ServerProxyCommand reject;
    private ServerProxyValue status;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyApplicationInstance(Logger logger, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted ApplicationInstanceData data) {
        super(logger, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        allow = (ServerProxyCommand) getChild(ApplicationInstanceData.ALLOW_COMMAND_ID);
        reject = (ServerProxyCommand) getChild(ApplicationInstanceData.REJECT_COMMAND_ID);
        status = (ServerProxyValue) getChild(ApplicationInstanceData.STATUS_VALUE_ID);
    }

    @Override
    public ServerProxyCommand getAllowCommand() {
        return allow;
    }

    @Override
    public ServerProxyCommand getRejectCommand() {
        return reject;
    }

    @Override
    public ServerProxyValue getStatusValue() {
        return status;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }
}
