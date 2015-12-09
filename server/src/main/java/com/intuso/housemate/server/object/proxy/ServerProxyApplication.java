package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.ApplicationData;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
import org.slf4j.Logger;

public class ServerProxyApplication
        extends ServerProxyObject<
        ApplicationData,
        HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyApplication,
            Application.Listener<? super ServerProxyApplication>>
        implements Application<
            ServerProxyValue,
            ServerProxyCommand,
            ServerProxyApplicationInstance,
            ServerProxyList<ApplicationInstanceData, ServerProxyApplicationInstance>,
            ServerProxyApplication> {

    private ServerProxyCommand allow;
    private ServerProxyCommand some;
    private ServerProxyCommand reject;
    private ServerProxyValue status;
    private ServerProxyList<ApplicationInstanceData, ServerProxyApplicationInstance> applicationInstances;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyApplication(Logger logger, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted ApplicationData data) {
        super(logger, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        allow = (ServerProxyCommand) getChild(ApplicationData.ALLOW_COMMAND_ID);
        some = (ServerProxyCommand) getChild(ApplicationData.SOME_COMMAND_ID);
        reject = (ServerProxyCommand) getChild(ApplicationData.REJECT_COMMAND_ID);
        status = (ServerProxyValue) getChild(ApplicationData.STATUS_VALUE_ID);
        applicationInstances = (ServerProxyList<ApplicationInstanceData, ServerProxyApplicationInstance>) getChild(ApplicationData.APPLICATION_INSTANCES_ID);
    }

    @Override
    public ServerProxyCommand getAllowCommand() {
        return allow;
    }

    @Override
    public ServerProxyCommand getSomeCommand() {
        return some;
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
    public ServerProxyList<ApplicationInstanceData, ServerProxyApplicationInstance> getApplicationInstances() {
        return applicationInstances;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof ApplicationData)
            getApplicationInstances().copyValues(data.getChildData(ApplicationData.APPLICATION_INSTANCES_ID));
    }
}
