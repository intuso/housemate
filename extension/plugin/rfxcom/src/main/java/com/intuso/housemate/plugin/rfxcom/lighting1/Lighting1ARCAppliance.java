package com.intuso.housemate.plugin.rfxcom.lighting1;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-lighting1-arc", name = "Lighting1 ARC", description = "RFXCom Lighting1 ARC Appliance")
public class Lighting1ARCAppliance extends Lighting1Appliance {

	@Inject
	public Lighting1ARCAppliance(@Assisted RealDevice device) {
		super(device);
	}

    public com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance createAppliance(byte houseId, byte unitCode) {
        return RFXtrx433Hardware.INSTANCE.makeLighting1ARC(houseId, unitCode);
    }
}
