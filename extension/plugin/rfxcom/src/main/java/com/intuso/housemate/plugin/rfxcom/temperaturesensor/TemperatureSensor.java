package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;
import com.intuso.housemate.client.v1_0.real.api.annotations.Values;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Housemate device that receives temperature updates from a sensor
 *
 */
public abstract class TemperatureSensor implements DeviceDriver {

	private com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor sensor;
    private ListenerRegistration listenerRegistration;
    private int sensorId = 0;

    @Values
    public DeviceValues deviceValues;

    private final RealDevice device;

	public TemperatureSensor(RealDevice device) {
		this.device = device;
	}

    public void propertyChanged() {
        // check the port value is a positive number
        if(sensorId < 0 || sensorId > 0xFFFF) {
            device.getErrorValue().setTypedValues("Id must be between 0 and " + 0xFFFF);
            return;
        }

        device.getErrorValue().setTypedValues((String) null);

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
	public void start() {
		propertyChanged();
	}

	@Override
	public void stop() {
		sensor = null;
	}

    public interface DeviceValues {

        @Value(id = "temperature", name = "Temperature", description = "The current temperature", typeId = "double")
        void setTemperature(double temperature);
    }
}
