package com.lisantom.our.housemate;

import com.google.common.collect.Lists;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.BrokerConditionFactory;
import com.intuso.housemate.broker.plugin.BrokerConsequenceFactory;
import com.intuso.housemate.broker.plugin.PluginDescriptor;
import com.intuso.housemate.broker.plugin.RealDeviceFactory;
import com.intuso.housemate.real.RealType;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.HomeEasy;
import gnu.io.CommPortIdentifier;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/03/13
 * Time: 11:32
 * To change this template use File | Settings | File Templates.
 */
public class RFXComPlugin implements PluginDescriptor {

    private HomeEasy homeEasy;

    @Override
    public String getId() {
        return RFXComPlugin.class.getName();
    }

    @Override
    public String getDescription() {
        return "Plugin for devices that work using a RFXCom Transceiver";
    }

    @Override
    public String getAuthor() {
        return "Intuso";
    }

    @Override
    public void init(BrokerGeneralResources resources) {
        resources.getLog().d("Initialising RFXCom plugin");
        java.util.List<CommPortIdentifier> portIds = RFXtrx.listSuitablePorts(resources.getLog());
        CommPortIdentifier portId = null;
        for(CommPortIdentifier pi : portIds) {
            resources.getLog().d("Found comm port id " + pi.getName());
            if(pi.getName().equals("/dev/ttyUSB0")) {
                resources.getLog().d("Found required comm port id");
                portId = pi;
                break;
            }
        }
        if(portId == null) {
            resources.getLog().d("No suitable portId found for Home Easy devices, shutting down");
            System.exit(0);
        }
        RFXtrx agent = new RFXtrx(portId, resources.getLog());
        agent.openPort();
        homeEasy = HomeEasy.createUK(agent);
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes() {
        return Lists.newArrayList();
    }

    @Override
    public List<RealDeviceFactory<?>> getDeviceFactories() {
        return Lists.<RealDeviceFactory<?>>newArrayList(new HomeEasyDeviceFactory(homeEasy));
    }

    @Override
    public List<BrokerConditionFactory<?>> getConditionFactories() {
        return Lists.newArrayList();
    }

    @Override
    public List<BrokerConsequenceFactory<?>> getConsequenceFactories() {
        return Lists.newArrayList();
    }
}
