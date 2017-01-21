package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.google.common.collect.Sets;
import com.intuso.housemate.plugin.rfxcom.old.Handler;
import com.intuso.utilities.listener.ListenerRegistration;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Created by tomc on 03/12/15.
 */
public abstract class TemperatureHandler implements Handler {

    private final Logger logger;
    private final TemperatureSensors temperatureSensors;
    private ListenerRegistration messageListenerRegistration;
    private final CallbackImpl callback = new CallbackImpl();
    private final Set<Integer> knownSensors = Sets.newHashSet();

    public TemperatureHandler(Logger logger, TemperatureSensors temperatureSensors) {
        this.logger = logger;
        this.temperatureSensors = temperatureSensors;
    }

    @Override
    public void listen(boolean listen) {
        if(!listen && messageListenerRegistration != null) {
            messageListenerRegistration.removeListener();
            messageListenerRegistration = null;
        } else if(listen && messageListenerRegistration == null)
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
                // todo
                /*final RealDevice<TemperatureSensor> device = deviceContainer.createAndAddDevice(new Device.Data(UUID.randomUUID().toString(), name, name));
                device.getDriverProperty().set(new Type.Instances(new Type.Instance(getDriverId())), new Command.PerformListener<RealCommand>() {
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
                });*/
            } catch (Throwable t) {
                logger.error("Failed to auto-create Temperature device " + sensorId, t);
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
