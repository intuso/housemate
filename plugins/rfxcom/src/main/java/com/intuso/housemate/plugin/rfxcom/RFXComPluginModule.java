package com.intuso.housemate.plugin.rfxcom;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.annotations.plugin.AnnotatedPluginModule;
import com.intuso.housemate.annotations.plugin.DeviceFactories;
import com.intuso.housemate.annotations.plugin.PluginInformation;
import com.intuso.housemate.api.HousemateException;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.HomeEasy;
import gnu.io.CommPortIdentifier;

@PluginInformation(id = "com.intuso.housemate.plugin.rfxcom", name = "RFXCom plugin", description = "Plugin for devices that work using a RFXCom Transceiver")
@DeviceFactories(HomeEasyApplianceFactory.class)
public class RFXComPluginModule extends AnnotatedPluginModule {

    @Inject
    public RFXComPluginModule(Log log) {
        super(log);
    }

    @Provides
    @Singleton
    public HomeEasy getHomeEasy(Log log) throws HousemateException {
        log.d("Initialising RFXCom plugin");
        java.util.List<CommPortIdentifier> portIds = RFXtrx.listSuitablePorts(log);
        CommPortIdentifier portId = null;
        for(CommPortIdentifier pi : portIds) {
            log.d("Found comm port id " + pi.getName());
            if(pi.getName().equals("/dev/ttyUSB0")) {
                log.d("Found required comm port id");
                portId = pi;
                break;
            }
        }
        if(portId == null)
            throw new HousemateException("No suitable portId found for Home Easy devices, shutting down");
        RFXtrx agent = new RFXtrx(portId, log);
        agent.openPort();
        return HomeEasy.createUK(agent);
    }
}
