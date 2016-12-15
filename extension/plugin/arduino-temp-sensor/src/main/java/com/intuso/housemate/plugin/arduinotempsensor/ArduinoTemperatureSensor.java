package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.Id;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;
import com.intuso.housemate.client.v1_0.real.api.annotations.Values;
import com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import org.slf4j.Logger;

import java.io.*;

/**
 */

@Id(value = "arduino-temp-sensor", name = "Arduino Temperature Sensor", description = "Arduino Temperature Sensor")
public class ArduinoTemperatureSensor implements FeatureDriver {

    private final Logger logger;
    private final SerialPortWrapper serialPort;
    private final SerialPortEventListener eventListener = new EventListener();
    private PipedInputStream input;
    private PipedOutputStream output;
    private BufferedReader in;
    private LineReader lineReader;

    private TemperatureValues temperatureValues;

    @Inject
    protected ArduinoTemperatureSensor(SerialPortWrapper serialPort,
                                       @Assisted Logger logger,
                                       @Assisted FeatureDriver.Callback callback) {
        this.logger = logger;
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
            throw new FeatureException("Failed to set up Arduino connection for reading data", e);
        }
        lineReader = new LineReader();
        lineReader.start();
    }

    @Override
    public void stop() {
        try {
            serialPort.removeEventListener();
        } catch(IOException e) {
            logger.error("Failed to remove serial port event listener", e);
        }
        if(lineReader != null) {
            lineReader.interrupt();
            try {
                lineReader.join();
            } catch(InterruptedException e) {
                logger.error("Interrupted waiting for reader to finish", e);
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
                logger.error("Failed to read data from Arduino", e);
            }
        }
    }

    private class LineReader extends Thread {
        @Override
        public void run() {
            try {
                String line;
                while(!isInterrupted() && (line = in.readLine()) != null) {
                    logger.debug("Read line");
                    try {
                        temperatureValues.setTemperature(Double.parseDouble(line));
                        logger.debug("Set temperature");
                    } catch(NumberFormatException e) {
                        logger.warn("Could not parse temperature value \"" + line + "\"");
                    }
                }
            } catch(IOException e) {
                logger.error("Error reading temperature from Arduino", e);
            }
            try {
                in.close();
            } catch(IOException e) {
                logger.error("Failed to close connection to the Arduino", e);
            }
        }
    }

    @Values
    private interface TemperatureValues {

        @Value("double")
        @Id(value = "temperature", name = "Temperature", description = "Current temperature")
        void setTemperature(double temperature);
    }
}
