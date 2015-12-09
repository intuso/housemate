package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.ApplicationData;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class ApplicationBridge
        extends BridgeObject<ApplicationData,
        HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>,
            ApplicationBridge,
            Application.Listener<? super ApplicationBridge>>
        implements Application<
            ValueBridge,
            CommandBridge,
            ApplicationInstanceBridge,
        ConvertingListBridge<ApplicationInstanceData, ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge>,
            ApplicationBridge> {

    private final ConvertingListBridge<ApplicationInstanceData, ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge> applicationInstances;
    private final CommandBridge allowCommand;
    private final CommandBridge someCommand;
    private final CommandBridge rejectCommand;
    private final ValueBridge statusValue;

    public ApplicationBridge(Logger logger, ListenersFactory listenersFactory, Application application) {
        super(logger, listenersFactory,
                new ApplicationData(application.getId(), application.getName(), application.getDescription()));
        applicationInstances = new ConvertingListBridge<>(
                logger, listenersFactory, application.getApplicationInstances(), new ApplicationInstanceBridge.Converter(logger, listenersFactory));
        allowCommand = new CommandBridge(logger, listenersFactory, application.getAllowCommand());
        someCommand = new CommandBridge(logger, listenersFactory, application.getSomeCommand());
        rejectCommand = new CommandBridge(logger, listenersFactory, application.getRejectCommand());
        statusValue = new ValueBridge(logger, listenersFactory, application.getStatusValue());
        addChild(applicationInstances);
        addChild(allowCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    @Override
    public ConvertingListBridge<ApplicationInstanceData, ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge> getApplicationInstances() {
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
    public ValueBridge getStatusValue() {
        return statusValue;
    }

    public final static class Converter implements Function<Application<?, ?, ?, ?, ?>, ApplicationBridge> {

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public ApplicationBridge apply(Application<?, ?, ?, ?, ?> application) {
            return new ApplicationBridge(logger, listenersFactory, application);
        }
    }
}
