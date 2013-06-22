package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.annotations.basic.Property;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.device.OnOffDevice;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.StringType;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/05/12
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public class ArduinoIndicator extends OnOffDevice {

    private final SerialPort serialPort;
    private OutputStream os;

    @Property(id = "colour", name = "Colour", description = "Colour of the indicator", type = StringType.class)
    public String colour;

    @Property(id = "intensity", name = "Intensity", description = "Intensity of the indicator", type = IntegerType.class)
    public int intensity;
    
    /**
     * Create a new device
     *
     * @param name the name of the device
     */
    protected ArduinoIndicator(RealResources resources, SerialPort serialPort, String id, String name, String description) {
        super(resources, id, name, description);
        this.serialPort = serialPort;
    }

    @Override
    protected void start() throws HousemateException {
        try {
            os = serialPort.getOutputStream();
        } catch(IOException e) {
            throw new HousemateException("Failed to open connection to Arduino", e);
        }
    }

    @Override
    protected void stop() {
        try {
            os.close();
        } catch(IOException e) {
            getLog().e("Failed to close connection to Arduino");
            getLog().st(e);
        }
    }

    @Override
    protected void turnOn() {
        try {
            os.write(new byte[] {colour.getBytes()[0], (byte)('0' + intensity)});
        } catch(IOException e) {
            getLog().w("Failed to send command to turn light on");
        }
    }

    @Override
    protected void turnOff() {
        try {
            os.write(new byte[] {colour.getBytes()[0], '0'});
        } catch(IOException e) {
            getLog().w("Failed to send command to turn light off");
        }
    }
}
