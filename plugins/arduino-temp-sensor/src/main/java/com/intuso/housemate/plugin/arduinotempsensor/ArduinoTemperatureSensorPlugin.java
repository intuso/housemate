package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.plugin.api.PluginModule;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.log.Log;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 */
public class ArduinoTemperatureSensorPlugin extends PluginModule {

    @Override
    public TypeInfo getTypeInfo() {
        return new TypeInfo(ArduinoTemperatureSensorPlugin.class.getName(), "Arduino Temp Sensor plugin",
                "Plugin for temperature sensing using an Arduino");
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

    @Override
    public void configureDeviceFactories(Multibinder<RealDeviceFactory<?>> deviceFactoryBindings) {
        deviceFactoryBindings.addBinding().to(ArduinoTemperatureSensorFactory.class);
        deviceFactoryBindings.addBinding().to(ArduinoIndicatorFactory.class);
    }
}
