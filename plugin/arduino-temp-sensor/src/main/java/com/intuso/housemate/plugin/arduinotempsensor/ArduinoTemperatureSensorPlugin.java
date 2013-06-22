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
import com.intuso.utilities.log.Log;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.util.ArrayList;
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
        List<CommPortIdentifier> portIds = listSuitablePorts(resources.getLog());
        CommPortIdentifier portId = null;
        for(CommPortIdentifier pi : portIds) {
            resources.getLog().d("Found comm port id " + pi.getName());
            if(pi.getName().equals("/dev/ttyACM0")) {
                resources.getLog().d("Found required comm port id");
                portId = pi;
                break;
            }
        }
        if(portId == null)
            throw new HousemateException("No suitable portId found for Arduino Temperature Sensor devices, shutting down");
        try {
            serialPort = openPort(portId, resources.getLog());
        } catch(HousemateException e) {
            throw new HousemateException("Could not open serial port to Arduino");
        }
    }

    /**
     * Open a serial port
     */
    private SerialPort openPort(CommPortIdentifier portId, Log log) throws HousemateException {

        // Open the port
        try {
            log.d("Attempting to open serial port " + portId.getName());
            SerialPort port = (SerialPort)portId.open(ArduinoTemperatureSensorPlugin.class.getName(), 9600);
            port.setInputBufferSize(0);
            port.setOutputBufferSize(0);
            port.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            log.d("Successfully opened serial port");
            return port;
        } catch (PortInUseException e) {
            throw new HousemateException("Serial port is already in use. Is the service already running elsewhere?", e);
        } catch (UnsupportedCommOperationException e) {
            throw new HousemateException("Couldn't set serial port parameters", e);
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

    /**
     * Get a list of descriptions of potential serial ports
     * @return a list of descriptions of potential serial ports
     */
    public static List<CommPortIdentifier> listSuitablePorts(Log log) {

        List<CommPortIdentifier> suitablePorts = new ArrayList<CommPortIdentifier>();

        // Get a list of suitable ports;
        @SuppressWarnings("unchecked")
        java.util.Enumeration<CommPortIdentifier> commPorts = CommPortIdentifier.getPortIdentifiers();
        while(commPorts.hasMoreElements())
        {
            CommPortIdentifier commPortId = commPorts.nextElement();
            log.d("Found comm port " + commPortId.getName());
            if(commPortId.getPortType() != CommPortIdentifier.PORT_SERIAL)
                log.d("Comm port is not serial type");
            else if(commPortId.isCurrentlyOwned())
                log.d("Comm port is already owned");
            else
                suitablePorts.add(commPortId);
        }

        return suitablePorts;
    }
}
