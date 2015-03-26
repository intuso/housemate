package com.intuso.housemate.plugin.rfxcom.homeeasy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.object.real.annotations.Property;
import com.intuso.housemate.object.real.impl.device.StatefulPoweredDevice;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.util.lighting2.Appliance;

import java.io.IOException;

/**
 * Housemate device that controls a HomeEasy Appliance
 *
 */
public abstract class HomeEasyAppliance extends StatefulPoweredDevice {

	private Appliance appliance;
    private ListenerRegistration listenerRegistration;
    private int houseId = 0;
    private int unitCode = 1;

	public HomeEasyAppliance(Log log,
                             ListenersFactory listenersFactory,
                             String type,
                             DeviceData data) {
		super(log, listenersFactory, type, data);
        getCustomPropertyIds().add("house-id");
        getCustomPropertyIds().add("unit-id");
	}

    public void propertyChanged() {
        // check the port value is a positive number
        if(houseId < 0 || houseId > 0x03FFFFFF) {
            getErrorValue().setTypedValues("House id must be between 0 and " + 0x03FFFFFF);
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
        appliance = createAppliance(houseId, (byte) unitCode);
        listenerRegistration = appliance.addCallback(new Appliance.Callback() {

            @Override
            public void turnedOn(Appliance a) {
                setOn();
            }

            @Override
            public void turnedOff(Appliance a) {
                setOff();
            }
        });
	}

    public abstract Appliance createAppliance(int houseId, byte unitCode);

    public int getHouseId() {
        return houseId;
    }

    @Property(id = "house-id", name = "House ID", description = "HomeEasy house ID (in decimal)", typeId = "integer")
    public void setHouseId(int houseId) {
        this.houseId = houseId;
        propertyChanged();
    }

    public int getUnitCode() {
        return unitCode;
    }

    @Property(id = "unit-id", name = "Unit ID", description = "HomeEasy unit ID", typeId = "integer")
    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
        propertyChanged();
    }

    @Override
    public void turnOn() throws HousemateException {
        if(appliance == null)
            throw new HousemateException("Not connected to HomeEasy device. Ensure properties are set");
		try {
			appliance.turnOn();
            setOn();
		} catch (IOException e) {
			throw new HousemateException("Could not turn appliance on", e);
		}
	}
	
	@Override
    public void turnOff() throws HousemateException {
        if(appliance == null)
            throw new HousemateException("Not connected to HomeEasy device. Ensure properties are set");
		try {
			appliance.turnOff();
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
		appliance = null;
	}
}
