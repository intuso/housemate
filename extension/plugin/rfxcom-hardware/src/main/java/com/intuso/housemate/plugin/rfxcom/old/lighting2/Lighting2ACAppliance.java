package com.intuso.housemate.plugin.rfxcom.old.lighting2;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;

/**
 * Housemate feature that controls a lighting 2 AC appliance over RFXCom
 *
 */
@Id(value = "rfxcom-lighting2-ac", name = "Lighting2 AC", description = "RFXCom Lighting2 AC Appliance")
public class Lighting2ACAppliance extends com.intuso.housemate.plugin.rfxcom.old.lighting2.Lighting2Appliance {

	@Inject
	public Lighting2ACAppliance() {
		super(Lighting2ACHandler.INSTANCE);
	}

    public Lighting2Appliance createAppliance(int houseId, byte unitCode) {
        return Lighting2ACHandler.INSTANCE.makeAppliance(houseId, unitCode);
    }
}
