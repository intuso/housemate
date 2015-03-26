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
@TypeInfo(id = "home-easy-uk", name = "HomeEasy UK", description = "HomeEasy UK appliance")
public class HomeEasyUKAppliance extends HomeEasyAppliance {

	@Inject
	public HomeEasyUKAppliance(Log log,
                               ListenersFactory listenersFactory,
                               @Assisted DeviceData data) {
		super(log, listenersFactory, "home-easy-uk", data);
	}

    public Appliance createAppliance(int houseId, byte unitCode) {
        return RFXtrx433Hardware.INSTANCE.makeHomeEasyApplianceUK(houseId, unitCode);
    }
}
