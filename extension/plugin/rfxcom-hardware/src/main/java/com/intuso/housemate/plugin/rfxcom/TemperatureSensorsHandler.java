package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.ability.Temperature;
import com.intuso.housemate.client.v1_0.api.annotation.Component;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
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

    private final Map<Integer, Device> sensors = Maps.newHashMap();

    protected TemperatureSensorsHandler(ManagedCollectionFactory managedCollectionFactory,
                                        RFXtrx rfxtrx,
                                        com.rfxcom.rfxtrx.message.TemperatureSensors.SubType subType,
                                        String idPrefix,
                                        String idFormat,
                                        String nameFormat,
                                        String descriptionFormat) {
        super(idPrefix + "-");
        this.managedCollectionFactory = managedCollectionFactory;
        this.temperatureSensors = new TemperatureSensors(rfxtrx, subType);
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
        Device sensor = getOrCreate(sensorId);
        if(sensor != null)
            sensor.setTemperature(temperature);
    }

    @Override
    void parseIdDetails(String details) {
        addSensor(Integer.parseInt(details));
    }

    private Device getOrCreate(int sensorId) {
        if(sensors.containsKey(sensorId))
            return sensors.get(sensorId);
        else if(autoCreate)
            return addSensor(sensorId);
        return null;
    }

    public Device addSensor(int sensorId) {
        Device sensor = new Device(managedCollectionFactory);
        sensors.put(sensorId, sensor);
        hardwareCallback.addDevice(
                idFormat.replaceAll("\\$\\{sensorId\\}", Integer.toString(sensorId)),
                nameFormat.replaceAll("\\$\\{sensorId\\}", Integer.toString(sensorId)),
                descriptionFormat.replaceAll("\\$\\{sensorId\\}", Integer.toString(sensorId)),
                sensor);
        return sensor;
    }

    public void removeSensor(int sensorId) {
        if(sensors.containsKey(sensorId))
            hardwareCallback.removeDevice(sensors.remove(sensorId));
    }

    public static class TEMP1 extends TemperatureSensorsHandler {
        @Inject
        public TEMP1(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP1,
                    "temperaturesensor-1",
                    "${sensorId}",
                    "Temperature Sensor 1-${sensorId}",
                    "Temperature Sensor 1-${sensorId}");
        }
    }

    public static class TEMP2 extends TemperatureSensorsHandler {

        @Inject
        public TEMP2(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP2,
                    "temperaturesensor-2",
                    "${sensorId}",
                    "Temperature Sensor 2-${sensorId}",
                    "Temperature Sensor 2-${sensorId}");
        }
    }

    public static class TEMP3 extends TemperatureSensorsHandler {

        @Inject
        public TEMP3(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP3,
                    "temperaturesensor-3",
                    "${sensorId}",
                    "Temperature Sensor 3-${sensorId}",
                    "Temperature Sensor 3-${sensorId}");
        }
    }

    public static class TEMP4 extends TemperatureSensorsHandler {

        @Inject
        public TEMP4(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP4,
                    "temperaturesensor-4",
                    "${sensorId}",
                    "Temperature Sensor 4-${sensorId}",
                    "Temperature Sensor 4-${sensorId}");
        }
    }

    public static class TEMP5 extends TemperatureSensorsHandler {

        @Inject
        public TEMP5(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.TemperatureSensors.SubType.TEMP5,
                    "temperaturesensor-5",
                    "${sensorId}",
                    "Temperature Sensor 5-${sensorId}",
                    "Temperature Sensor 5-${sensorId}");
        }
    }

    public class Device {

        @Component
        @Id(value = "temperature", name = "Temperature", description = "Temperature")
        private final TemperatureSensorImpl temperatureSensor;

        public Device(ManagedCollectionFactory managedCollectionFactory) {
            this.temperatureSensor = new TemperatureSensorImpl(managedCollectionFactory);
        }

        public void setTemperature(double temperature) {
            temperatureSensor.setTemperature(temperature);
        }

        public class TemperatureSensorImpl implements Temperature.State {

            private final ManagedCollection<Listener> listeners;

            private Double temperature;

            protected TemperatureSensorImpl(ManagedCollectionFactory managedCollectionFactory) {
                this.listeners = managedCollectionFactory.createSet();
            }

            @Override
            public synchronized ManagedCollection.Registration addListener(Listener listener) {
                listener.temperature(temperature);
                return listeners.add(listener);
            }

            public synchronized void setTemperature(double temperature) {
                this.temperature = temperature;
                for (Listener listener : listeners)
                    listener.temperature(temperature);
            }
        }
    }
}
