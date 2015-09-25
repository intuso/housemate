package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;
import com.intuso.housemate.client.v1_0.real.api.annotations.Values;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Housemate device that receives temperature updates from a sensor
 *
 */
public abstract class TemperatureSensor extends RealDevice {

	private com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor sensor;
    private ListenerRegistration listenerRegistration;
    private int sensorId = 0;

    @Values
    public DeviceValues deviceValues;

	public TemperatureSensor(Log log,
                             ListenersFactory listenersFactory,
                             String type,
                             DeviceData data) {
		super(log, listenersFactory, type, data);
        getData().setCustomPropertyIds(Lists.newArrayList("sensor-id"));
        getData().setCustomValueIds(Lists.newArrayList("temperature"));
	}

    public void propertyChanged() {
        // check the port value is a positive number
        if(sensorId < 0 || sensorId > 0xFFFF) {
            getErrorValue().setTypedValues("Id must be between 0 and " + 0xFFFF);
            return;
        }

        getErrorValue().setTypedValues((String) null);

        if(listenerRegistration != null) {
            listenerRegistration.removeListener();
            listenerRegistration = null;
        }
        sensor = createSensor(sensorId);
        listenerRegistration = sensor.addCallback(new com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor.Callback() {

            @Override
            public void newTemperature(double temperature) {
                deviceValues.setTemperature(temperature);
            }
        });
	}

    public abstract com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId);

    public int getSensorId() {
        return sensorId;
    }

    @Property(id = "sensor-id", name = "Sensor ID", description = "Sensor ID (in decimal)", typeId = "integer")
    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
        propertyChanged();
    }
	
	@Override
	protected void start() {
		propertyChanged();
	}

	@Override
	protected void stop() {
		sensor = null;
	}

    public interface DeviceValues {

        @Value(id = "temperature", name = "Temperature", description = "The current temperature", typeId = "double")
        void setTemperature(double temperature);
    }
}
