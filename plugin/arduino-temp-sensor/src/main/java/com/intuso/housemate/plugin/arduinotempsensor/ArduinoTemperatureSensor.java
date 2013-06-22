package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    private Reader reader = null;
    
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
            in = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            reader = new Reader();
            reader.start();
        } catch(IOException e) {
            throw new HousemateException("Failed to open connection to Arduino");
        }
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

    private class Reader extends Thread {
        @Override
        public void run() {
            try {
                String line;
                while(!isInterrupted() && (line = in.readLine()) != null) {
                    try {
                        temperature.setTypedValue(Double.parseDouble(line));
                    } catch(NumberFormatException e) {
                        getLog().w("Could not parse temperature value \"" + line + "\"");
                    }
                }
            } catch(IOException e) {
                getLog().e("Error reading temperature from Arduino");
            }
        }
    }
}
