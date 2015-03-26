package com.intuso.housemate.plugin.rfxcom.lighting2;

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
@TypeInfo(id = "rfxcom-lighting2-homeeasyeu", name = "Lighting2 HomeEasyEU", description = "RFXCom Lighting2 HomeEasy EU Appliance")
public class Lighting2HomeEasyEUAppliance extends com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2Appliance {

	@Inject
	public Lighting2HomeEasyEUAppliance(Log log,
                                        ListenersFactory listenersFactory,
                                        @Assisted DeviceData data) {
		super(log, listenersFactory, "rfxcom-lighting2-homeeasyeu", data);
	}

    public Lighting2Appliance createAppliance(int houseId, byte unitCode) {
        return RFXtrx433Hardware.INSTANCE.makeHomeEasyApplianceEU(houseId, unitCode);
	}
}
