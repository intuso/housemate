package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.payload.ApplicationData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealApplicationImpl
        extends RealObject<ApplicationData, HousemateData<?>, RealObject<?, ? ,?, ?>, Application.Listener<? super RealApplication>>
        implements RealApplication {

    private final RealList<RealApplicationInstance> applicationInstances;
    private final RealCommandImpl allowCommand;
    private final RealCommandImpl someCommand;
    private final RealCommandImpl rejectCommand;
    private final RealValueImpl<Status> statusValue;

    /**
     * @param log {@inheritDoc}
     * @param details the application details
     */
    public RealApplicationImpl(Log log, ListenersFactory listenersFactory, ApplicationDetails details, RealType<Status> applicationStatusType) {
        this(log, listenersFactory, details.getApplicationId(), details.getApplicationName(), details.getApplicationDescription(),
                applicationStatusType);
    }

    public RealApplicationImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                               RealType<Status> applicationStatusType) {
        super(log, listenersFactory, new ApplicationData(id, name, description));
        this.applicationInstances = (RealList)new RealListImpl<>(
                log, listenersFactory, ApplicationData.APPLICATION_INSTANCES_ID, ApplicationData.APPLICATION_INSTANCES_ID, "The application's instances");
        allowCommand = new RealCommandImpl(log, listenersFactory, ApplicationData.ALLOW_COMMAND_ID, ApplicationData.ALLOW_COMMAND_ID, "Allow access to all the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                setStatus(Status.AllowInstances);
            }
        };
        someCommand = new RealCommandImpl(log, listenersFactory, ApplicationData.SOME_COMMAND_ID, ApplicationData.SOME_COMMAND_ID, "Allow access to some of the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                setStatus(Status.SomeInstances);
            }
        };
        rejectCommand = new RealCommandImpl(log, listenersFactory, ApplicationData.REJECT_COMMAND_ID, ApplicationData.REJECT_COMMAND_ID, "Reject access to all the application instances",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                setStatus(Status.RejectInstances);
            }
        };
        statusValue = new RealValueImpl<>(log, listenersFactory, ApplicationData.STATUS_VALUE_ID, ApplicationData.STATUS_VALUE_ID,
                "The status of the application instances", applicationStatusType, (Status)null);
        addChild((RealListImpl)applicationInstances);
        addChild(allowCommand);
        addChild(someCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    public void setStatus(Status status) {
        statusValue.setTypedValues(status);
        boolean enabled = (status == Status.SomeInstances || status == Status.AllowInstances);
        for(RealApplicationInstance instance : applicationInstances) {
            instance.getAllowCommand().getEnabledValue().setTypedValues(enabled);
            instance.getRejectCommand().getEnabledValue().setTypedValues(enabled);
        }
    }

    @Override
    public RealList<RealApplicationInstance> getApplicationInstances() {
        return applicationInstances;
    }

    @Override
    public void addApplicationInstance(RealApplicationInstance applicationInstance) {
        applicationInstances.add((RealApplicationInstanceImpl) applicationInstance);
    }

    @Override
    public void removeApplicationInstance(RealApplicationInstance applicationInstance) {
        applicationInstances.remove(applicationInstance.getId());
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
    public RealValue<Status> getStatusValue() {
        return statusValue;
    }
}
