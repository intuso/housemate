package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.google.common.collect.Sets;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.plugin.rfxcom.Handler;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;

import java.util.Set;
import java.util.UUID;

/**
 * Created by tomc on 03/12/15.
 */
public abstract class TemperatureHandler implements Handler {

    private final Log log;
    private final RealDevice.Container deviceContainer;
    private final TemperatureSensors temperatureSensors;
    private ListenerRegistration messageListenerRegistration;
    private final CallbackImpl callback = new CallbackImpl();
    private final Set<Integer> knownSensors = Sets.newHashSet();

    public TemperatureHandler(Log log, TemperatureSensors temperatureSensors, RealDevice.Container deviceContainer) {
        this.log = log;
        this.deviceContainer = deviceContainer;
        this.temperatureSensors = temperatureSensors;
    }

    @Override
    public void listen(boolean listen) {
        if(messageListenerRegistration != null) {
            messageListenerRegistration.removeListener();
            messageListenerRegistration = null;
        }
        if(listen)
            messageListenerRegistration = temperatureSensors.addCallback(callback);
    }

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor makeSensor(int sensorId) {
        knownSensors.add(sensorId);
        return new com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor(temperatureSensors, sensorId);
    }

    public void propertiesChanged(int oldSensorId, int newSensorId) {
        knownSensors.remove(oldSensorId);
        knownSensors.add(newSensorId);
    }

    public void messageReceived(final int sensorId, final double temperature) {
        if(!knownSensors.contains(sensorId)) {
            try {
                String name = getNewDeviceName(sensorId);
                final RealDevice<TemperatureSensor> device = deviceContainer.createAndAddDevice(new DeviceData(UUID.randomUUID().toString(), name, name));
                device.getDriverProperty().set(new TypeInstances(new TypeInstance(getDriverId())), new Command.PerformListener<RealCommand>() {
                    @Override
                    public void commandStarted(RealCommand command) {

                    }

                    @Override
                    public void commandFinished(RealCommand command) {
                        if(device.isDriverLoaded()) {
                            TemperatureSensor sensor = device.getDriver();
                            sensor.setSensorId(sensorId);
                            sensor.temperatureValues.setTemperature(temperature);
                        }
                    }

                    @Override
                    public void commandFailed(RealCommand command, String error) {

                    }
                });
            } catch (Throwable t) {
                log.e("Failed to auto-create Temperature device " + sensorId, t);
            }
        }
    }

    public abstract String getNewDeviceName(int sensorId);
    public abstract String getDriverId();

    private class CallbackImpl implements TemperatureSensors.Callback {

        @Override
        public void newTemperature(int sensorId, double temperature) {
            messageReceived(sensorId, temperature);
        }
    }
}
