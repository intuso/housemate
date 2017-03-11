package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.plugin.HardwareDriver;
import com.intuso.housemate.client.api.internal.plugin.Plugin;
import com.intuso.housemate.client.api.internal.plugin.PluginListener;
import com.intuso.housemate.client.real.impl.internal.type.HardwareDriverType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by tomc on 08/02/17.
 */
public class HardwareDetectorInternalPluginListener implements PluginListener {

    private final static Logger logger = LoggerFactory.getLogger(HardwareDetectorInternalPluginListener.class);

    private final Injector injector;
    private final HardwareDriverType hardwareDriverType;
    private final RealNodeImpl node;
    private final RealHardwareImpl.Factory hardwareFactory;

    private Command.PerformListener<? super RealCommandImpl> dummyListener = new Command.PerformListener<RealCommandImpl>() {
        @Override
        public void commandStarted(RealCommandImpl command) {}

        @Override
        public void commandFinished(RealCommandImpl command) {}

        @Override
        public void commandFailed(RealCommandImpl command, String error) {}
    };

    @Inject
    public HardwareDetectorInternalPluginListener(Injector injector, HardwareDriverType hardwareDriverType, RealNodeImpl node, RealHardwareImpl.Factory hardwareFactory) {
        this.injector = injector;
        this.hardwareDriverType = hardwareDriverType;
        this.node = node;
        this.hardwareFactory = hardwareFactory;
    }

    @Override
    public void pluginAdded(Plugin plugin) {
        for(HardwareDriver hardwareDriver : plugin.getHardwareDrivers()) {
            Id id = hardwareDriver.value().getAnnotation(Id.class);
            if(id != null) {
                com.intuso.housemate.client.api.internal.driver.HardwareDriver.Detector detector = injector.getInstance(hardwareDriver.detector());
                try {
                    detector.detect(new CallbackImpl(id.value()));
                } catch (Throwable t) {
                    logger.error("Failed to detect hardware", t);
                }
            }
        }
    }

    @Override
    public void pluginRemoved(Plugin plugin) {
        // do nothing
    }

    private class CallbackImpl implements com.intuso.housemate.client.api.internal.driver.HardwareDriver.Detector.Callback {

        private final String driverId;

        private CallbackImpl(String driverId) {
            this.driverId = driverId;
        }

        @Override
        public void create(String id, String name, String description, Map<String, Object> properties) {
            RealHardwareImpl hardware = hardwareFactory.create(ChildUtil.logger(node.getLogger(), Node.HARDWARES_ID, id), id, name, description, node.getHardwares().getRemoveCallback());
            node.getHardwares().add(hardware);
            hardware.getDriverProperty().set(hardwareDriverType.deserialise(new Type.Instance(driverId)), dummyListener);
            for (Map.Entry<String, Object> propertyEntry : properties.entrySet()) {
                // for properties to already exist, this requires the driver to already be initialised which it will be, as long as the types listener is called before this one!
                RealPropertyImpl property = hardware.getProperties().get(propertyEntry.getKey());
                if (property != null)
                    property.set(propertyEntry.getValue(), dummyListener);
            }
            hardware.getStartCommand().perform(new Type.InstanceMap(), dummyListener);
        }
    }
}
