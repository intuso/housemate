package com.intuso.housemate.plugin.rfxcom;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.plugin.api.PluginModule;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.HomeEasy;
import gnu.io.CommPortIdentifier;

/**
 */
public class RFXComPluginModule extends PluginModule {

    @Override
    public TypeInfo getTypeInfo() {
        return new TypeInfo(RFXComPluginModule.class.getName(), "RFXCom plugin",
                "Plugin for devices that work using a RFXCom Transceiver");
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

    @Override
    public void configureDeviceFactories(Multibinder<RealDeviceFactory<?>> deviceFactoryBindings) {
        deviceFactoryBindings.addBinding().to(HomeEasyApplianceFactory.class);
    }
}
