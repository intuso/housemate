package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.object.real.impl.type.EnumChoiceType;
import com.intuso.housemate.object.server.ServerProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public class ApplicationInstanceBridge
        extends BridgeObject<ApplicationInstanceData,
            HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>,
            ApplicationInstanceBridge,
            ApplicationInstanceListener<? super ApplicationInstanceBridge>>
        implements ApplicationInstance<
            ValueBridge,
            CommandBridge,
            ApplicationInstanceBridge> {

    private final CommandBridge allowCommand;
    private final CommandBridge rejectCommand;
    private final ValueBridge statusValue;

    public ApplicationInstanceBridge(Log log, ListenersFactory listenersFactory, ApplicationInstance<?, ?, ?> applicationInstance,
                                     ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory,
                new ApplicationInstanceData(applicationInstance.getId(), applicationInstance.getName(), applicationInstance.getDescription()));
        allowCommand = new CommandBridge(log, listenersFactory, applicationInstance.getAllowCommand(), types);
        rejectCommand = new CommandBridge(log, listenersFactory, applicationInstance.getRejectCommand(), types);
        statusValue = new ValueBridge(log, listenersFactory, applicationInstance.getStatusValue(), types);
        addChild(allowCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    @Override
    public CommandBridge getAllowCommand() {
        return allowCommand;
    }

    @Override
    public CommandBridge getRejectCommand() {
        return rejectCommand;
    }

    @Override
    public ApplicationInstanceStatus getStatus() {
        List<ApplicationInstanceStatus> statuses = ApplicationInstanceStatusType.deserialiseAll(new EnumChoiceType.EnumInstanceSerialiser<>(ApplicationInstanceStatus.class), statusValue.getTypeInstances());
        return statuses != null && statuses.size() > 0 ? statuses.get(0) : null;
    }

    @Override
    public ValueBridge getStatusValue() {
        return statusValue;
    }

    public final static class Converter implements Function<ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListenersFactory listenersFactory, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
            this.types = types;
        }

        @Override
        public ApplicationInstanceBridge apply(ApplicationInstance<?, ?, ?> applicationInstance) {
            return new ApplicationInstanceBridge(log, listenersFactory, applicationInstance, types);
        }
    }
}
