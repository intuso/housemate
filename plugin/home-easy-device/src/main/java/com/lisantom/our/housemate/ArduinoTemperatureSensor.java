package com.lisantom.our.housemate;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.real.RealDevice;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.RealValue;
import com.intuso.housemate.real.impl.type.DoubleType;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/05/12
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public class ArduinoTemperatureSensor extends RealDevice implements SerialPortEventListener {
    
    public final RealValue<Double> temperature = DoubleType.createValue(getResources(), "temperature", "Temperature", "The current temperature", 0.0);
    
    private final SerialPort serialPort;
    private final InputStream in;
    
    /**
     * Create a new device
     *
     * @param name the name of the device
     */
    protected ArduinoTemperatureSensor(RealResources resources, SerialPort serialPort, String id, String name, String description) throws HousemateException {
        super(resources, id, name, description);
        getValues().add(temperature);
        this.serialPort = serialPort;
        try {
            this.in = serialPort.getInputStream();
        } catch(IOException e) {
            throw new HousemateException("Failed to get input stream from serial port");
        }
    }

    @Override
    protected void _start() throws HousemateException {
        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch(TooManyListenersException e) {
            throw new HousemateException("Could not listen for serial port events");
        }
    }

    @Override
    protected void _stop() {
        serialPort.removeEventListener();
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        try {
            byte[] data = new byte[in.available()];
            int len = in.read(data);
            String[] lines = new String(data, 0, len).split("\n");
            for(String line : lines) {
                try {
                    temperature.setTypedValue(Double.parseDouble(line));
                } catch(NumberFormatException e) {
                    getLog().w("Could not parse temperature value \"" + line + "\"");
                }
            }
        } catch(IOException e) {
            getErrorValue().setValue("Failed to read temperature from Arduino device");
            stop();
        }
    }
}
