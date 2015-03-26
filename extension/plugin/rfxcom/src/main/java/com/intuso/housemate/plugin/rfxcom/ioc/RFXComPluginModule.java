package com.intuso.housemate.plugin.rfxcom.ioc;

import com.google.inject.Inject;
import com.intuso.housemate.plugin.api.Devices;
import com.intuso.housemate.plugin.api.Hardwares;
import com.intuso.housemate.plugin.api.PluginModule;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.housemate.plugin.rfxcom.homeeasy.HomeEasyEUAppliance;
import com.intuso.housemate.plugin.rfxcom.homeeasy.HomeEasyUKAppliance;
import com.intuso.housemate.plugin.rfxcom.temperaturesensor.*;
import com.intuso.utilities.log.Log;

@TypeInfo(id = "com.intuso.housemate.plugin.rfxcom", name = "RFXCom plugin", description = "Plugin for devices that work using a RFXCom Transceiver")
@Hardwares(RFXtrx433Hardware.class)
@Devices({HomeEasyUKAppliance.class,
        HomeEasyEUAppliance.class,
        Temperature1Sensor.class,
        Temperature2Sensor.class,
        Temperature3Sensor.class,
        Temperature4Sensor.class,
        Temperature5Sensor.class})
public class RFXComPluginModule extends PluginModule {
    @Inject
    public RFXComPluginModule(Log log) {
        super(log);
    }
}
