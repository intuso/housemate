package com.intuso.housemate.plugin.rfxcom.ioc;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.plugin.AnnotatedPlugin;
import com.intuso.housemate.client.v1_0.api.plugin.FeatureDrivers;
import com.intuso.housemate.client.v1_0.api.plugin.HardwareDrivers;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.housemate.plugin.rfxcom.old.lighting1.Lighting1ARCAppliance;
import com.intuso.housemate.plugin.rfxcom.old.lighting2.Lighting2ACAppliance;
import com.intuso.housemate.plugin.rfxcom.old.lighting2.Lighting2HomeEasyEUAppliance;
import com.intuso.housemate.plugin.rfxcom.old.temperaturesensor.*;

@Id(value = "com.intuso.housemate.plugin.rfxcom.hardware", name = "RFXCom hardware plugin", description = "Plugin for an RFXCom Transceiver")
@HardwareDrivers(RFXtrx433Hardware.class)
@FeatureDrivers({Lighting1ARCAppliance.class,
        Lighting2ACAppliance.class,
        Lighting2HomeEasyEUAppliance.class,
        Temperature1Sensor.class,
        Temperature2Sensor.class,
        Temperature3Sensor.class,
        Temperature4Sensor.class,
        Temperature5Sensor.class})
public class RFXComPlugin extends AnnotatedPlugin {}