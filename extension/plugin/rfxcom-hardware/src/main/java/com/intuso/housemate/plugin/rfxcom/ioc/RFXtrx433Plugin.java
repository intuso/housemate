package com.intuso.housemate.plugin.rfxcom.ioc;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.plugin.AnnotatedPlugin;
import com.intuso.housemate.client.v1_0.api.plugin.HardwareDrivers;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;

@Id(value = "com.intuso.housemate.plugin.rfxcom.hardware", name = "RFXCom hardware plugin", description = "Plugin for an RFXCom Transceiver")
@HardwareDrivers(RFXtrx433Hardware.class)
public class RFXtrx433Plugin extends AnnotatedPlugin {}
