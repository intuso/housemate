package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.intuso.housemate.annotations.plugin.AnnotatedPluginModule;
import com.intuso.housemate.annotations.plugin.DeviceFactories;
import com.intuso.housemate.annotations.plugin.PluginInformation;
import com.intuso.housemate.api.HousemateException;
import com.intuso.utilities.log.Log;
import jssc.SerialPort;
import jssc.SerialPortException;

@PluginInformation(id = "com.intuso.housemate.plugin.arduino-temp-sensor", name = "Arduino Temperature Sensor plugin", description = "Plugin for temperature sensing using an Arduino")
@DeviceFactories({ArduinoTemperatureSensorFactory.class, ArduinoIndicatorFactory.class})
public class ArduinoTemperatureSensorPluginModule extends AnnotatedPluginModule {

    @Inject
    public ArduinoTemperatureSensorPluginModule(Log log) {
        super(log);
    }

    @Provides
    public SerialPort getSerialPort(Log log) throws HousemateException {
        log.d("Initialising Arduino Temperature Sensor plugin");
        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        try {
            if(!serialPort.openPort())
                throw new HousemateException("No suitable port found for Arduino Temperature Sensor devices");
            if(!serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE))
                throw new HousemateException("Failed to properly configure port for Arduino Temperature Sensor devices");
        } catch(SerialPortException e) {
            throw new HousemateException("Failed to open connection to Arduino", e);
        }
        return serialPort;
    }
}
