package com.intuso.housemate.plugin.rfxcom.old.lighting1;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.feature.PowerControl;
import com.intuso.utilities.listener.ManagedCollection;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Housemate device that controls a HomeEasy Appliance
 *
 */
public abstract class Lighting1Appliance implements FeatureDriver, PowerControl.Stateful {

	private com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance lighting1Appliance;
    private ManagedCollection.Registration listenerRegistration;
    private byte houseId = 0;
    private byte unitCode = 1;

    private final Lighting1Handler handler;
    private FeatureDriver.Callback callback;

    protected Listener listener;

    protected Lighting1Appliance(Lighting1Handler handler) {
        this.handler = handler;
    }

    public void propertyChanged() {
        // check the port value is a positive number
        if(houseId < 0x41 || houseId > 0x50) {
            callback.setError("House id must be between " + 0x41 + " and " + 0x50);
            return;
        }
			
		// check the relay value is a number between 1 and 8
		if(unitCode < 1 || unitCode > 16) {
            callback.setError("Unitcode must be between 1 and 16 (inclusive)");
            return;
        }

        callback.setError(null);

        if(listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
        lighting1Appliance = createAppliance((byte) houseId, (byte) unitCode);
        listenerRegistration = lighting1Appliance.addCallback(new com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance.Callback() {

            @Override
            public void turnedOn(com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance a) {
                listener.on(true);
            }

            @Override
            public void turnedOff(com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance a) {
                listener.on(false);
            }
        });
	}

    public abstract com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance createAppliance(byte houseId, byte unitCode);

    public int getHouseId() {
        return houseId;
    }

    @Property
    @Id(value = "house-id", name = "House ID", description = "House ID (in decimal)")
    public void setHouseId(byte houseId) {
        handler.propertiesChanged(this.houseId, unitCode, houseId, unitCode);
        this.houseId = houseId;
        propertyChanged();
    }

    public int getUnitCode() {
        return unitCode;
    }

    @Property
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
            listener.on(true);
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
            listener.on(false);
		} catch (IOException e) {
			throw new FeatureException("Could not turn appliance off", e);
		}
	}

    public boolean isOn() {
        return true; // todo
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        return null;
        // todo;
    }

    public Listener getListener() {
        return listener;
    }

    @Override
    public void init(Logger logger, FeatureDriver.Callback callback) {
        this.callback = callback;
        propertyChanged();
    }

    @Override
    public void uninit() {
        callback = null;
        if(listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
        lighting1Appliance = null;
    }
}
