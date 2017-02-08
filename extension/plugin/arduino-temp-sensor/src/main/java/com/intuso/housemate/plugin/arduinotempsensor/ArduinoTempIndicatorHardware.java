package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/02/17.
 */
public class ArduinoTempIndicatorHardware implements HardwareDriver {

    private final Injector injector;

    @Inject
    public ArduinoTempIndicatorHardware(Injector injector) {
        this.injector = injector;
    }

    @Override
    public void init(Logger logger, Callback callback) {
        // todo
    }

    @Override
    public void uninit() {
        // todo
    }

    @Override
    public void foundDeviceId(String deviceId) {
        // todo
    }
}
