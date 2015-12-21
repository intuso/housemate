package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class ApplicationInstanceBridge
        extends BridgeObject<ApplicationInstanceData,
        HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>,
            ApplicationInstanceBridge,
            ApplicationInstance.Listener<? super ApplicationInstanceBridge>>
        implements ApplicationInstance<
            ValueBridge,
            CommandBridge,
            ApplicationInstanceBridge> {

    private final CommandBridge allowCommand;
    private final CommandBridge rejectCommand;
    private final ValueBridge statusValue;

    public ApplicationInstanceBridge(Logger logger, ListenersFactory listenersFactory, ApplicationInstance<?, ?, ?> applicationInstance) {
        super(listenersFactory, logger,
                new ApplicationInstanceData(applicationInstance.getId(), applicationInstance.getName(), applicationInstance.getDescription()));
        allowCommand = new CommandBridge(logger, listenersFactory, applicationInstance.getAllowCommand());
        rejectCommand = new CommandBridge(logger, listenersFactory, applicationInstance.getRejectCommand());
        statusValue = new ValueBridge(logger, listenersFactory, applicationInstance.getStatusValue());
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
    public ValueBridge getStatusValue() {
        return statusValue;
    }

    public final static class Converter implements Function<ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge> {

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public ApplicationInstanceBridge apply(ApplicationInstance<?, ?, ?> applicationInstance) {
            return new ApplicationInstanceBridge(logger, listenersFactory, applicationInstance);
        }
    }
}
