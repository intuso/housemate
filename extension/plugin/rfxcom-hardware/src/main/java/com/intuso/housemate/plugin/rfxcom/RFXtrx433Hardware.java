package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import com.intuso.housemate.extension.homeeasyuk.api.HomeEasyUKAPI;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.rfxcom.rfxtrx.RFXtrx;
import org.slf4j.Logger;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
@Id(value = "rfxtrx433", name = "RFXtr433", description = "RFXCom 433MHz Transceiver")
public class RFXtrx433Hardware implements HardwareDriver, HomeEasyUKAPI {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final Map<Integer, Map<Byte, HomeEasyUKApplianceImpl>> appliances = Maps.newHashMap();

    private HardwareDriver.Callback callback;
    private RFXtrx rfxtrx;
    private String pattern = ".*ttyUSB.*";
    private boolean listen = true;

    @Inject
    public RFXtrx433Hardware(ManagedCollectionFactory managedCollectionFactory) {
        this.managedCollectionFactory = managedCollectionFactory;
    }

    @Property
    @Id(value = "serial-pattern", name = "Serial port pattern", description = "Regex matching acceptable serial port names")
    public void setPattern(String pattern) {
        this.pattern = pattern;
        if(rfxtrx != null)
            rfxtrx.setPatterns(Lists.newArrayList(Pattern.compile(pattern)));
    }

    public String getPattern() {
        return pattern;
    }

    @Property
    @Id(value = "listen", name = "Listen for new devices", description = "Create a new device when a command is received for it")
    public void setListen(boolean listen) {
        this.listen = listen;
        // todo listen for new devices
    }

    @Override
    public void init(Logger logger, HardwareDriver.Callback callback) {
        // setup the connection to the USB device
        this.callback = callback;
        rfxtrx = new RFXtrx(logger, Lists.newArrayList(Pattern.compile(pattern)));
        rfxtrx.openPortSafe();
    }

    @Override
    public void uninit() {
        rfxtrx.closePort();
        rfxtrx = null;
        this.callback = null;
    }

    @Override
    public void initAppliance(int houseId, byte unitCode) {
        HomeEasyUKApplianceImpl appliance = new HomeEasyUKApplianceImpl(managedCollectionFactory, rfxtrx, houseId, unitCode);
        if(appliances.get(houseId) == null)
            appliances.put(houseId, Maps.<Byte, HomeEasyUKApplianceImpl>newHashMap());
        appliances.get(houseId).put(unitCode, appliance);
        callback.addObject(appliance, "" + houseId + unitCode);
    }

    @Override
    public void uninitAppliance(int houseId, byte unitCode) {
        if(appliances.containsKey(houseId) && appliances.get(houseId).containsKey(unitCode)) {
            HomeEasyUKApplianceImpl appliance = appliances.get(houseId).get(unitCode);
            if(appliances.get(houseId).size() == 0)
                appliances.remove(houseId);
            callback.removeObject(appliance);
        }
    }
}
