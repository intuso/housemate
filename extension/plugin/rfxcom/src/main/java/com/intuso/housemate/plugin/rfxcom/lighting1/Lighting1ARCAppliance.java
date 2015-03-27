package com.intuso.housemate.plugin.rfxcom.lighting1;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-lighting1-arc", name = "Lighting1 ARC", description = "RFXCom Lighting1 ARC Appliance")
public class Lighting1ARCAppliance extends Lighting1Appliance {

	@Inject
	public Lighting1ARCAppliance(Log log,
                                 ListenersFactory listenersFactory,
                                 @Assisted DeviceData data) {
		super(log, listenersFactory, "rfxcom-lighting1-arc", data);
	}

    public com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance createAppliance(byte houseId, byte unitCode) {
        return RFXtrx433Hardware.INSTANCE.makeLighting1ARC(houseId, unitCode);
    }
}
