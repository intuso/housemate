package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;
import com.intuso.housemate.client.v1_0.real.api.annotations.Values;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.log.Log;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

import java.io.*;

/**
 */

@TypeInfo(id = "arduino-temp-sensor", name = "Arduino Temperature Sensor", description = "Arduino Temperature Sensor")
public class ArduinoTemperatureSensor implements DeviceDriver {

    private final Log log;
    private final SerialPortWrapper serialPort;
    private final SerialPortEventListener eventListener = new EventListener();
    private PipedInputStream input;
    private PipedOutputStream output;
    private BufferedReader in;
    private LineReader lineReader;

    @Values
    private TemperatureValues temperatureValues;

    @Inject
    protected ArduinoTemperatureSensor(Log log,
                                       SerialPortWrapper serialPort,
                                       @Assisted DeviceDriver.Callback callback) {
        this.log = log;
        this.serialPort = serialPort;
    }

    @Override
    public void start() {
        try {
            serialPort.addEventListener(eventListener, SerialPort.MASK_RXCHAR);
            input = new PipedInputStream();
            output = new PipedOutputStream(input);
            in = new BufferedReader(new InputStreamReader(input));
        } catch(IOException e) {
            throw new HousemateCommsException("Failed to set up Arduino connection for reading data", e);
        }
        lineReader = new LineReader();
        lineReader.start();
    }

    @Override
    public void stop() {
        try {
            serialPort.removeEventListener();
        } catch(IOException e) {
            log.e("Failed to remove serial port event listener", e);
        }
        if(lineReader != null) {
            lineReader.interrupt();
            try {
                lineReader.join();
            } catch(InterruptedException e) {
                log.e("Interrupted waiting for reader to finish", e);
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
                log.e("Failed to read data from Arduino", e);
            }
        }
    }

    private class LineReader extends Thread {
        @Override
        public void run() {
            try {
                String line;
                while(!isInterrupted() && (line = in.readLine()) != null) {
                    log.d("Read line");
                    try {
                        temperatureValues.setTemperature(Double.parseDouble(line));
                        log.d("Set temperature");
                    } catch(NumberFormatException e) {
                        log.w("Could not parse temperature value \"" + line + "\"");
                    }
                }
            } catch(IOException e) {
                log.e("Error reading temperature from Arduino", e);
            }
            try {
                in.close();
            } catch(IOException e) {
                log.e("Failed to close connection to the Arduino", e);
            }
        }
    }

    private interface TemperatureValues {

        @Value(id = "temperature", name = "Temperature", description = "Current temperature", typeId = "double")
        void setTemperature(double temperature);
    }
}
