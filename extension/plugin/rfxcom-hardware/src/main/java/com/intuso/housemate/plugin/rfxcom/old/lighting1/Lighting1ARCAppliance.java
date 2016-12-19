package com.intuso.housemate.plugin.rfxcom.old.lighting1;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-lighting1-arc", name = "Lighting1 ARC", description = "RFXCom Lighting1 ARC Appliance")
public class Lighting1ARCAppliance extends Lighting1Appliance {

	@Inject
	public Lighting1ARCAppliance(@Assisted Logger logger, @Assisted FeatureDriver.Callback driverCallback) {
		super(Lighting1ARCHandler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance createAppliance(byte houseId, byte unitCode) {
        return Lighting1ARCHandler.INSTANCE.makeAppliance(houseId, unitCode);
    }
}
