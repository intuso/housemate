package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.device.OnOffDevice;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.rfxcom.rfxtrx.util.HomeEasy;

import java.io.IOException;

/**
 * Housemate device that controls a USB relay
 *
 */
public class HomeEasyAppliance extends OnOffDevice implements ValueListener<RealProperty<?>> {

    private HomeEasy homeEasy;

	/**
	 * The unit to control
	 */
	private HomeEasy.Appliance appliance;

    /**
     * The number of the relay this device "controls"
     */
    public final RealProperty<Integer> houseId = IntegerType.createProperty(getResources(), "house-id", "House ID", "HomeEasy house ID (in decimal)", 0);

    /**
     * The number of the relay this device "controls"
     */
    public final RealProperty<Integer> unitId = IntegerType.createProperty(getResources(), "unit-id", "Unit ID", "HomeEasy unit ID", 1);

	/**
	 * Create a new USB relay device
	 * @param name the name of the device
	 * @throws HousemateException
	 */
	public HomeEasyAppliance(RealResources resources, HomeEasy homeEasy, String id, String name, String description) throws HousemateException {
		super(resources, id, name, description);
        getProperties().add(this.houseId);
        getProperties().add(this.unitId);
        this.homeEasy = homeEasy;
        this.houseId.addObjectListener(this);
        this.unitId.addObjectListener(this);
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
            getErrorValue().setTypedValue(this.houseId.getName() + " has not been set");
            return;
        }

        Integer unitId = this.unitId.getTypedValue();
        if(unitId == null) {
            getErrorValue().setTypedValue(this.unitId.getName() + " has not been set");
            return;
        }
		
        // check the port value is a positive number
        if(houseId < 0 || houseId > 0x03FFFFFF) {
            getErrorValue().setTypedValue("House id must be between 0 and " + 0x03FFFFFF);
            return;
        }
			
		// check the relay value is a number between 1 and 8
		if(unitId < 1 || unitId > 16) {
            getErrorValue().setTypedValue("Unitcode must be between 1 and 16 (inclusive)");
            return;
        }

        getErrorValue().setTypedValue(null);
        createHed(houseId, unitId);
	}
	
	@Override
	protected void turnOn() throws HousemateException {
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
	protected void turnOff() throws HousemateException {
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
