package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.*;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.List;

/**
 */
public class ArduinoTemperatureSensorPlugin implements PluginDescriptor {

    private SerialPort serialPort;

    @Override
    public String getId() {
        return ArduinoTemperatureSensorPlugin.class.getName();
    }

    @Override
    public String getName() {
        return "Arduino Temp Sensor plugin";
    }

    @Override
    public String getDescription() {
        return "Plugin for temperature sensing using an Arduino";
    }

    @Override
    public String getAuthor() {
        return "Intuso";
    }

    @Override
    public void init(Resources resources) throws HousemateException {
        resources.getLog().d("Initialising Arduino Temperature Sensor plugin");
        serialPort = new SerialPort("/dev/ttyACM0");
        try {
            if(!serialPort.openPort())
                throw new HousemateException("No suitable port found for Arduino Temperature Sensor devices");
            if(!serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE))
                throw new HousemateException("Failed to properly configure port for Arduino Temperature Sensor devices");
        } catch(SerialPortException e) {
            throw new HousemateException("Failed to open connection to Arduino", e);
        }
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes(RealResources resources) {
        return Lists.newArrayList();
    }

    @Override
    public List<Comparator<?>> getComparators(RealResources resources) {
        return Lists.newArrayList();
    }

    @Override
    public List<Operator<?, ?>> getOperators(RealResources resources) {
        return Lists.newArrayList();
    }

    @Override
    public List<Transformer<?, ?>> getTransformers(RealResources resources) {
        return Lists.newArrayList();
    }

    @Override
    public List<RealDeviceFactory<?>> getDeviceFactories() {
        return Lists.<RealDeviceFactory<?>>newArrayList(
                new ArduinoTemperatureSensorFactory(serialPort),
                new ArduinoIndicatorFactory(serialPort));
    }

    @Override
    public List<ServerConditionFactory<?>> getConditionFactories() {
        return Lists.newArrayList();
    }

    @Override
    public List<ServerTaskFactory<?>> getTaskFactories() {
        return Lists.newArrayList();
    }
}
