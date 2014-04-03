package com.intuso.housemate.object.server.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.ApplicationListener;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerRealApplication
        extends ServerRealObject<ApplicationData, HousemateData<?>, ServerRealObject<?, ? ,?, ?>, ApplicationListener<? super ServerRealApplication>>
        implements Application<
            ServerRealValue<ApplicationStatus>,
            ServerRealCommand,
            ServerRealApplicationInstance,
            ServerRealList<ApplicationInstanceData, ServerRealApplicationInstance>,
            ServerRealApplication> {

    private final ServerRealList<ApplicationInstanceData, ServerRealApplicationInstance> applicationInstances;
    private final ServerRealCommand allowCommand;
    private final ServerRealCommand someCommand;
    private final ServerRealCommand rejectCommand;
    private final ServerRealValue<ApplicationStatus> statusValue;

    /**
     * @param log {@inheritDoc}
     * @param details the application details
     */
    public ServerRealApplication(Log log, ListenersFactory listenersFactory, ApplicationDetails details, RealType<?, ?,
            ApplicationStatus> applicationStatusType, ApplicationStatus initialStatus) {
        this(log, listenersFactory, details.getApplicationId(), details.getApplicationName(), details.getApplicationDescription(),
                applicationStatusType, initialStatus);
    }

    public ServerRealApplication(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                                 RealType<?, ?, ApplicationStatus> applicationStatusType, ApplicationStatus initialStatus) {
        super(log, listenersFactory, new ApplicationData(id, name, description));
        this.applicationInstances = new ServerRealList<ApplicationInstanceData, ServerRealApplicationInstance>(
                log, listenersFactory, APPLICATION_INSTANCES_ID, APPLICATION_INSTANCES_ID, "The application's instances");
        allowCommand = new ServerRealCommand(log, listenersFactory, ALLOW_COMMAND_ID, ALLOW_COMMAND_ID, "Allow access to all the application instances",
                Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                setStatus(ApplicationStatus.AllowInstances);
            }
        };
        someCommand = new ServerRealCommand(log, listenersFactory, SOME_COMMAND_ID, SOME_COMMAND_ID, "Allow access to some of the application instances",
                Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                setStatus(ApplicationStatus.SomeInstances);
            }
        };
        rejectCommand = new ServerRealCommand(log, listenersFactory, REJECT_COMMAND_ID, REJECT_COMMAND_ID, "Reject access to all the application instances",
                Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                setStatus(ApplicationStatus.RejectInstances);
            }
        };
        statusValue = new ServerRealValue<ApplicationStatus>(log, listenersFactory, STATUS_VALUE_ID, STATUS_VALUE_ID,
                "The status of the application instances", applicationStatusType, initialStatus);
        addChild(applicationInstances);
        addChild(allowCommand);
        addChild(someCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    public void setStatus(ApplicationStatus status) {
        statusValue.setTypedValue(status);
        boolean enabled = (status == ApplicationStatus.SomeInstances || status == ApplicationStatus.AllowInstances);
        for(ServerRealApplicationInstance instance : applicationInstances) {
            instance.setApplicationStatus(status);
            instance.getAllowCommand().getEnabledValue().setTypedValue(enabled);
            instance.getRejectCommand().getEnabledValue().setTypedValue(enabled);
        }
    }

    @Override
    public ServerRealList<ApplicationInstanceData, ServerRealApplicationInstance> getApplicationInstances() {
        return applicationInstances;
    }

    @Override
    public ServerRealCommand getAllowCommand() {
        return allowCommand;
    }

    @Override
    public ServerRealCommand getSomeCommand() {
        return someCommand;
    }

    @Override
    public ServerRealCommand getRejectCommand() {
        return rejectCommand;
    }

    @Override
    public ApplicationStatus getStatus() {
        return statusValue.getTypedValue();
    }

    @Override
    public ServerRealValue<ApplicationStatus> getStatusValue() {
        return statusValue;
    }
}
