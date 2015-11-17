package com.intuso.housemate.plugin.rfxcom.lighting1;

import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.device.feature.RealStatefulPowerControl;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.utilities.listener.ListenerRegistration;

import java.io.IOException;

/**
 * Housemate device that controls a HomeEasy Appliance
 *
 */
public abstract class Lighting1Appliance implements DeviceDriver, RealStatefulPowerControl {

	private com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance lighting1Appliance;
    private ListenerRegistration listenerRegistration;
    private int houseId = 0;
    private int unitCode = 1;

    private final DeviceDriver.Callback driverCallback;

    @com.intuso.housemate.client.v1_0.real.api.annotations.Values
    protected Values values;

    protected Lighting1Appliance(DeviceDriver.Callback driverCallback) {
        this.driverCallback = driverCallback;
    }

    public void propertyChanged() {
        // check the port value is a positive number
        if(houseId < 0x41 || houseId > 0x50) {
            driverCallback.setError("House id must be between " + 0x41 + " and " + 0x50);
            return;
        }
			
		// check the relay value is a number between 1 and 8
		if(unitCode < 1 || unitCode > 16) {
            driverCallback.setError("Unitcode must be between 1 and 16 (inclusive)");
            return;
        }

        driverCallback.setError(null);

        if(listenerRegistration != null) {
            listenerRegistration.removeListener();
            listenerRegistration = null;
        }
        lighting1Appliance = createAppliance((byte) houseId, (byte) unitCode);
        listenerRegistration = lighting1Appliance.addCallback(new com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance.Callback() {

            @Override
            public void turnedOn(com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance a) {
                values.isOn(true);
            }

            @Override
            public void turnedOff(com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance a) {
                values.isOn(false);
            }
        });
	}

    public abstract com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance createAppliance(byte houseId, byte unitCode);

    public int getHouseId() {
        return houseId;
    }

    @Property("integer")
    @TypeInfo(id = "house-id", name = "House ID", description = "House ID (in decimal)")
    public void setHouseId(int houseId) {
        this.houseId = houseId;
        propertyChanged();
    }

    public int getUnitCode() {
        return unitCode;
    }

    @Property("integer")
    @TypeInfo(id = "unit-id", name = "Unit ID", description = "Unit ID")
    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
        propertyChanged();
    }

    @Override
    public void turnOn() {
        if(lighting1Appliance == null)
            throw new HousemateCommsException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting1Appliance.turnOn();
            values.isOn(true);
		} catch (IOException e) {
			throw new HousemateCommsException("Could not turn appliance on", e);
		}
	}
	
	@Override
    public void turnOff() {
        if(lighting1Appliance == null)
            throw new HousemateCommsException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting1Appliance.turnOff();
            values.isOn(false);
		} catch (IOException e) {
			throw new HousemateCommsException("Could not turn appliance off", e);
		}
	}

    @Override
    public void setOn(boolean on) {
        values.isOn(on);
    }
	
	@Override
	public void start() {
		propertyChanged();
	}

	@Override
    public void stop() {
		lighting1Appliance = null;
	}
}
