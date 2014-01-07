package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.*;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.HomeEasy;
import gnu.io.CommPortIdentifier;

import java.util.List;

/**
 */
public class RFXComPlugin implements PluginDescriptor {

    private HomeEasy homeEasy;

    @Override
    public String getId() {
        return RFXComPlugin.class.getName();
    }

    @Override
    public String getName() {
        return "RFXCom plugin";
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
    public void init(Log log, Injector injector) throws HousemateException {
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
        homeEasy = HomeEasy.createUK(agent);
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes(Log log) {
        return Lists.newArrayList();
    }

    @Override
    public List<Comparator<?>> getComparators(Log log) {
        return Lists.newArrayList();
    }

    @Override
    public List<Operator<?, ?>> getOperators(Log log) {
        return Lists.newArrayList();
    }

    @Override
    public List<Transformer<?, ?>> getTransformers(Log log) {
        return Lists.newArrayList();
    }

    @Override
    public List<RealDeviceFactory<?>> getDeviceFactories() {
        return Lists.<RealDeviceFactory<?>>newArrayList(new HomeEasyApplianceFactory(homeEasy));
    }

    @Override
    public List<ServerConditionFactory<?>> getConditionFactories() {
        return Lists.newArrayList();
    }

    @Override
    public List<ServerTaskFactory<?>> getTaskFactories() {
        return Lists.newArrayList();
    }
}
