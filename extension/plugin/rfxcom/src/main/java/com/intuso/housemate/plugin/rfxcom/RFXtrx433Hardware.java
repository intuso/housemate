package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.client.v1_0.real.api.annotations.AnnotationProcessor;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.factory.device.RealDeviceFactory;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.comms.v1_0.api.payload.HardwareData;
import com.intuso.housemate.plugin.rfxcom.lighting1.Lighting1ARCAppliance;
import com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2ACAppliance;
import com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2HomeEasyEUAppliance;
import com.intuso.housemate.plugin.rfxcom.temperaturesensor.*;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1House;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2House;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;

import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
@TypeInfo(id = "rfxtrx433", name = "RFXtr433", description = "RFXCom 433MHz Transceiver")
public class RFXtrx433Hardware extends RealHardware {

    public static RFXtrx433Hardware INSTANCE;

    private final RFXtrx rfxtrx;

    // lighting1 arc stuff
    private final Lighting1 lighting1ARC;
    private ListenerRegistration messageListenerLighting1ARC;
    private final SetMultimap<Byte, Byte> knownLighting1ARC = HashMultimap.create();
    private final RealDeviceFactory<Lighting1ARCAppliance> lighting1ARCFactory;
    private final CallbackLighting1ARC callbackLighting1ARC = new CallbackLighting1ARC();

    // lighting2 ac stuff
    private final Lighting2 lighting2UK;
    private ListenerRegistration messageListenerLighting2AC;
    private final SetMultimap<Integer, Byte> knownLighting2AC = HashMultimap.create();
    private final RealDeviceFactory<Lighting2ACAppliance> lighting2ACFactory;
    private final CallbackLighting2AC callbackLighting2AC = new CallbackLighting2AC();

    // lighting2 home easy eu stuff
    private final Lighting2 lighting2HomeEasyEU;
    private ListenerRegistration messageListenerLighting2HomeEasyEU;
    private final SetMultimap<Integer, Byte> knownLighting2HomeEasyEU = HashMultimap.create();
    private final RealDeviceFactory<Lighting2HomeEasyEUAppliance> lighting2HomeEasyEUFactory;
    private final CallbackLighting2HomeEasyEU callbackLighting2HomeEasyEU = new CallbackLighting2HomeEasyEU();

    // temperature sensor 1 stuff
    private final TemperatureSensors temperature1;
    private ListenerRegistration messageListenerTemperature1;
    private final Set<Integer> knownTemperature1 = Sets.newHashSet();
    private final RealDeviceFactory<Temperature1Sensor> temperature1Factory;
    private final CallbackTemperature1 callbackTemperature1 = new CallbackTemperature1();

    // temperature sensor 2 stuff
    private final TemperatureSensors temperature2;
    private ListenerRegistration messageListenerTemperature2;
    private final Set<Integer> knownTemperature2 = Sets.newHashSet();
    private final RealDeviceFactory<Temperature2Sensor> temperature2Factory;
    private final CallbackTemperature2 callbackTemperature2 = new CallbackTemperature2();

    // temperature sensor 3 stuff
    private final TemperatureSensors temperature3;
    private ListenerRegistration messageListenerTemperature3;
    private final Set<Integer> knownTemperature3 = Sets.newHashSet();
    private final RealDeviceFactory<Temperature3Sensor> temperature3Factory;
    private final CallbackTemperature3 callbackTemperature3 = new CallbackTemperature3();

    // temperature sensor 4 stuff
    private final TemperatureSensors temperature4;
    private ListenerRegistration messageListenerTemperature4;
    private final Set<Integer> knownTemperature4 = Sets.newHashSet();
    private final RealDeviceFactory<Temperature4Sensor> temperature4Factory;
    private final CallbackTemperature4 callbackTemperature4 = new CallbackTemperature4();

    // temperature sensor 5 stuff
    private final TemperatureSensors temperature5;
    private ListenerRegistration messageListenerTemperature5;
    private final Set<Integer> knownTemperature5 = Sets.newHashSet();
    private final RealDeviceFactory<Temperature5Sensor> temperature5Factory;
    private final CallbackTemperature5 callbackTemperature5 = new CallbackTemperature5();

    private final AnnotationProcessor annotationProcessor;

    private String pattern;
    private boolean create;

