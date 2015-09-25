package com.intuso.housemate.plugin.rfxcom.lighting2;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-lighting2-ac", name = "Lighting2 AC", description = "RFXCom Lighting2 AC Appliance")
public class Lighting2ACAppliance extends com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2Appliance {

	@Inject
	public Lighting2ACAppliance(Log log,
                                ListenersFactory listenersFactory,
                                @Assisted DeviceData data) {
		super(log, listenersFactory, "rfxcom-lighting2-ac", data);
	}

    public Lighting2Appliance createAppliance(int houseId, byte unitCode) {
        return RFXtrx433Hardware.INSTANCE.makeLighting2AC(houseId, unitCode);
    }
}
