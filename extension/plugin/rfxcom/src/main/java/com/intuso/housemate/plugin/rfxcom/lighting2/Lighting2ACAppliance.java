package com.intuso.housemate.plugin.rfxcom.lighting2;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-lighting2-ac", name = "Lighting2 AC", description = "RFXCom Lighting2 AC Appliance")
public class Lighting2ACAppliance extends com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2Appliance {

	@Inject
	public Lighting2ACAppliance(@Assisted DeviceDriver.Callback driverCallback) {
		super(Lighting2ACHandler.INSTANCE, driverCallback);
	}

    public Lighting2Appliance createAppliance(int houseId, byte unitCode) {
        return Lighting2ACHandler.INSTANCE.makeAppliance(houseId, unitCode);
    }
}
