package com.intuso.housemate.plugin.rfxcom.lighting2;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.v1_0.api.annotations.TypeInfo;
import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-lighting2-ac", name = "Lighting2 AC", description = "RFXCom Lighting2 AC Appliance")
public class Lighting2ACAppliance extends com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2Appliance {

	@Inject
	public Lighting2ACAppliance(@Assisted Logger logger, @Assisted DeviceDriver.Callback driverCallback) {
		super(Lighting2ACHandler.INSTANCE, driverCallback);
	}

    public Lighting2Appliance createAppliance(int houseId, byte unitCode) {
        return Lighting2ACHandler.INSTANCE.makeAppliance(houseId, unitCode);
    }
}
