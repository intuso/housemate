package com.intuso.housemate.plugin.rfxcom.lighting1;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-lighting1-arc", name = "Lighting1 ARC", description = "RFXCom Lighting1 ARC Appliance")
public class Lighting1ARCAppliance extends Lighting1Appliance {

	@Inject
	public Lighting1ARCAppliance(@Assisted Logger logger, @Assisted DeviceDriver.Callback driverCallback) {
		super(Lighting1ARCHandler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance createAppliance(byte houseId, byte unitCode) {
        return Lighting1ARCHandler.INSTANCE.makeAppliance(houseId, unitCode);
    }
}
