package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/05/12
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public class ArduinoTemperatureSensor extends RealDevice {
    
    public final RealValue<Double> temperature = DoubleType.createValue(getResources(), "temperature", "Temperature", "The current temperature", 0.0);
    
    private final SerialPort serialPort;
    private BufferedReader in;
    private LineReader reader = null;
    
    /**
     * Create a new device
     *
     * @param name the name of the device
     */
    protected ArduinoTemperatureSensor(RealResources resources, SerialPort serialPort, String id, String name, String description) throws HousemateException {
        super(resources, id, name, description);
        getValues().add(temperature);
        this.serialPort = serialPort;
    }

    @Override
    protected void start() throws HousemateException {
        in = new BufferedReader(new SerialPortReader());
        reader = new LineReader();
        reader.start();
    }

    @Override
    protected void stop() {
        if(reader != null) {
            reader.interrupt();
            try {
                reader.join();
            } catch(InterruptedException e) {
                getLog().e("Interrupted waiting for reader to finish");
            }
            reader = null;
        }
    }

    private class LineReader extends Thread {
        @Override
        public void run() {
            try {
                String line;
                while(!isInterrupted() && (line = in.readLine()) != null) {
                    getLog().d("Read line");
                    try {
                        temperature.setTypedValue(Double.parseDouble(line));
                        getLog().d("Set temperature");
                    } catch(NumberFormatException e) {
                        getLog().w("Could not parse temperature value \"" + line + "\"");
                    }
                }
            } catch(IOException e) {
                getLog().e("Error reading temperature from Arduino");
            }
            try {
                in.close();
            } catch(IOException e) {
                getLog().e("Failed to close connection to the Arduino");
            }
        }
    }

    private class SerialPortReader extends Reader {
        @Override
        public int read(char[] chars, int offset, int length) throws IOException {
            try {
                chars[offset] = (char)serialPort.readBytes(1)[0];
                return 1;
            } catch(SerialPortException e) {
                throw new IOException("Failed to read from serial port");
            }
        }

        @Override
        public void close() throws IOException {
            try {
                serialPort.closePort();
            } catch(SerialPortException e) {
                throw new IOException("Failed to close serial port", e);
            }
        }
    }
}
