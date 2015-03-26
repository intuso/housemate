package com.intuso.housemate.plugin.rfxcom.lighting1;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.object.real.annotations.Property;
import com.intuso.housemate.object.real.impl.device.StatefulPoweredDevice;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.io.IOException;

/**
 * Housemate device that controls a HomeEasy Appliance
 *
 */
public abstract class Lighting1Appliance extends StatefulPoweredDevice {

	private com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance lighting1Appliance;
    private ListenerRegistration listenerRegistration;
    private int houseId = 0;
    private int unitCode = 1;

	public Lighting1Appliance(Log log,
                              ListenersFactory listenersFactory,
                              String type,
                              DeviceData data) {
		super(log, listenersFactory, type, data);
        getCustomPropertyIds().add("house-id");
        getCustomPropertyIds().add("unit-id");
	}

    public void propertyChanged() {
        // check the port value is a positive number
        if(houseId < 0x41 || houseId > 0x50) {
            getErrorValue().setTypedValues("House id must be between " + 0x41 + " and " + 0x50);
            return;
        }
			
		// check the relay value is a number between 1 and 8
		if(unitCode < 1 || unitCode > 16) {
            getErrorValue().setTypedValues("Unitcode must be between 1 and 16 (inclusive)");
            return;
        }

        getErrorValue().setTypedValues((String)null);

        if(listenerRegistration != null) {
            listenerRegistration.removeListener();
            listenerRegistration = null;
        }
        lighting1Appliance = createAppliance((byte) houseId, (byte) unitCode);
        listenerRegistration = lighting1Appliance.addCallback(new com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance.Callback() {

            @Override
            public void turnedOn(com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance a) {
                setOn();
            }

            @Override
            public void turnedOff(com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance a) {
                setOff();
            }
        });
	}

    public abstract com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance createAppliance(byte houseId, byte unitCode);

    public int getHouseId() {
        return houseId;
    }

    @Property(id = "house-id", name = "House ID", description = "House ID (in decimal)", typeId = "integer")
    public void setHouseId(int houseId) {
        this.houseId = houseId;
        propertyChanged();
    }

    public int getUnitCode() {
        return unitCode;
    }

    @Property(id = "unit-id", name = "Unit ID", description = "Unit ID", typeId = "integer")
    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
        propertyChanged();
    }

    @Override
    public void turnOn() throws HousemateException {
        if(lighting1Appliance == null)
            throw new HousemateException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting1Appliance.turnOn();
            setOn();
		} catch (IOException e) {
			throw new HousemateException("Could not turn appliance on", e);
		}
	}
	
	@Override
    public void turnOff() throws HousemateException {
        if(lighting1Appliance == null)
            throw new HousemateException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting1Appliance.turnOff();
            setOff();
		} catch (IOException e) {
			throw new HousemateException("Could not turn appliance off", e);
		}
	}
	
	@Override
	protected void start() {
		propertyChanged();
	}

	@Override
	protected void stop() {
		lighting1Appliance = null;
	}
}
