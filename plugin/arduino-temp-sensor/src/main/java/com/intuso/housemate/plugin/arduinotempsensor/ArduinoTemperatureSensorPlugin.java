package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;
import com.intuso.housemate.plugin.api.BrokerConsequenceFactory;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/03/13
 * Time: 11:32
 * To change this template use File | Settings | File Templates.
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
            if(!serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE))
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
    public List<RealDeviceFactory<?>> getDeviceFactories() {
        return Lists.<RealDeviceFactory<?>>newArrayList(
                new ArduinoTemperatureSensorFactory(serialPort),
                new ArduinoIndicatorFactory(serialPort));
    }

    @Override
    public List<BrokerConditionFactory<?>> getConditionFactories() {
        return Lists.newArrayList();
    }

    @Override
    public List<BrokerConsequenceFactory<?>> getConsequenceFactories() {
        return Lists.newArrayList();
    }
}
