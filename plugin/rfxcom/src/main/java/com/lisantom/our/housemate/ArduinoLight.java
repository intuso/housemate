package com.lisantom.our.housemate;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.device.OnOffDevice;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/05/12
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public class ArduinoLight extends OnOffDevice {
    
    OutputStream os;
    byte lightColour;
    
    /**
     * Create a new device
     *
     * @param name the name of the device
     */
    protected ArduinoLight(RealResources resources, OutputStream os, String id, String name, String description, byte lightColour) throws HousemateException {
        super(resources, id, name, description);
        this.os = os;
        this.lightColour = lightColour;
    }
    
    @Override
    protected void turnOn() {
        try {
            os.write(new byte[] {lightColour, '1'});
        } catch(IOException e) {
            getLog().w("Failed to send command to turn light on");
        }
    }

    @Override
    protected void turnOff() {
        try {
            os.write(new byte[] {lightColour, '0'});
        } catch(IOException e) {
            getLog().w("Failed to send command to turn light off");
        }
    }
}
