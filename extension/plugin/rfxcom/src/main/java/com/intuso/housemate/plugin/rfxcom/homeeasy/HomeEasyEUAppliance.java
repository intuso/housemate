package com.intuso.housemate.plugin.rfxcom.homeeasy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.util.lighting2.Appliance;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "home-easy-eu", name = "HomeEasy EU", description = "HomeEasy EU appliance")
public class HomeEasyEUAppliance extends HomeEasyAppliance {

	@Inject
	public HomeEasyEUAppliance(Log log,
                               ListenersFactory listenersFactory,
                               @Assisted DeviceData data) {
		super(log, listenersFactory, "home-easy-eu", data);
	}

    public Appliance createAppliance(int houseId, byte unitCode) {
        return RFXtrx433Hardware.INSTANCE.makeHomeEasyApplianceEU(houseId, unitCode);
	}
}
