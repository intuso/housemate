package com.intuso.housemate.object.real;

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
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealApplication
        extends RealObject<ApplicationData, HousemateData<?>, RealObject<?, ? ,?, ?>, ApplicationListener<? super RealApplication>>
        implements Application<
            RealValue<ApplicationStatus>,
            RealCommand,
            RealApplicationInstance,
            RealList<ApplicationInstanceData, RealApplicationInstance>,
            RealApplication> {

    private final RealList<ApplicationInstanceData, RealApplicationInstance> applicationInstances;
    private final RealCommand allowCommand;
    private final RealCommand someCommand;
    private final RealCommand rejectCommand;
    private final RealValue<ApplicationStatus> statusValue;

    /**
     * @param log {@inheritDoc}
     * @param details the application details
     */
    public RealApplication(Log log, ListenersFactory listenersFactory, ApplicationDetails details, RealType<?, ?,
            ApplicationStatus> applicationStatusType) {
        this(log, listenersFactory, details.getApplicationId(), details.getApplicationName(), details.getApplicationDescription(),
                applicationStatusType);
    }

    public RealApplication(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                           RealType<?, ?, ApplicationStatus> applicationStatusType) {
        super(log, listenersFactory, new ApplicationData(id, name, description));
        this.applicationInstances = new RealList<>(
                log, listenersFactory, APPLICATION_INSTANCES_ID, APPLICATION_INSTANCES_ID, "The application's instances");
        allowCommand = new RealCommand(log, listenersFactory, ALLOW_COMMAND_ID, ALLOW_COMMAND_ID, "Allow access to all the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                setStatus(ApplicationStatus.AllowInstances);
            }
        };
        someCommand = new RealCommand(log, listenersFactory, SOME_COMMAND_ID, SOME_COMMAND_ID, "Allow access to some of the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                setStatus(ApplicationStatus.SomeInstances);
            }
        };
        rejectCommand = new RealCommand(log, listenersFactory, REJECT_COMMAND_ID, REJECT_COMMAND_ID, "Reject access to all the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                setStatus(ApplicationStatus.RejectInstances);
            }
        };
        statusValue = new RealValue<>(log, listenersFactory, STATUS_VALUE_ID, STATUS_VALUE_ID,
                "The status of the application instances", applicationStatusType, (ApplicationStatus)null);
        addChild(applicationInstances);
        addChild(allowCommand);
        addChild(someCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    public void setStatus(ApplicationStatus status) {
        statusValue.setTypedValues(status);
        boolean enabled = (status == ApplicationStatus.SomeInstances || status == ApplicationStatus.AllowInstances);
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
    public ApplicationStatus getStatus() {
        return statusValue.getTypedValue();
    }

    @Override
    public RealValue<ApplicationStatus> getStatusValue() {
        return statusValue;
    }
}
