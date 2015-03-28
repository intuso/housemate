package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.ApplicationListener;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.object.real.impl.type.ApplicationStatusType;
import com.intuso.housemate.object.real.impl.type.EnumChoiceType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public class ApplicationBridge
        extends BridgeObject<ApplicationData,
            HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>,
            ApplicationBridge,
            ApplicationListener<? super ApplicationBridge>>
        implements Application<
            ValueBridge,
            CommandBridge,
            ApplicationInstanceBridge,
            ListBridge<ApplicationInstanceData, ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge>,
            ApplicationBridge> {

    private final ListBridge<ApplicationInstanceData, ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge> applicationInstances;
    private final CommandBridge allowCommand;
    private final CommandBridge someCommand;
    private final CommandBridge rejectCommand;
    private final ValueBridge statusValue;

    public ApplicationBridge(Log log, ListenersFactory listenersFactory, Application application) {
        super(log, listenersFactory,
                new ApplicationData(application.getId(), application.getName(), application.getDescription()));
        applicationInstances = new SingleListBridge<>(
                log, listenersFactory, application.getApplicationInstances(), new ApplicationInstanceBridge.Converter(log, listenersFactory));
        allowCommand = new CommandBridge(log, listenersFactory, application.getAllowCommand());
        someCommand = new CommandBridge(log, listenersFactory, application.getSomeCommand());
        rejectCommand = new CommandBridge(log, listenersFactory, application.getRejectCommand());
        statusValue = new ValueBridge(log, listenersFactory, application.getStatusValue());
        addChild(applicationInstances);
        addChild(allowCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    @Override
    public ListBridge<ApplicationInstanceData, ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge> getApplicationInstances() {
        return applicationInstances;
    }

    @Override
    public CommandBridge getAllowCommand() {
        return allowCommand;
    }

    @Override
    public CommandBridge getSomeCommand() {
        return someCommand;
    }

    @Override
    public CommandBridge getRejectCommand() {
        return rejectCommand;
    }

    @Override
    public ApplicationStatus getStatus() {
        List<ApplicationStatus> statuses = ApplicationStatusType.deserialiseAll(new EnumChoiceType.EnumInstanceSerialiser<>(ApplicationStatus.class), statusValue.getTypeInstances());
        return statuses != null && statuses.size() > 0 ? statuses.get(0) : null;
    }

    @Override
    public ValueBridge getStatusValue() {
        return statusValue;
    }

    public final static class Converter implements Function<Application<?, ?, ?, ?, ?>, ApplicationBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public ApplicationBridge apply(Application<?, ?, ?, ?, ?> application) {
            return new ApplicationBridge(log, listenersFactory, application);
        }
    }
}
