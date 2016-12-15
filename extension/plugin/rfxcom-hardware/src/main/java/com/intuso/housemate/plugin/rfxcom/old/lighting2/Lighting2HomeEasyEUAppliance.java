package com.intuso.housemate.plugin.rfxcom.old.lighting2;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.driver.FeatureDriver;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-lighting2-homeeasyeu", name = "Lighting2 HomeEasyEU", description = "RFXCom Lighting2 HomeEasy EU Appliance")
public class Lighting2HomeEasyEUAppliance extends com.intuso.housemate.plugin.rfxcom.old.lighting2.Lighting2Appliance {

	@Inject
	public Lighting2HomeEasyEUAppliance(@Assisted Logger logger, @Assisted FeatureDriver.Callback driverCallback) {
		super(Lighting2HomeEasyEUHandler.INSTANCE, driverCallback);
	}

    public Lighting2Appliance createAppliance(int houseId, byte unitCode) {
        return Lighting2HomeEasyEUHandler.INSTANCE.makeAppliance(houseId, unitCode);
	}
}
