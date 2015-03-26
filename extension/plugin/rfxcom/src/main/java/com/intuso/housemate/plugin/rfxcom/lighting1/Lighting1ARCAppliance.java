package com.intuso.housemate.plugin.rfxcom.lighting1;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-lighting1-arc", name = "Lighting1 ARC", description = "RFXCom Lighting1 ARC Appliance")
public class Lighting1ARCAppliance extends com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2Appliance {

	@Inject
	public Lighting1ARCAppliance(Log log,
                                 ListenersFactory listenersFactory,
                                 @Assisted DeviceData data) {
		super(log, listenersFactory, "rfxcom-lighting1-arc", data);
	}

    public Lighting2Appliance createAppliance(int houseId, byte unitCode) {
        return RFXtrx433Hardware.INSTANCE.makeLighting2AC(houseId, unitCode);
    }
}
