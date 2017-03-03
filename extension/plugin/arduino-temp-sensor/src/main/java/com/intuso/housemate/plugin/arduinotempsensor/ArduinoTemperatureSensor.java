package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.ability.TemperatureSensor;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import org.slf4j.Logger;

import java.io.*;

/**
 */

@Id(value = "arduino-temp-sensor", name = "Arduino Temperature Sensor", description = "Arduino Temperature Sensor")
public class ArduinoTemperatureSensor implements TemperatureSensor {

    private final Logger logger;
    private final ManagedCollection<Listener> listeners;
    private final SerialPortWrapper serialPort;
    private final SerialPortEventListener eventListener = new EventListener();

    private final PipedInputStream input;
    private final PipedOutputStream output;
    private final BufferedReader in;
    private final LineReader lineReader;

    private Double temperature = null;

    @Inject
    protected ArduinoTemperatureSensor(Logger logger, ManagedCollectionFactory managedCollectionFactory, SerialPortWrapper serialPort) {
        this.logger = logger;
        this.listeners = managedCollectionFactory.create();
        this.serialPort = serialPort;
        try {
            serialPort.addEventListener(eventListener, SerialPort.MASK_RXCHAR);
            input = new PipedInputStream();
            output = new PipedOutputStream(input);
            in = new BufferedReader(new InputStreamReader(input));
        } catch(IOException e) {
            throw new HardwareDriver.HardwareException("Failed to set up Arduino connection for reading data", e);
        }
        lineReader = new LineReader();
        lineReader.start();
    }

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
        }
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        listener.temperature(temperature);
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
