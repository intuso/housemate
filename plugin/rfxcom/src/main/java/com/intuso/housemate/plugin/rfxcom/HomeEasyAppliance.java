package com.intuso.housemate.plugin.rfxcom;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.impl.device.StatefulPoweredDevice;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.util.HomeEasy;

import java.io.IOException;
import java.util.Arrays;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "home-easy", name = "Home Easy", description = "Remote Home Easy switch")
public class HomeEasyAppliance extends StatefulPoweredDevice implements ValueListener<RealProperty<?>> {

    private HomeEasy homeEasy;

	/**
	 * The unit to control
	 */
	private HomeEasy.Appliance appliance;

    /**
     * The number of the relay this device "controls"
     */
    public final RealProperty<Integer> houseId = IntegerType.createProperty(getLog(), getListenersFactory(), "house-id", "House ID", "HomeEasy house ID (in decimal)", Arrays.asList(0));

    /**
     * The number of the relay this device "controls"
     */
    public final RealProperty<Integer> unitId = IntegerType.createProperty(getLog(), getListenersFactory(), "unit-id", "Unit ID", "HomeEasy unit ID", Arrays.asList(1));

    @Inject
	public HomeEasyAppliance(Log log,
                             ListenersFactory listenersFactory,
                             @Assisted DeviceData data,
                             HomeEasy homeEasy) {
		super(log, listenersFactory, data);
        getCustomPropertyIds().add(houseId.getId());
        getProperties().add(houseId);
        getCustomPropertyIds().add(unitId.getId());
        getProperties().add(unitId);
        this.homeEasy = homeEasy;
        houseId.addObjectListener(this);
        unitId.addObjectListener(this);
	}
	
	/**
	 * Start the USB relay
	 * @param id
     * @param unitcode
	 * @throws HousemateException 
	 */
	private void createHed(int id, int unitcode) {
		
        appliance = homeEasy.createAppliance(id, (byte) unitcode);
        appliance.addListener(new HomeEasy.KnownApplianceStateListener() {
            @Override
            public void nowOff(HomeEasy.Appliance a) {
                setOff();
            }

            @Override
            public void nowOn(HomeEasy.Appliance a) {
                setOn();
            }
        });
	}

    @Override
    public void valueChanging(RealProperty<?> value) {
        // do nothing
    }

    /**
	 * Check if all the current property values are valid and if so, start the USB relay client
	 * @throws HousemateException
	 */
    @Override
    public void valueChanged(RealProperty<?> property) {
        Integer houseId = this.houseId.getTypedValue();
        if(houseId == null) {
            getErrorValue().setTypedValues(this.houseId.getName() + " has not been set");
            return;
        }

        Integer unitId = this.unitId.getTypedValue();
        if(unitId == null) {
            getErrorValue().setTypedValues(this.unitId.getName() + " has not been set");
            return;
        }
		
        // check the port value is a positive number
        if(houseId < 0 || houseId > 0x03FFFFFF) {
            getErrorValue().setTypedValues("House id must be between 0 and " + 0x03FFFFFF);
            return;
        }
			
		// check the relay value is a number between 1 and 8
		if(unitId < 1 || unitId > 16) {
            getErrorValue().setTypedValues("Unitcode must be between 1 and 16 (inclusive)");
            return;
        }

        getErrorValue().setTypedValues((String)null);
        createHed(houseId, unitId);
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
		valueChanged(null);
	}

	@Override
	protected void stop() {
		appliance = null;
	}
}
