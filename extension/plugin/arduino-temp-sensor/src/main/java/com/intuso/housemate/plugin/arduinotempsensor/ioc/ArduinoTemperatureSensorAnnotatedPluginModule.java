package com.intuso.housemate.plugin.arduinotempsensor.ioc;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.plugin.arduinotempsensor.ArduinoIndicator;
import com.intuso.housemate.plugin.arduinotempsensor.ArduinoTemperatureSensor;
import com.intuso.housemate.plugin.arduinotempsensor.SerialPortWrapper;
import com.intuso.housemate.plugin.v1_0.api.AnnotatedPluginModule;
import com.intuso.housemate.plugin.v1_0.api.Devices;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.log.Log;
import jssc.SerialPort;

import java.util.List;
import java.util.regex.Pattern;

@TypeInfo(id = "com.intuso.housemate.plugin.arduino-temp-sensor", name = "Arduino Temperature Sensor plugin", description = "Plugin for temperature sensing using an Arduino")
@Devices({ArduinoTemperatureSensor.class,
        ArduinoIndicator.class})
public class ArduinoTemperatureSensorAnnotatedPluginModule extends AnnotatedPluginModule {

    private final static List<Pattern> PATTERNS = Lists.newArrayList(
            Pattern.compile(".*ttyACM.*")
    );

    @Inject
    public ArduinoTemperatureSensorAnnotatedPluginModule(Log log) {
        super(log);
    }

    @Provides
    @Singleton
    public SerialPortWrapper getSerialPortWrapper(Log log) {
        log.d("Initialising Arduino Temperature Sensor plugin");
        return new SerialPortWrapper(log, PATTERNS,
                SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, // params
                SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT); // flow control mode
    }
}
