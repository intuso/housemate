package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.ApplicationListener;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <COMMAND> the type of the command
 * @param <APPLICATION> the type of the user
 */
public abstract class ProxyApplication<
            VALUE extends ProxyValue<?, VALUE>,
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            APPLICATION_INSTANCE extends ProxyApplicationInstance<?, ?, APPLICATION_INSTANCE>,
            APPLICATION_INSTANCES extends ProxyList<ApplicationInstanceData, APPLICATION_INSTANCE, APPLICATION_INSTANCES>,
            APPLICATION extends ProxyApplication<VALUE, COMMAND, APPLICATION_INSTANCE, APPLICATION_INSTANCES, APPLICATION>>
        extends ProxyObject<ApplicationData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, APPLICATION, ApplicationListener<? super APPLICATION>>
        implements Application<VALUE, COMMAND, APPLICATION_INSTANCE, APPLICATION_INSTANCES, APPLICATION> {

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     */
    public ProxyApplication(Log log, ListenersFactory listenersFactory, ApplicationData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public APPLICATION_INSTANCES getApplicationInstances() {
        return (APPLICATION_INSTANCES) getChild(APPLICATION_INSTANCES_ID);
    }

    @Override
    public COMMAND getAllowCommand() {
        return (COMMAND) getChild(ALLOW_COMMAND_ID);
    }

    @Override
    public COMMAND getSomeCommand() {
        return (COMMAND) getChild(SOME_COMMAND_ID);
    }

    @Override
    public COMMAND getRejectCommand() {
        return (COMMAND) getChild(REJECT_COMMAND_ID);
    }

    @Override
    public ApplicationStatus getStatus() {
        String value = getStatusValue().getTypeInstances().getFirstValue();
        if(value == null)
            return null;
        return ApplicationStatus.valueOf(value);
    }

    @Override
    public VALUE getStatusValue() {
        return (VALUE) getChild(STATUS_VALUE_ID);
    }
}
