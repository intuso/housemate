package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.object.real.RealHardware;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.annotations.Property;
import com.intuso.housemate.object.real.factory.device.RealDeviceFactory;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.homeeasy.Appliance;
import com.rfxcom.rfxtrx.homeeasy.HomeEasy;
import com.rfxcom.rfxtrx.homeeasy.House;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
@TypeInfo(id = "rfxtrx433", name = "RFXtr433", description = "RFXCom 433MHz Transceiver")
public class RFXtrx433Hardware extends RealHardware {

    public static RFXtrx433Hardware INSTANCE;

    private final RFXtrx rfxtrx = new RFXtrx(getLog(), Lists.<Pattern>newArrayList());
    private final HomeEasy homeEasyUK = HomeEasy.forUK(rfxtrx);
    private final HomeEasy homeEasyEU = HomeEasy.forEU(rfxtrx);
    private ListenerRegistration messageListenerUK;
    private ListenerRegistration messageListenerEU;
    private final SetMultimap<Integer, Byte> knownAppliancesUK = HashMultimap.create();
    private final SetMultimap<Integer, Byte> knownAppliancesEU = HashMultimap.create();
    private final RealDeviceFactory<HomeEasyUKAppliance> homeEasyUKApplianceFactory;
    private final RealDeviceFactory<HomeEasyEUAppliance> homeEasyEUApplianceFactory;
    private final AnnotationProcessor annotationProcessor;

    private final CallbackUK callbackUK = new CallbackUK();
    private final CallbackEU callbackEU = new CallbackEU();

    private String pattern;
    private boolean create;

    @Inject
    public RFXtrx433Hardware(Log log, ListenersFactory listenersFactory, @Assisted HardwareData data,
                             RealDeviceFactory<HomeEasyUKAppliance> homeEasyUKApplianceFactory,
                             RealDeviceFactory<HomeEasyEUAppliance> homeEasyEUApplianceFactory,
                             AnnotationProcessor annotationProcessor) {
        super(log, listenersFactory, "rfxtrx433", data);
        this.homeEasyUKApplianceFactory = homeEasyUKApplianceFactory;
        this.homeEasyEUApplianceFactory = homeEasyEUApplianceFactory;
        this.annotationProcessor = annotationProcessor;
        INSTANCE = this;
    }

    @Property(id = "serial-pattern", name = "Serial port pattern", description = "Regex matching acceptable serial port names", typeId = "string", initialValue = ".*ttyUSB.*")
    public void setPattern(String pattern) {
        this.pattern = pattern;
        rfxtrx.setPatterns(Lists.newArrayList(Pattern.compile(pattern)));
    }

    public String getPattern() {
        return pattern;
    }

    @Property(id = "create", name = "Create devices", description = "Create a new device when a command is received for it", typeId = "boolean", initialValue = "true")
    public void setCreate(boolean create) {
        this.create = create;
        if(messageListenerUK != null) {
            messageListenerUK.removeListener();
            messageListenerUK = null;
        }
        if(messageListenerEU != null) {
            messageListenerEU.removeListener();
            messageListenerEU = null;
        }
        if(create) {
            messageListenerUK = homeEasyUK.addCallback(callbackUK);
            messageListenerEU = homeEasyUK.addCallback(callbackEU);
        }
    }

    public boolean isCreate() {
        return create;
    }

    public Appliance makeApplianceUK(int houseId, byte unitCode) {
        knownAppliancesUK.put(houseId, unitCode);
        return new Appliance(new House(homeEasyUK, houseId), unitCode);
    }

    public Appliance makeApplianceEU(int houseId, byte unitCode) {
        knownAppliancesEU.put(houseId, unitCode);
        return new Appliance(new House(homeEasyEU, houseId), unitCode);
    }

    public void ensureApplianceUK(int houseId, byte unitCode, boolean on) {
        if(!knownAppliancesUK.containsEntry(houseId, unitCode)) {
            try {
                String name = houseId + "/" + (int)unitCode;
                HomeEasyUKAppliance appliance = homeEasyUKApplianceFactory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                appliance.setHouseId(houseId);
                appliance.setUnitCode(unitCode);
                annotationProcessor.process(getRealRoot().getTypes(), appliance);
                if(on)
                    appliance.setOn();
                else
                    appliance.setOff();
                getRealRoot().addDevice(appliance);
            } catch (HousemateException e) {
                getLog().e("Failed to auto-create HomeEasy UK device " + houseId + "/" + (int)unitCode);
            }
        }
    }

    public void ensureApplianceEU(int houseId, byte unitCode, boolean on) {
        if(!knownAppliancesEU.containsEntry(houseId, unitCode)) {
            try {
                String name = houseId + "/" + (int)unitCode;
                HomeEasyEUAppliance appliance = homeEasyEUApplianceFactory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                appliance.setHouseId(houseId);
                appliance.setUnitCode(unitCode);
                annotationProcessor.process(getRealRoot().getTypes(), appliance);
                if(on)
                    appliance.setOn();
                else
                    appliance.setOff();
                getRealRoot().addDevice(appliance);
            } catch (HousemateException e) {
                getLog().e("Failed to auto-create HomeEasy EU device " + houseId + "/" + (int)unitCode);
            }
        }
    }

    private class CallbackUK implements HomeEasy.Callback {

        @Override
        public void turnedOn(int houseId, byte unitCode) {
            ensureApplianceUK(houseId, unitCode, true);
        }

        @Override
        public void turnedOnAll(int houseId) {

        }

        @Override
        public void turnedOff(int houseId, byte unitCode) {
            ensureApplianceUK(houseId, unitCode, false);
        }

        @Override
        public void turnedOffAll(int houseId) {

        }

        @Override
        public void setLevel(int houseId, byte unitCode, byte level) {
            ensureApplianceUK(houseId, unitCode, level != 0);
        }

        @Override
        public void setLevelAll(int houseId, byte level) {

        }
    }

    private class CallbackEU implements HomeEasy.Callback {

        @Override
        public void turnedOn(int houseId, byte unitCode) {
            ensureApplianceEU(houseId, unitCode, true);
        }

        @Override
        public void turnedOnAll(int houseId) {

        }

        @Override
        public void turnedOff(int houseId, byte unitCode) {
            ensureApplianceEU(houseId, unitCode, false);
        }

        @Override
        public void turnedOffAll(int houseId) {

        }

        @Override
        public void setLevel(int houseId, byte unitCode, byte level) {
            ensureApplianceEU(houseId, unitCode, level != 0);
        }

        @Override
        public void setLevelAll(int houseId, byte level) {

        }
    }
}
