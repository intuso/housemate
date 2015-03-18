package com.intuso.housemate.realclient;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.plugin.host.PluginListener;
import com.intuso.housemate.plugin.host.PluginManager;
import com.intuso.housemate.realclient.factory.ConditionFactory;
import com.intuso.housemate.realclient.factory.DeviceFactory;
import com.intuso.housemate.realclient.factory.HardwareFactory;
import com.intuso.housemate.realclient.factory.TaskFactory;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import java.util.Set;

/**
 * Version of a root object for server-internal objects
 */
public class RealClientRoot extends RealRoot implements PluginListener {

    private final RealCommand addHardwareCommand;
    private final RealCommand addDeviceCommand;

    private boolean typesAdded = false;

    @Inject
    public RealClientRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router,
                          RealList<HardwareData, RealHardware> hardwares, RealList<TypeData<?>, RealType<?, ?, ?>> types,
                          RealList<DeviceData, RealDevice> devices, LifecycleHandler lifecycleHandler,
                          final HardwareFactory hardwareFactory, final DeviceFactory deviceFactory,
                          final ConditionFactory conditionFactory, final TaskFactory taskFactory,
                          final PluginManager pluginManager) {
        super(log, listenersFactory, WriteableMapPropertyRepository.newEmptyRepository(listenersFactory, properties), router, hardwares, types, devices);
        addHardwareCommand = lifecycleHandler.createAddHardwareCommand(getHardwares());
        addDeviceCommand = lifecycleHandler.createAddDeviceCommand(getDevices());
        addChild(addHardwareCommand);
        addChild(addDeviceCommand);
        addObjectListener(new RootListener<RealRoot>() {

            @Override
            public void serverConnectionStatusChanged(RealRoot root, ServerConnectionStatus serverConnectionStatus) {
                // do nothing
            }

            @Override
            public void applicationStatusChanged(RealRoot root, ApplicationStatus applicationStatus) {
                // do nothing
            }

            @Override
            public void applicationInstanceStatusChanged(RealRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
                if (!typesAdded && applicationInstanceStatus == ApplicationInstanceStatus.Allowed) {
                    typesAdded = true;
                    addType(hardwareFactory.getType());
                    addType(deviceFactory.getType());
                    addType(conditionFactory.getType());
                    addType(taskFactory.getType());
                    pluginManager.addPluginListener(RealClientRoot.this, true);
                }
            }

            @Override
            public void newApplicationInstance(RealRoot root, String instanceId) {
                // do nothing
            }

            @Override
            public void newServerInstance(RealRoot root, String serverId) {
                // do nothing
            }
        });
    }

    /**
     * Get the add hardware command for this client
     * @return
     */
    public RealCommand getAddHardwareCommand() {
        return addHardwareCommand;
    }

    /**
     * Get the add device command for this client
     * @return
     */
    public RealCommand getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        if(message.getPayload() instanceof HousemateData)
            super.messageReceived(new Message(message.getPath(), message.getType(),
                    ((HousemateData)message.getPayload()).deepClone(),
                    message.getRoute()));
        else
            super.messageReceived(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(message.getPayload() instanceof HousemateData)
            super.sendMessage(new Message(message.getPath(), message.getType(),
                    ((HousemateData)message.getPayload()).deepClone(),
                    message.getRoute()));
        else
            super.sendMessage(message);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(RealType<?, ?, ?> type : pluginInjector.getInstance(new Key<Set<RealType<?, ?, ?>>>() {})) {
            getLog().d("Adding type " + type.getId());
            addType(type);
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove types added by this plugin
    }
}
