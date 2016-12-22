package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Housemate device that receives temperature updates from a sensor
 *
 */
public abstract class TemperatureSensor implements FeatureDriver, com.intuso.housemate.client.v1_0.api.feature.TemperatureSensor {

	private com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor sensor;
    private ListenerRegistration listenerRegistration;
    private int sensorId = 0;

    public Listener listener;

    private final TemperatureHandler handler;
    private final FeatureDriver.Callback driverCallback;

	public TemperatureSensor(TemperatureHandler handler, FeatureDriver.Callback driverCallback) {
        this.handler = handler;
		this.driverCallback = driverCallback;
	}

    public void propertyChanged() {
        // check the port value is a positive number
        if(sensorId < 0 || sensorId > 0xFFFF) {
            driverCallback.setError("Id must be between 0 and " + 0xFFFF);
            return;
        }

        driverCallback.setError(null);

        if(listenerRegistration != null) {
            listenerRegistration.removeListener();
            listenerRegistration = null;
        }
        sensor = createSensor(sensorId);
        listenerRegistration = sensor.addCallback(new com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor.Callback() {

            @Override
            public void newTemperature(double temperature) {
                listener.temperature(temperature);
            }
        });
	}

    public abstract com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId);

    public int getSensorId() {
        return sensorId;
    }

    @Property("integer")
    @Id(value = "sensor-id", name = "Sensor ID", description = "Sensor ID (in decimal)")
    public void setSensorId(int sensorId) {
        handler.propertiesChanged(this.sensorId, sensorId);
        this.sensorId = sensorId;
        propertyChanged();
    }

    @Override
    public double getTemperature() {
        return 0; // todo
    }

    @Override
    public ListenerRegistration addListener(Listener listener) {
        return null; // todo
    }

    @Override
	public void startFeature() {
		propertyChanged();
	}

	@Override
	public void stopFeature() {
		sensor = null;
	}

}
