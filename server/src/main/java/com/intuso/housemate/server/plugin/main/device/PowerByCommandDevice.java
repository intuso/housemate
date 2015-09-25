package com.intuso.housemate.server.plugin.main.device;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.impl.device.StatefulPoweredDevice;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.io.IOException;
import java.util.List;

/**
 * Device that allows On/Off functionality by performing a system command.
 */
@TypeInfo(id = "power-by-command", name = "Power By Command", description = "Device which runs a configured command to turn things on and off")
public final class PowerByCommandDevice extends StatefulPoweredDevice {

    private final RealProperty<String> onCommandProperty;
    private final RealProperty<String> offCommandProperty;

    @Inject
	public PowerByCommandDevice(Log log, ListenersFactory listenersFactory, @Assisted DeviceData data) {
		super(log, listenersFactory, "power-by-command", data);
        onCommandProperty = StringType.createProperty(getLog(), listenersFactory, "on-command", "On Command", "The command to turn the device on", null);
        offCommandProperty = StringType.createProperty(getLog(), listenersFactory, "off-command", "Off Command", "The command to turn the device off", null);
        List<String> propertyIds = data.getCustomPropertyIds() == null ? Lists.<String>newArrayList() : Lists.newArrayList(data.getCustomPropertyIds());
        propertyIds.add(onCommandProperty.getId());
        propertyIds.add(offCommandProperty.getId());
        data.setCustomPropertyIds(propertyIds);
        getProperties().add(onCommandProperty);
        getProperties().add(offCommandProperty);
	}

    /**
	 * Turn the device on
	 */
    public void turnOn() {
        String command = onCommandProperty.getTypedValue();
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                setOn();
            } catch(IOException e) {
                throw new HousemateCommsException("Could not run command to turn device on", e);
            }
        } else
            throw new HousemateCommsException("No command set");
    }

	/**
	 * Turn the device off
	 */
    public void turnOff() {
        String command = offCommandProperty.getTypedValue();
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                setOff();
            } catch(IOException e) {
                throw new HousemateCommsException("Could not run command to turn device off", e);
            }
        } else
            throw new HousemateCommsException("No command set");
    }
}
