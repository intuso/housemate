package com.intuso.housemate.plugin.tvremote;

import com.intuso.housemate.annotations.basic.Property;
import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.device.feature.RealPlaybackControl;
import com.intuso.housemate.object.real.device.feature.RealVolumeControl;
import com.intuso.housemate.object.real.impl.device.PoweredDevice;
import com.intuso.utilities.log.Log;

import java.io.IOException;

@FactoryInformation(id = "tv-remote", name = "TV Remote", description = "TV Remote")
public class TVRemote extends PoweredDevice implements RealPlaybackControl, RealVolumeControl {

    @Property(id = "remote-name", name = "Remote Name", description = "The name of the remote you want to use", typeId = "string")
    public String remoteName;

    public TVRemote(Log log, String id, String name, String description) {
        super(log, id, name, description);
        getCustomPropertyIds().add("remote-name");
    }

    @Override
    public void turnOn() throws HousemateException {
        irSend("KEY_POWER");
    }

    @Override
    public void turnOff() throws HousemateException {
        irSend("KEY_POWER");
    }

    @Override
    public void play() throws HousemateException {
        irSend("KEY_PLAY");
    }

    @Override
    public void pause() throws HousemateException {
        irSend("KEY_PAUSE");
    }

    @Override
    public void stopPlayback() throws HousemateException {
        irSend("KEY_STOP");
    }

    @Override
    public void rewind() throws HousemateException {
        irSend("KEY_REWIND");
    }

    @Override
    public void forward() throws HousemateException {
        irSend("KEY_FORWARD");
    }

    @Override
    public void mute() throws HousemateException {
        irSend("KEY_MUTE");
    }

    @Override
    public void volumeUp() throws HousemateException {
        irSend("KEY_VOLUMEUP");
    }

    @Override
    public void volumeDown() throws HousemateException {
        irSend("KEY_VOLUMEDOWN");
    }

    private void irSend(String buttonName) throws HousemateException {
        try {
            Runtime.getRuntime().exec("irsend SEND_ONCE " + remoteName + " " + buttonName);
        } catch(IOException e) {
            throw new HousemateException("Failed to perform TV remote command", e);
        }
    }
}
