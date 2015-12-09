package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import jssc.SerialPort;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by tomc on 07/11/14.
 */
public class SerialPortWrapper {

    private final Logger logger;
    private final List<Pattern> patterns;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;
    private final int flowControlMode;
    private SerialPort serialPort;
    private SerialPortEventListener eventListener;
    private int maskRxchar;

    public SerialPortWrapper(Logger logger, List<Pattern> patterns, int baudRate, int dataBits, int stopBits, int parity, int flowControlMode) {
        this.logger = logger;
        this.patterns = patterns;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.flowControlMode = flowControlMode;
    }

    private synchronized void openPort() throws IOException {

        if(serialPort != null)
            return;

        outer: for(Pattern pattern : patterns) {
            logger.debug("Looking for comm ports matching " + pattern);
            Set<String> pns = Sets.newHashSet(SerialPortList.getPortNames(pattern));
            if (pns.size() > 0) {
                logger.debug("Found comm ports " + Joiner.on(",").join(pns));
                for(String pn : pns) {
                    logger.debug("Trying " + pn);
                    try {
                        openPort(pn);
                        break outer;
                    } catch(Throwable t) {
                        logger.warn("Failed to open " + pn);
                    }
                }
            }
        }
        if(serialPort == null)
            throw new IOException("No ports available");
    }

    private void openPort(String portName) throws IOException {
        try {
            if (portName == null)
                throw new IOException("No port name set");

            logger.debug("Attempting to open serial port " + portName);
            serialPort = new SerialPort(portName);
            serialPort.openPort();
            serialPort.setParams(baudRate, dataBits, stopBits, parity);
            serialPort.setFlowControlMode(flowControlMode);
            serialPort.addEventListener(eventListener, maskRxchar);
            logger.debug("Successfully opened serial port");
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    public final synchronized void closePort() {

        if(serialPort != null) {
            try {
                serialPort.removeEventListener();
            } catch (SerialPortException e) {
                // do nothing, closing down anyway
            }
            try {
                serialPort.closePort();
            } catch (SerialPortException e) {
                // do nothing, closing down anyway
            }
            serialPort = null;
        }
    }

    public void writeBytes(byte[] bytes) throws IOException {
        openPort();
        try {
            serialPort.writeBytes(bytes);
        } catch (SerialPortException e) {
            closePort();
            openPort();
            try {
                serialPort.writeBytes(bytes);
            } catch (SerialPortException e2) {
                throw new IOException(e);
            }
        }
    }

    public int getInputBufferBytesCount() throws IOException {
        openPort();
        try {
            return serialPort.getInputBufferBytesCount();
        } catch (SerialPortException e) {
            closePort();
            openPort();
            try {
                return serialPort.getInputBufferBytesCount();
            } catch (SerialPortException e2) {
                throw new IOException(e);
            }
        }
    }

    public byte[] readBytes(int available) throws IOException {
        openPort();
        try {
            return serialPort.readBytes(available);
        } catch (SerialPortException e) {
            closePort();
            openPort();
            try {
                return serialPort.readBytes(available);
            } catch (SerialPortException e2) {
                throw new IOException(e);
            }
        }
    }

    public void addEventListener(SerialPortEventListener eventListener, int maskRxchar) throws IOException {
        this.eventListener = eventListener;
        this.maskRxchar = maskRxchar;
        if(serialPort != null)
            try {
                serialPort.addEventListener(eventListener, maskRxchar);
            } catch (SerialPortException e) {
                throw new IOException(e);
            }
    }

    public void removeEventListener() throws IOException {
        eventListener = null;
        if(serialPort != null)
            try {
                serialPort.removeEventListener();
            } catch (SerialPortException e) {
                throw new IOException(e);
            }
    }
}