    @Inject
    public RFXtrx433Hardware(Log log, ListenersFactory listenersFactory, @Assisted HardwareData data,
                             RealDeviceFactory<Lighting1ARCAppliance> lighting1ARCFactory,
                             RealDeviceFactory<Lighting2ACAppliance> lighting2ACFactory,
                             RealDeviceFactory<Lighting2HomeEasyEUAppliance> lighting2HomeEasyEUFactory,
                             RealDeviceFactory<Temperature1Sensor> temperature1Factory,
                             RealDeviceFactory<Temperature2Sensor> temperature2Factory,
                             RealDeviceFactory<Temperature3Sensor> temperature3Factory,
                             RealDeviceFactory<Temperature4Sensor> temperature4Factory,
                             RealDeviceFactory<Temperature5Sensor> temperature5Factory,
                             AnnotationProcessor annotationProcessor) {
        super(log, listenersFactory, "rfxtrx433", data);

        INSTANCE = this;
        this.annotationProcessor = annotationProcessor;

        // injected factories
        this.lighting1ARCFactory = lighting1ARCFactory;
        this.lighting2ACFactory = lighting2ACFactory;
        this.lighting2HomeEasyEUFactory = lighting2HomeEasyEUFactory;
        this.temperature1Factory = temperature1Factory;
        this.temperature2Factory = temperature2Factory;
        this.temperature3Factory = temperature3Factory;
        this.temperature4Factory = temperature4Factory;
        this.temperature5Factory = temperature5Factory;

        // setup the connection to the USB device
        rfxtrx = new RFXtrx(getLog(), Lists.<Pattern>newArrayList());

        // create the wrappers
        lighting1ARC = Lighting1.forARC(rfxtrx);
        lighting2UK = Lighting2.forAC(rfxtrx);
        lighting2HomeEasyEU = Lighting2.forHomeEasyEU(rfxtrx);
        temperature1 = TemperatureSensors.forTemp1(rfxtrx);
        temperature2 = TemperatureSensors.forTemp2(rfxtrx);
        temperature3 = TemperatureSensors.forTemp3(rfxtrx);
        temperature4 = TemperatureSensors.forTemp4(rfxtrx);
        temperature5 = TemperatureSensors.forTemp5(rfxtrx);

        // set the properties to finish setting everything up
        setPattern(".*ttyUSB.*");
        setCreate(true);
        rfxtrx.openPortSafe();
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
        if(messageListenerLighting1ARC != null) {
            messageListenerLighting1ARC.removeListener();
            messageListenerLighting1ARC = null;
        }
        if(messageListenerLighting2AC != null) {
            messageListenerLighting2AC.removeListener();
            messageListenerLighting2AC = null;
        }
        if(messageListenerLighting2HomeEasyEU != null) {
            messageListenerLighting2HomeEasyEU.removeListener();
            messageListenerLighting2HomeEasyEU = null;
        }
        if(messageListenerTemperature1 != null) {
            messageListenerTemperature1.removeListener();
            messageListenerTemperature1 = null;
        }
        if(messageListenerTemperature2 != null) {
            messageListenerTemperature2.removeListener();
            messageListenerTemperature2 = null;
        }
        if(messageListenerTemperature3 != null) {
            messageListenerTemperature3.removeListener();
            messageListenerTemperature3 = null;
        }
        if(messageListenerTemperature4 != null) {
            messageListenerTemperature4.removeListener();
            messageListenerTemperature4 = null;
        }
        if(messageListenerTemperature5 != null) {
            messageListenerTemperature5.removeListener();
            messageListenerTemperature5 = null;
        }
        if(create) {
            messageListenerLighting1ARC = lighting1ARC.addCallback(callbackLighting1ARC);
            messageListenerLighting2AC = lighting2UK.addCallback(callbackLighting2AC);
            messageListenerLighting2HomeEasyEU = lighting2HomeEasyEU.addCallback(callbackLighting2HomeEasyEU);
            messageListenerTemperature1 = temperature1.addCallback(callbackTemperature1);
            messageListenerTemperature2 = temperature2.addCallback(callbackTemperature2);
            messageListenerTemperature3 = temperature3.addCallback(callbackTemperature3);
            messageListenerTemperature4 = temperature4.addCallback(callbackTemperature4);
            messageListenerTemperature5 = temperature5.addCallback(callbackTemperature5);
        }
    }

    public boolean isCreate() {
        return create;
    }

    public Lighting1Appliance makeLighting1ARC(byte houseId, byte unitCode) {
        knownLighting1ARC.put(houseId, unitCode);
        return new Lighting1Appliance(new Lighting1House(lighting1ARC, houseId), unitCode);
    }

    public void ensureLighting1ARC(byte houseId, byte unitCode, boolean on) {
        if(!knownLighting1ARC.containsEntry(houseId, unitCode)) {
            try {
                String name = "Lighting1 ARC " + houseId + "/" + (int)unitCode;
                Lighting1ARCAppliance appliance = lighting1ARCFactory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                appliance.setHouseId(houseId);
                appliance.setUnitCode(unitCode);
                annotationProcessor.process(getRealRoot().getTypes(), appliance);
                if(on)
                    appliance.setOn();
                else
                    appliance.setOff();
                getRealRoot().addDevice(appliance);
            } catch (Throwable t) {
                getLog().e("Failed to auto-create Lighting1 ARC device " + houseId + "/" + (int)unitCode);
            }
        }
    }

