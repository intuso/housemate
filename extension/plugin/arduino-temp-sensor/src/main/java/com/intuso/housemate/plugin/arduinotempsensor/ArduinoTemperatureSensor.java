package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.feature.TemperatureSensor;
import com.intuso.utilities.listener.MemberRegistration;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import org.slf4j.Logger;

import java.io.*;

/**
 */

@Id(value = "arduino-temp-sensor", name = "Arduino Temperature Sensor", description = "Arduino Temperature Sensor")
public class ArduinoTemperatureSensor implements FeatureDriver, TemperatureSensor {

    private Logger logger;
    private final ManagedCollection<Listener> listeners;
    private final SerialPortWrapper serialPort;
    private final SerialPortEventListener eventListener = new EventListener();

    private PipedInputStream input;
    private PipedOutputStream output;
    private BufferedReader in;
    private LineReader lineReader;

    private double temperature = 0.0;

    @Inject
    protected ArduinoTemperatureSensor(SerialPortWrapper serialPort,
                                       ManagedCollectionFactory managedCollectionFactory) {
        this.serialPort = serialPort;
        this.listeners = managedCollectionFactory.create();
    }

    @Override
    public void init(Logger logger, FeatureDriver.Callback callback) {
        this.logger = logger;
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
    public void uninit() {
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
        this.logger = null;
    }

    @Override
    public double getTemperature() {
        return temperature;
    }

    @Override
    public MemberRegistration addListener(Listener listener) {
        return listeners.add(listener);
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
                        // assign to temp value in case it changes again very quickly
                        double t = Double.parseDouble(line);
                        temperature = t;
                        for(Listener listener : listeners)
                            listener.temperature(t);
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
}
