package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.real.api.internal.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public class RealApplicationInstance
        extends RealObject<
        ApplicationInstanceData,
        HousemateData<?>,
            RealObject<?, ? ,?, ?>,
            ApplicationInstance.Listener<? super RealApplicationInstance>>
        implements ApplicationInstance<RealValue<ApplicationInstance.Status>, RealCommand, RealApplicationInstance> {

    private final RealCommand allowCommand;
    private final RealCommand rejectCommand;
    private final RealValue<ApplicationInstance.Status> statusValue;

    public RealApplicationInstance(Log log, ListenersFactory listenersFactory, String instanceId,
                                   ApplicationInstanceStatusType applicationInstanceStatusType) {
        super(log, listenersFactory, new ApplicationInstanceData(instanceId, instanceId, instanceId));
        allowCommand = new RealCommand(log, listenersFactory, ApplicationInstanceData.ALLOW_COMMAND_ID, ApplicationInstanceData.ALLOW_COMMAND_ID, "Allow access to the application instance",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                statusValue.setTypedValues(ApplicationInstance.Status.Allowed);
            }
        };
        rejectCommand = new RealCommand(log, listenersFactory, ApplicationInstanceData.REJECT_COMMAND_ID, ApplicationInstanceData.REJECT_COMMAND_ID, "Reject access to the application instance",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                statusValue.setTypedValues(ApplicationInstance.Status.Rejected);
            }
        };
        statusValue = new RealValue<>(log, listenersFactory, ApplicationInstanceData.STATUS_VALUE_ID, ApplicationInstanceData.STATUS_VALUE_ID,
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
    public RealValue<ApplicationInstance.Status> getStatusValue() {
        return statusValue;
    }
}
