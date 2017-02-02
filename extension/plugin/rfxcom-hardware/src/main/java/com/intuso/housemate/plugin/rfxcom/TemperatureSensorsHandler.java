package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;

import java.util.Map;

/**
 * Created by tomc on 02/02/17.
 */
public class TemperatureSensorsHandler extends Handler implements TemperatureSensors.Callback {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final TemperatureSensors temperatureSensors;

    private final String idFormat, nameFormat, descriptionFormat;

    private final Map<Integer, TemperatureSensorImpl> sensors = Maps.newHashMap();

    protected TemperatureSensorsHandler(ManagedCollectionFactory managedCollectionFactory,
                                        RFXtrx rfXtrx,
                                        com.rfxcom.rfxtrx.message.TemperatureSensors.SubType subType,
                                        String idPrefix,
                                        String idFormat,
                                        String nameFormat,
                                        String descriptionFormat) {
        super(idPrefix + "-");
        this.managedCollectionFactory = managedCollectionFactory;
        this.temperatureSensors = new TemperatureSensors(rfXtrx, subType);
        this.idFormat = idPrefix + "-" + idFormat;
        this.nameFormat = nameFormat;
        this.descriptionFormat = descriptionFormat;
    }

    @Override
    public ManagedCollection.Registration initListener() {
        return temperatureSensors.addCallback(this);
    }

    @Override
    public void newTemperature(int sensorId, double temperature) {
        TemperatureSensorImpl sensor = getOrCreate(sensorId);
        if(sensor != null)
            sensor.setTemperature(temperature);
    }

    @Override
    void parseIdDetails(String details) {
        addSensor(Integer.parseInt(details));
    }

    private TemperatureSensorImpl getOrCreate(int sensorId) {
        if(sensors.containsKey(sensorId))
            return sensors.get(sensorId);
        else if(autoCreate)
            return addSensor(sensorId);
        return null;
    }

    public TemperatureSensorImpl addSensor(int sensorId) {
        TemperatureSensorImpl sensor = new TemperatureSensorImpl(managedCollectionFactory);
        sensors.put(sensorId, sensor);
        hardwareCallback.addConnectedDevice(
                idFormat.replaceAll("\\$\\{sensorId\\}", Integer.toString(sensorId)),
                nameFormat.replaceAll("\\$\\{sensorId\\}", Integer.toString(sensorId)),
                descriptionFormat.replaceAll("\\$\\{sensorId\\}", Integer.toString(sensorId)),
                sensor);
        return sensor;
    }

    public void removeSensor(int sensorId) {
        if(sensors.containsKey(sensorId))
            hardwareCallback.removeConnectedDevice(sensors.remove(sensorId));
    }

    public static class TEMP1 extends TemperatureSensorsHandler {
        @Inject
        public TEMP1(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP1,
                    "temperaturesensor-1",
                    "${sensorId}",
                    "Temperature Sensor 1-${sensorId}",
                    "Temperature Sensor 1-${sensorId}");
        }
    }

    public static class TEMP2 extends TemperatureSensorsHandler {

        @Inject
        public TEMP2(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP2,
                    "temperaturesensor-2",
                    "${sensorId}",
                    "Temperature Sensor 2-${sensorId}",
                    "Temperature Sensor 2-${sensorId}");
        }
    }

    public static class TEMP3 extends TemperatureSensorsHandler {

        @Inject
        public TEMP3(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP3,
                    "temperaturesensor-3",
                    "${sensorId}",
                    "Temperature Sensor 3-${sensorId}",
                    "Temperature Sensor 3-${sensorId}");
        }
    }

    public static class TEMP4 extends TemperatureSensorsHandler {

        @Inject
        public TEMP4(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP4,
                    "temperaturesensor-4",
                    "${sensorId}",
                    "Temperature Sensor 4-${sensorId}",
                    "Temperature Sensor 4-${sensorId}");
        }
    }

    public static class TEMP5 extends TemperatureSensorsHandler {

        @Inject
        public TEMP5(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP5,
                    "temperaturesensor-5",
                    "${sensorId}",
                    "Temperature Sensor 5-${sensorId}",
                    "Temperature Sensor 5-${sensorId}");
        }
    }
}
