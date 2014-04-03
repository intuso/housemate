package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceListener;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <VALUE> the type of the value
 * @param <COMMAND> the type of the command
 * @param <APPLICATION_INSTANCE> the type of the application instance
 */
public abstract class ProxyApplicationInstance<
            VALUE extends ProxyValue<?, ?>,
            COMMAND extends ProxyCommand<?, ?, ?, ?>,
            APPLICATION_INSTANCE extends ProxyApplicationInstance<VALUE, COMMAND, APPLICATION_INSTANCE>>
        extends ProxyObject<ApplicationInstanceData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, APPLICATION_INSTANCE, ApplicationInstanceListener<? super APPLICATION_INSTANCE>>
        implements ApplicationInstance<VALUE, COMMAND, APPLICATION_INSTANCE> {

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     */
    public ProxyApplicationInstance(Log log, ListenersFactory listenersFactory, ApplicationInstanceData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public COMMAND getAllowCommand() {
        return (COMMAND) getChild(ALLOW_COMMAND_ID);
    }

    @Override
    public COMMAND getRejectCommand() {
        return (COMMAND) getChild(REJECT_COMMAND_ID);
    }

    @Override
    public ApplicationInstanceStatus getStatus() {
        String value = getStatusValue().getTypeInstances().getFirstValue();
        if(value == null)
            return null;
        return ApplicationInstanceStatus.valueOf(value);
    }

    @Override
    public VALUE getStatusValue() {
        return (VALUE) getChild(STATUS_VALUE_ID);
    }
}
