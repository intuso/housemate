package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.ApplicationListener;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

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

    public ApplicationBridge(Log log, ListenersFactory listenersFactory, Application application,
                             ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory,
                new ApplicationData(application.getId(), application.getName(), application.getDescription()));
        applicationInstances = new SingleListBridge<ApplicationInstanceData, ApplicationInstance<?, ?, ?>, ApplicationInstanceBridge>(
                log, listenersFactory, application.getApplicationInstances(), new ApplicationInstanceBridge.Converter(log, listenersFactory, types));
        allowCommand = new CommandBridge(log, listenersFactory, application.getAllowCommand(), types);
        someCommand = new CommandBridge(log, listenersFactory, application.getSomeCommand(), types);
        rejectCommand = new CommandBridge(log, listenersFactory, application.getRejectCommand(), types);
        statusValue = new ValueBridge(log, listenersFactory, application.getStatusValue(), types);
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
        throw new HousemateRuntimeException("This should not be called on this type of object");
    }

    @Override
    public ValueBridge getStatusValue() {
        return statusValue;
    }

    public final static class Converter implements Function<Application<?, ?, ?, ?, ?>, ApplicationBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListenersFactory listenersFactory, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
            this.types = types;
        }

        @Override
        public ApplicationBridge apply(Application<?, ?, ?, ?, ?> application) {
            return new ApplicationBridge(log, listenersFactory, application, types);
        }
    }
}
