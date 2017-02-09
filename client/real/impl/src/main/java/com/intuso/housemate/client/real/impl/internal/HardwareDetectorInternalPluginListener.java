package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.plugin.HardwareDriver;
import com.intuso.housemate.client.api.internal.plugin.Plugin;
import com.intuso.housemate.client.api.internal.plugin.PluginListener;
import com.intuso.housemate.client.real.impl.internal.type.HardwareDriverType;

import java.util.Map;

/**
 * Created by tomc on 08/02/17.
 */
public class HardwareDetectorInternalPluginListener implements PluginListener {

    private final Injector injector;
    private final HardwareDriverType hardwareDriverType;
    private final RealNodeImpl node;
    private final RealHardwareImpl.Factory hardwareFactory;

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
                detector.detect(new CallbackImpl(id.value()));
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
            hardware.getDriverProperty().set(hardwareDriverType.deserialise(new Type.Instance(driverId)), null);
            for (Map.Entry<String, Object> propertyEntry : properties.entrySet()) {
                // for properties to already exist, this requires the driver to already be initialised which it will be, as long as the types listener is called before this one!
                RealPropertyImpl property = hardware.getProperties().get(propertyEntry.getKey());
                if (property != null)
                    property.set(propertyEntry.getValue(), null);
            }
        }
    }
}
