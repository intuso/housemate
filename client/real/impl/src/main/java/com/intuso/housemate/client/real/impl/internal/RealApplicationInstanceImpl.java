package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.real.api.internal.RealApplicationInstance;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.client.real.impl.internal.type.ApplicationInstanceStatusType;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public class RealApplicationInstanceImpl
        extends RealObject<
        ApplicationInstanceData,
        HousemateData<?>,
            RealObject<?, ? ,?, ?>,
            ApplicationInstance.Listener<? super RealApplicationInstance>>
        implements RealApplicationInstance {

    private final RealCommandImpl allowCommand;
    private final RealCommandImpl rejectCommand;
    private final RealValueImpl<Status> statusValue;

    public RealApplicationInstanceImpl(Log log, ListenersFactory listenersFactory, String instanceId,
                                       ApplicationInstanceStatusType applicationInstanceStatusType) {
        super(log, listenersFactory, new ApplicationInstanceData(instanceId, instanceId, instanceId));
        allowCommand = new RealCommandImpl(log, listenersFactory, ApplicationInstanceData.ALLOW_COMMAND_ID, ApplicationInstanceData.ALLOW_COMMAND_ID, "Allow access to the application instance",
                Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                statusValue.setTypedValues(Status.Allowed);
            }
        };
        rejectCommand = new RealCommandImpl(log, listenersFactory, ApplicationInstanceData.REJECT_COMMAND_ID, ApplicationInstanceData.REJECT_COMMAND_ID, "Reject access to the application instance",
                Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                statusValue.setTypedValues(Status.Rejected);
            }
        };
        statusValue = new RealValueImpl<>(log, listenersFactory, ApplicationInstanceData.STATUS_VALUE_ID, ApplicationInstanceData.STATUS_VALUE_ID,
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
    public RealValue<Status> getStatusValue() {
        return statusValue;
    }
}
