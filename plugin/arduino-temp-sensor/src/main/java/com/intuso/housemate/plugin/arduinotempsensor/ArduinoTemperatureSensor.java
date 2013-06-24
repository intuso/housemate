package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 */
public class ArduinoTemperatureSensor extends RealDevice {
    
    public final RealValue<Double> temperature = DoubleType.createValue(getResources(), "temperature", "Temperature", "The current temperature", 0.0);
    
    private final SerialPort serialPort;
    private final SerialPortEventListener eventListener = new EventListener();
    private PipedInputStream input;
    private PipedOutputStream output;
    private BufferedReader in;
    private LineReader lineReader;

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
        try {
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(eventListener, SerialPort.MASK_RXCHAR);
            input = new PipedInputStream();
            output = new PipedOutputStream(input);
            in = new BufferedReader(new InputStreamReader(input));
        } catch(SerialPortException e) {
            throw new HousemateException("Failed to add listener for Arduino data", e);
        } catch(IOException e) {
            throw new HousemateException("Failed to set up read of Arduino connection data", e);
        }
        lineReader = new LineReader();
        lineReader.start();
    }

    @Override
    protected void stop() {
        try {
            serialPort.removeEventListener();
        } catch(SerialPortException e) {
            getLog().e("Failed to remove serial port event listener");
            getLog().st(e);
        }
        if(lineReader != null) {
            lineReader.interrupt();
            try {
                lineReader.join();
            } catch(InterruptedException e) {
                getLog().e("Interrupted waiting for reader to finish");
                getLog().st(e);
            }
            lineReader = null;
        }
    }

    private class EventListener implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            int available;
            try {
                while((available = serialPort.getInputBufferBytesCount()) > 0)
                    output.write(serialPort.readBytes(available));
            } catch(IOException e) {
                getLog().e("Failed to read data from Arduino");
                getLog().st(e);
            } catch(SerialPortException e) {
                getLog().e("Failed to read data from Arduino");
                getLog().st(e);
            }
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
                getLog().st(e);
            }
            try {
                in.close();
            } catch(IOException e) {
                getLog().e("Failed to close connection to the Arduino");
                getLog().st(e);
            }
        }
    }
}
