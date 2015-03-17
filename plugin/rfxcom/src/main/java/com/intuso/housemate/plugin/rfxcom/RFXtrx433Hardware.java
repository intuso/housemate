package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.object.real.RealHardware;
import com.intuso.housemate.object.real.annotations.Property;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.homeeasy.HomeEasy;

import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
public class RFXtrx433Hardware extends RealHardware implements HomeEasy.Callback {

    private final RFXtrx rfxtrx = new RFXtrx(getLog(), Lists.<Pattern>newArrayList());
    private final HomeEasy homeEasy = HomeEasy.forUK(rfxtrx);
    private ListenerRegistration messageListener;

    @Inject
    public RFXtrx433Hardware(Log log, ListenersFactory listenersFactory, @Assisted HardwareData data) {
        super(log, listenersFactory, data);
    }

    @Property(id = "serial-pattern", name = "Serial port pattern", description = "Regex matching acceptable serial port names", typeId = "string", initialValue = ".*ttyUSB.*")
    public void setPattern(String pattern) {
        rfxtrx.setPatterns(Lists.newArrayList(Pattern.compile(pattern)));
    }

    @Property(id = "create", name = "Create devices", description = "Create a new device when a command is received for it", typeId = "boolean", initialValue = "true")
    public void setCreate(boolean create) {
        if(messageListener != null) {
            messageListener.removeListener();
            messageListener = null;
        }
        if(create)
            messageListener = homeEasy.addCallback(this);
    }

    @Override
    public void turnedOn(int houseId, byte unitCode) {

    }

    @Override
    public void turnedOnAll(int houseId) {

    }

    @Override
    public void turnedOff(int houseId, byte unitCode) {

    }

    @Override
    public void turnedOffAll(int houseId) {

    }

    @Override
    public void setLevel(int houseId, byte unitCode, byte level) {

    }

    @Override
    public void setLevelAll(int houseId, byte level) {

    }
}
