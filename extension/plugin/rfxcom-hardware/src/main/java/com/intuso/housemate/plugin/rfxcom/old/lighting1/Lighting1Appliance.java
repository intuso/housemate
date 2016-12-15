package com.intuso.housemate.plugin.rfxcom.old.lighting1;

import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.annotations.Property;
import com.intuso.housemate.plugin.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.plugin.v1_0.api.feature.StatefulPowerControl;
import com.intuso.utilities.listener.ListenerRegistration;

import java.io.IOException;

/**
 * Housemate device that controls a HomeEasy Appliance
 *
 */
public abstract class Lighting1Appliance implements FeatureDriver, StatefulPowerControl {

	private com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance lighting1Appliance;
    private ListenerRegistration listenerRegistration;
    private byte houseId = 0;
    private byte unitCode = 1;

    private final Lighting1Handler handler;
    private final FeatureDriver.Callback driverCallback;

    protected PowerValues powerValues;

    protected Lighting1Appliance(Lighting1Handler handler, Callback driverCallback) {
        this.handler = handler;
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
                powerValues.isOn(true);
            }

            @Override
            public void turnedOff(com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance a) {
                powerValues.isOn(false);
            }
        });
	}

    public abstract com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance createAppliance(byte houseId, byte unitCode);

    public int getHouseId() {
        return houseId;
    }

    @Property("integer")
    @Id(value = "house-id", name = "House ID", description = "House ID (in decimal)")
    public void setHouseId(byte houseId) {
        handler.propertiesChanged(this.houseId, unitCode, houseId, unitCode);
        this.houseId = houseId;
        propertyChanged();
    }

    public int getUnitCode() {
        return unitCode;
    }

    @Property("integer")
    @Id(value = "unit-id", name = "Unit ID", description = "Unit ID")
    public void setUnitCode(byte unitCode) {
        handler.propertiesChanged(houseId, this.unitCode, houseId, unitCode);
        this.unitCode = unitCode;
        propertyChanged();
    }

    @Override
    public void turnOn() {
        if(lighting1Appliance == null)
            throw new FeatureException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting1Appliance.turnOn();
            powerValues.isOn(true);
		} catch (IOException e) {
			throw new FeatureException("Could not turn appliance on", e);
		}
	}
	
	@Override
    public void turnOff() {
        if(lighting1Appliance == null)
            throw new FeatureException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting1Appliance.turnOff();
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
		lighting1Appliance = null;
	}
}
