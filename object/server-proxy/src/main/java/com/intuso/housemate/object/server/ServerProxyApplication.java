package com.intuso.housemate.object.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.ApplicationListener;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.object.real.impl.type.ApplicationStatusType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.EnumChoiceType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public class ServerProxyApplication
        extends ServerProxyObject<
            ApplicationData,
            HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyApplication,
            ApplicationListener<? super ServerProxyApplication>>
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
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyApplication(Log log, ListenersFactory listenersFactory, Injector injector, BooleanType booleanType, @Assisted ApplicationData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        allow = (ServerProxyCommand) getChild(ALLOW_COMMAND_ID);
        some = (ServerProxyCommand) getChild(SOME_COMMAND_ID);
        reject = (ServerProxyCommand) getChild(REJECT_COMMAND_ID);
        status = (ServerProxyValue) getChild(STATUS_VALUE_ID);
        applicationInstances = (ServerProxyList<ApplicationInstanceData, ServerProxyApplicationInstance>) getChild(APPLICATION_INSTANCES_ID);
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
    public ApplicationStatus getStatus() {
        List<ApplicationStatus> statuses = ApplicationStatusType.deserialiseAll(new EnumChoiceType.EnumInstanceSerialiser<>(ApplicationStatus.class), status.getTypeInstances());
        return statuses != null && statuses.size() > 0 ? statuses.get(0) : null;
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
            getApplicationInstances().copyValues(data.getChildData(APPLICATION_INSTANCES_ID));
    }
}
