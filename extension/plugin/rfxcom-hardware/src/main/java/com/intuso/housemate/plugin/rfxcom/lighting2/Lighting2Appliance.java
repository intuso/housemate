package com.intuso.housemate.plugin.rfxcom.lighting2;

import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.real.api.feature.StatefulPowerControl;
import com.intuso.utilities.listener.ListenerRegistration;

import java.io.IOException;

/**
 * Housemate device that controls a HomeEasy Appliance
 *
 */
public abstract class Lighting2Appliance implements FeatureDriver, StatefulPowerControl {

	private com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance lighting2Appliance;
    private ListenerRegistration listenerRegistration;
    private int houseId = 0;
    private byte unitCode = 1;

    private final Lighting2Handler handler;
    private final FeatureDriver.Callback driverCallback;

    protected StatefulPowerControl.PowerValues powerValues;

    public Lighting2Appliance(Lighting2Handler handler, Callback driverCallback) {
        this.handler = handler;
        this.driverCallback = driverCallback;
    }

    public void propertyChanged() {
        // check the port value is a positive number
        if(houseId < 0 || houseId > 0x03FFFFFF) {
            driverCallback.setError("House id must be between 0 and " + 0x03FFFFFF);
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
        lighting2Appliance = createAppliance(houseId, (byte) unitCode);
        listenerRegistration = lighting2Appliance.addCallback(new com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance.Callback() {

            @Override
            public void turnedOn(com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance a) {
                powerValues.isOn(true);
            }

            @Override
            public void turnedOff(com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance a) {
                powerValues.isOn(false);
            }
        });
	}

    public abstract com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance createAppliance(int houseId, byte unitCode);

    public int getHouseId() {
        return houseId;
    }

    @Property("integer")
    @TypeInfo(id = "house-id", name = "House ID", description = "House ID (in decimal)")
    public void setHouseId(int houseId) {
        handler.propertiesChanged(this.houseId, unitCode, houseId, unitCode);
        this.houseId = houseId;
        propertyChanged();
    }

    public byte getUnitCode() {
        return unitCode;
    }

    @Property("integer")
    @TypeInfo(id = "unit-id", name = "Unit ID", description = "Unit ID")
    public void setUnitCode(byte unitCode) {
        handler.propertiesChanged(houseId, this.unitCode, houseId, unitCode);
        this.unitCode = unitCode;
        propertyChanged();
    }

    @Override
    public void turnOn() {
        if(lighting2Appliance == null)
            throw new FeatureException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting2Appliance.turnOn();
            powerValues.isOn(true);
		} catch (IOException e) {
			throw new FeatureException("Could not turn appliance on", e);
		}
	}
	
	@Override
    public void turnOff() {
        if(lighting2Appliance == null)
            throw new FeatureException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting2Appliance.turnOff();
            powerValues.isOn(false);
		} catch (IOException e) {
			throw new FeatureException("Could not turn appliance off", e);
		}
	}

    public PowerValues getPowerValues() {
        return powerValues;
    }

    @Override
    public void start() {
		propertyChanged();
	}

	@Override
    public void stop() {
		lighting2Appliance = null;
	}
}
