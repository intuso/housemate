package com.intuso.housemate.plugin.rfxcom.old.lighting2;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-lighting2-homeeasyeu", name = "Lighting2 HomeEasyEU", description = "RFXCom Lighting2 HomeEasy EU Appliance")
public class Lighting2HomeEasyEUAppliance extends com.intuso.housemate.plugin.rfxcom.old.lighting2.Lighting2Appliance {

	@Inject
	public Lighting2HomeEasyEUAppliance() {
		super(Lighting2HomeEasyEUHandler.INSTANCE);
	}

    public Lighting2Appliance createAppliance(int houseId, byte unitCode) {
        return Lighting2HomeEasyEUHandler.INSTANCE.makeAppliance(houseId, unitCode);
	}
}
