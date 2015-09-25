package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.payload.ApplicationData;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealApplication
        extends RealObject<ApplicationData, HousemateData<?>, RealObject<?, ? ,?, ?>, Application.Listener<? super RealApplication>>
        implements Application<
            RealValue<Application.Status>,
            RealCommand,
            RealApplicationInstance,
            RealList<ApplicationInstanceData, RealApplicationInstance>,
            RealApplication> {

    private final RealList<ApplicationInstanceData, RealApplicationInstance> applicationInstances;
    private final RealCommand allowCommand;
    private final RealCommand someCommand;
    private final RealCommand rejectCommand;
    private final RealValue<Application.Status> statusValue;

    /**
     * @param log {@inheritDoc}
     * @param details the application details
     */
    public RealApplication(Log log, ListenersFactory listenersFactory, ApplicationDetails details, RealType<?, ?,
            Application.Status> applicationStatusType) {
        this(log, listenersFactory, details.getApplicationId(), details.getApplicationName(), details.getApplicationDescription(),
                applicationStatusType);
    }

    public RealApplication(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                           RealType<?, ?, Application.Status> applicationStatusType) {
        super(log, listenersFactory, new ApplicationData(id, name, description));
        this.applicationInstances = new RealList<>(
                log, listenersFactory, ApplicationData.APPLICATION_INSTANCES_ID, ApplicationData.APPLICATION_INSTANCES_ID, "The application's instances");
        allowCommand = new RealCommand(log, listenersFactory, ApplicationData.ALLOW_COMMAND_ID, ApplicationData.ALLOW_COMMAND_ID, "Allow access to all the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                setStatus(Application.Status.AllowInstances);
            }
        };
        someCommand = new RealCommand(log, listenersFactory, ApplicationData.SOME_COMMAND_ID, ApplicationData.SOME_COMMAND_ID, "Allow access to some of the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                setStatus(Application.Status.SomeInstances);
            }
        };
        rejectCommand = new RealCommand(log, listenersFactory, ApplicationData.REJECT_COMMAND_ID, ApplicationData.REJECT_COMMAND_ID, "Reject access to all the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                setStatus(Application.Status.RejectInstances);
            }
        };
        statusValue = new RealValue<>(log, listenersFactory, ApplicationData.STATUS_VALUE_ID, ApplicationData.STATUS_VALUE_ID,
                "The status of the application instances", applicationStatusType, (Application.Status)null);
        addChild(applicationInstances);
        addChild(allowCommand);
        addChild(someCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    public void setStatus(Application.Status status) {
        statusValue.setTypedValues(status);
        boolean enabled = (status == Application.Status.SomeInstances || status == Application.Status.AllowInstances);
        for(RealApplicationInstance instance : applicationInstances) {
            instance.getAllowCommand().getEnabledValue().setTypedValues(enabled);
            instance.getRejectCommand().getEnabledValue().setTypedValues(enabled);
        }
    }

    @Override
    public RealList<ApplicationInstanceData, RealApplicationInstance> getApplicationInstances() {
        return applicationInstances;
    }

    @Override
    public RealCommand getAllowCommand() {
        return allowCommand;
    }

    @Override
    public RealCommand getSomeCommand() {
        return someCommand;
    }

    @Override
    public RealCommand getRejectCommand() {
        return rejectCommand;
    }

    @Override
    public RealValue<Application.Status> getStatusValue() {
        return statusValue;
    }
}
