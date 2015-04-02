package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceListener;
import com.intuso.housemate.object.real.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.object.real.impl.type.EnumChoiceType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public class ServerProxyApplicationInstance
        extends ServerProxyObject<
            ApplicationInstanceData,
            HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyApplicationInstance,
            ApplicationInstanceListener<? super ServerProxyApplicationInstance>>
        implements ApplicationInstance<
                    ServerProxyValue,
                    ServerProxyCommand,
                    ServerProxyApplicationInstance> {

    private ServerProxyCommand allow;
    private ServerProxyCommand reject;
    private ServerProxyValue status;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyApplicationInstance(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted ApplicationInstanceData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        allow = (ServerProxyCommand) getChild(ALLOW_COMMAND_ID);
        reject = (ServerProxyCommand) getChild(REJECT_COMMAND_ID);
        status = (ServerProxyValue) getChild(STATUS_VALUE_ID);
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
    public ApplicationInstanceStatus getStatus() {
        List<ApplicationInstanceStatus> statuses = ApplicationInstanceStatusType.deserialiseAll(new EnumChoiceType.EnumInstanceSerialiser<>(ApplicationInstanceStatus.class), status.getTypeInstances());
        return statuses != null && statuses.size() > 0 ? statuses.get(0) : null;
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
