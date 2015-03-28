package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceListener;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.impl.type.ApplicationInstanceStatusType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public class RealApplicationInstance
        extends RealObject<
            ApplicationInstanceData,
            HousemateData<?>,
            RealObject<?, ? ,?, ?>,
            ApplicationInstanceListener<? super RealApplicationInstance>>
        implements ApplicationInstance<RealValue<ApplicationInstanceStatus>, RealCommand, RealApplicationInstance> {

    private final RealCommand allowCommand;
    private final RealCommand rejectCommand;
    private final RealValue<ApplicationInstanceStatus> statusValue;

    public RealApplicationInstance(Log log, ListenersFactory listenersFactory, String instanceId,
                                   ApplicationInstanceStatusType applicationInstanceStatusType,
                                   ApplicationStatus applicationStatus) {
        super(log, listenersFactory, new ApplicationInstanceData(instanceId, instanceId, instanceId));
        allowCommand = new RealCommand(log, listenersFactory, ALLOW_COMMAND_ID, ALLOW_COMMAND_ID, "Allow access to the application instance",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                statusValue.setTypedValues(ApplicationInstanceStatus.Allowed);
            }
        };
        rejectCommand = new RealCommand(log, listenersFactory, REJECT_COMMAND_ID, REJECT_COMMAND_ID, "Reject access to the application instance",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                statusValue.setTypedValues(ApplicationInstanceStatus.Rejected);
            }
        };
        statusValue = new RealValue<>(log, listenersFactory, STATUS_VALUE_ID, STATUS_VALUE_ID,
                "The status of the application instance", applicationInstanceStatusType, (List)null);
        addChild(allowCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    @Override
    public RealCommand getAllowCommand() {
        return allowCommand;
    }

    @Override
    public RealCommand getRejectCommand() {
        return rejectCommand;
    }

    @Override
    public ApplicationInstanceStatus getStatus() {
        return statusValue.getTypedValue();
    }

    @Override
    public RealValue<ApplicationInstanceStatus> getStatusValue() {
        return statusValue;
    }
}
