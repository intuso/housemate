package com.lisantom.our.housemate;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.real.RealProperty;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.impl.device.OnOffDevice;
import com.intuso.housemate.real.impl.type.IntegerType;
import com.rfxcom.rfxtrx.util.HomeEasy;

import java.io.IOException;

/**
 * Housemate device that controls a USB relay
 * @author tclabon
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
        appliance.addListener(new HomeEasy.KnownApplianceListener() {
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
	
	/**
	 * Check if all the current property values are valid and if so, start the USB relay client
	 * @throws HousemateException
	 */
    @Override
    public void valueChanged(RealProperty<?> property) {
        int id = this.houseId.getTypedValue();
        int unitcode = this.unitId.getTypedValue();
		
        // check the port value is a positive number
        if(id < 0 || id > 0x03FFFFFF)
            getErrorValue().setValue("House id must be between 0 and " + 0x03FFFFFF);
			
		// check the relay value is a number between 1 and 8
		if(unitcode < 1 || unitcode > 16)
            getErrorValue().setValue("Unitcode must be between 1 and 16 (inclusive)");

        getErrorValue().setValue(null);
        createHed(id, unitcode);
	}
	
	@Override
	protected void turnOn() throws HousemateException {
        if(appliance == null)
            throw new HousemateException("Not connected to HomeEasy device. Ensure properties are set");
		try {
			appliance.turnOn();
            setOn();
		} catch (IOException e) {
			throw new HousemateException("Could not turn relay on", e);
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
			throw new HousemateException("Could not turn relay off", e);
		}
	}
	
	@Override
	protected void _start() {
		valueChanged(null);
	}

	@Override
	protected void _stop() {
		appliance = null;
	}
}