    public Lighting2Appliance makeLighting2AC(int houseId, byte unitCode) {
        knownLighting2AC.put(houseId, unitCode);
        return new Lighting2Appliance(new Lighting2House(lighting2UK, houseId), unitCode);
    }

    public void ensureLighting2AC(int houseId, byte unitCode, boolean on) {
        if(!knownLighting2AC.containsEntry(houseId, unitCode)) {
            try {
                String name = "Lighting2 AC " + houseId + "/" + (int)unitCode;
                Lighting2ACAppliance appliance = lighting2ACFactory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                appliance.setHouseId(houseId);
                appliance.setUnitCode(unitCode);
                annotationProcessor.process(getRealRoot().getTypes(), appliance);
                if(on)
                    appliance.setOn();
                else
                    appliance.setOff();
                getRealRoot().addDevice(appliance);
            } catch (Throwable t) {
                getLog().e("Failed to auto-create Lighting2 AC device " + houseId + "/" + (int)unitCode);
            }
        }
    }

    public Lighting2Appliance makeHomeEasyApplianceEU(int houseId, byte unitCode) {
        knownLighting2HomeEasyEU.put(houseId, unitCode);
        return new Lighting2Appliance(new Lighting2House(lighting2HomeEasyEU, houseId), unitCode);
    }

    public void ensureHomeEasyApplianceEU(int houseId, byte unitCode, boolean on) {
        if(!knownLighting2HomeEasyEU.containsEntry(houseId, unitCode)) {
            try {
                String name = "Lighting2 HomeEasy EU " + houseId + "/" + (int)unitCode;
                Lighting2HomeEasyEUAppliance appliance = lighting2HomeEasyEUFactory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                appliance.setHouseId(houseId);
                appliance.setUnitCode(unitCode);
                annotationProcessor.process(getRealRoot().getTypes(), appliance);
                if(on)
                    appliance.setOn();
                else
                    appliance.setOff();
                getRealRoot().addDevice(appliance);
            } catch (Throwable t) {
                getLog().e("Failed to auto-create Lighting2 HomeEasy EU device " + houseId + "/" + (int)unitCode);
            }
        }
    }

    public TemperatureSensor makeTemperature1(int sensorId) {
        knownTemperature1.add(sensorId);
        return new TemperatureSensor(temperature1, sensorId);
    }

    public void ensureTemperature1(int sensorId, double temperature) {
        if(!knownTemperature1.contains(sensorId)) {
            try {
                String name = "Temperature 1 " + sensorId;
                Temperature1Sensor sensor = temperature1Factory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                sensor.setSensorId(sensorId);
                annotationProcessor.process(getRealRoot().getTypes(), sensor);
                sensor.deviceValues.setTemperature(temperature);
                getRealRoot().addDevice(sensor);
            } catch (Throwable t) {
                getLog().e("Failed to auto-create Temperature1 device " + sensorId);
            }
        }
    }

    public TemperatureSensor makeTemperature2(int sensorId) {
        knownTemperature2.add(sensorId);
        return new TemperatureSensor(temperature2, sensorId);
    }

    public void ensureTemperature2(int sensorId, double temperature) {
        if(!knownTemperature2.contains(sensorId)) {
            try {
                String name = "Temperature 2 " + sensorId;
                Temperature2Sensor sensor = temperature2Factory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                sensor.setSensorId(sensorId);
                annotationProcessor.process(getRealRoot().getTypes(), sensor);
                sensor.deviceValues.setTemperature(temperature);
                getRealRoot().addDevice(sensor);
            } catch (Throwable t) {
                getLog().e("Failed to auto-create Temperature2 device " + sensorId);
            }
        }
    }

    public TemperatureSensor makeTemperature3(int sensorId) {
        knownTemperature3.add(sensorId);
        return new TemperatureSensor(temperature3, sensorId);
    }

    public void ensureTemperature3(int sensorId, double temperature) {
        if(!knownTemperature3.contains(sensorId)) {
            try {
                String name = "Temperature 3 " + sensorId;
                Temperature3Sensor sensor = temperature3Factory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                sensor.setSensorId(sensorId);
                annotationProcessor.process(getRealRoot().getTypes(), sensor);
                sensor.deviceValues.setTemperature(temperature);
                getRealRoot().addDevice(sensor);
            } catch (Throwable t) {
                getLog().e("Failed to auto-create Temperature3 device " + sensorId);
            }
        }
    }

    public TemperatureSensor makeTemperature4(int sensorId) {
        knownTemperature4.add(sensorId);
        return new TemperatureSensor(temperature4, sensorId);
    }

