package com.intuso.housemate.plugin.rfxcom.ioc;

import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.housemate.plugin.rfxcom.lighting1.Lighting1ARCAppliance;
import com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2ACAppliance;
import com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2HomeEasyEUAppliance;
import com.intuso.housemate.plugin.rfxcom.temperaturesensor.*;
import com.intuso.housemate.plugin.v1_0.api.module.AnnotatedPluginModule;
import com.intuso.housemate.plugin.v1_0.api.module.DeviceDrivers;
import com.intuso.housemate.plugin.v1_0.api.module.HardwareDrivers;

@TypeInfo(id = "com.intuso.housemate.plugin.rfxcom", name = "RFXCom plugin", description = "Plugin for devices that work using a RFXCom Transceiver")
@HardwareDrivers(RFXtrx433Hardware.class)
@DeviceDrivers({Lighting1ARCAppliance.class,
        Lighting2ACAppliance.class,
        Lighting2HomeEasyEUAppliance.class,
        Temperature1Sensor.class,
        Temperature2Sensor.class,
        Temperature3Sensor.class,
        Temperature4Sensor.class,
        Temperature5Sensor.class})
public class RFXComPluginModule extends AnnotatedPluginModule {}
