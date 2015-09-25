package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.impl.device.StatefulPoweredDevice;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.io.IOException;

/**
 */
@TypeInfo(id = "arduino-indicator", name = "Arduino Indicator", description = "Arduino Indicator")
public class ArduinoIndicator extends StatefulPoweredDevice {

    private final SerialPortWrapper serialPort;

    @Property(id = "colour", name = "Colour", description = "Colour of the indicator", typeId = "string")
    public String colour;

    @Property(id = "intensity", name = "Intensity", description = "Intensity of the indicator", typeId = "integer")
    public int intensity;

    @Inject
    protected ArduinoIndicator(Log log,
                               ListenersFactory listenersFactory,
                               @Assisted DeviceData data,
                               SerialPortWrapper serialPort) {
        super(log, listenersFactory, "arduino-indicator", data);
        this.serialPort = serialPort;
        getData().setCustomPropertyIds(Lists.newArrayList("colour", "intensity"));
    }

    @Override
    public void turnOn() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], (byte) ('0' + intensity)});
            setOn();
        } catch(IOException e) {
            getLog().w("Failed to send command to turn light on");
        }
    }

    @Override
    public void turnOff() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], '0'});
            setOff();
        } catch(IOException e) {
            getLog().w("Failed to send command to turn light off");
        }
    }
}