    public void ensureTemperature4(int sensorId, double temperature) {
        if(!knownTemperature4.contains(sensorId)) {
            try {
                String name = "Temperature 4 " + sensorId;
                Temperature4Sensor sensor = temperature4Factory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                sensor.setSensorId(sensorId);
                annotationProcessor.process(getRealRoot().getTypes(), sensor);
                sensor.deviceValues.setTemperature(temperature);
                getRealRoot().addDevice(sensor);
            } catch (Throwable t) {
                getLog().e("Failed to auto-create Temperature4 device " + sensorId);
            }
        }
    }

    public TemperatureSensor makeTemperature5(int sensorId) {
        knownTemperature5.add(sensorId);
        return new TemperatureSensor(temperature5, sensorId);
    }

    public void ensureTemperature5(int sensorId, double temperature) {
        if(!knownTemperature5.contains(sensorId)) {
            try {
                String name = "Temperature 5 " + sensorId;
                Temperature5Sensor sensor = temperature5Factory.create(new DeviceData(UUID.randomUUID().toString(), name, name), getRealRoot());
                sensor.setSensorId(sensorId);
                annotationProcessor.process(getRealRoot().getTypes(), sensor);
                sensor.deviceValues.setTemperature(temperature);
                getRealRoot().addDevice(sensor);
            } catch (Throwable t) {
                getLog().e("Failed to auto-create Temperature5 device " + sensorId);
            }
        }
    }

    private class CallbackLighting1ARC implements Lighting1.Callback {

        @Override
        public void turnedOn(byte houseId, byte unitCode) {
            ensureLighting1ARC(houseId, unitCode, true);
        }

        @Override
        public void turnedOnAll(byte houseId) {

        }

        @Override
        public void turnedOff(byte houseId, byte unitCode) {
            ensureLighting1ARC(houseId, unitCode, false);
        }

        @Override
        public void turnedOffAll(byte houseId) {

        }

        @Override
        public void dim(byte houseId, byte unitCode) {
            // dimmable devices not supported yet
        }

        @Override
        public void bright(byte houseId, byte level) {
            // dimmable devices not supported yet
        }

        @Override
        public void chime(byte houseId) {
            // chime devices not supported yet
        }
    }

    private class CallbackLighting2AC implements Lighting2.Callback {

        @Override
        public void turnedOn(int houseId, byte unitCode) {
            ensureLighting2AC(houseId, unitCode, true);
        }

        @Override
        public void turnedOnAll(int houseId) {

        }

        @Override
        public void turnedOff(int houseId, byte unitCode) {
            ensureLighting2AC(houseId, unitCode, false);
        }

        @Override
        public void turnedOffAll(int houseId) {

        }

        @Override
        public void setLevel(int houseId, byte unitCode, byte level) {
            ensureLighting2AC(houseId, unitCode, level != 0 && level != 0x0F);
        }

        @Override
        public void setLevelAll(int houseId, byte level) {

        }
    }

    private class CallbackLighting2HomeEasyEU implements Lighting2.Callback {

        @Override
        public void turnedOn(int houseId, byte unitCode) {
            ensureHomeEasyApplianceEU(houseId, unitCode, true);
        }

        @Override
        public void turnedOnAll(int houseId) {

        }

        @Override
        public void turnedOff(int houseId, byte unitCode) {
            ensureHomeEasyApplianceEU(houseId, unitCode, false);
        }

        @Override
        public void turnedOffAll(int houseId) {

        }

        @Override
        public void setLevel(int houseId, byte unitCode, byte level) {
            ensureHomeEasyApplianceEU(houseId, unitCode, level != 0 && level != 0x0F);
        }

        @Override
        public void setLevelAll(int houseId, byte level) {

        }
    }

    private class CallbackTemperature1 implements TemperatureSensors.Callback {

        @Override
        public void newTemperature(int sensorId, double temperature) {
            ensureTemperature1(sensorId, temperature);
        }
    }

    private class CallbackTemperature2 implements TemperatureSensors.Callback {

        @Override
        public void newTemperature(int sensorId, double temperature) {
            ensureTemperature2(sensorId, temperature);
        }
    }

    private class CallbackTemperature3 implements TemperatureSensors.Callback {

        @Override
        public void newTemperature(int sensorId, double temperature) {
            ensureTemperature3(sensorId, temperature);
        }
    }

    private class CallbackTemperature4 implements TemperatureSensors.Callback {

        @Override
        public void newTemperature(int sensorId, double temperature) {
            ensureTemperature4(sensorId, temperature);
        }
    }

    private class CallbackTemperature5 implements TemperatureSensors.Callback {

        @Override
        public void newTemperature(int sensorId, double temperature) {
            ensureTemperature5(sensorId, temperature);
        }
    }
}
