package com.intuso.housemate.plugin.tvremote;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.v1_0.api.annotations.Property;
import com.intuso.housemate.plugin.v1_0.api.annotations.TypeInfo;
import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;
import com.intuso.housemate.plugin.v1_0.api.feature.PlaybackControl;
import com.intuso.housemate.plugin.v1_0.api.feature.PowerControl;
import com.intuso.housemate.plugin.v1_0.api.feature.VolumeControl;
import org.slf4j.Logger;

import java.io.IOException;

@TypeInfo(id = "tv-remote", name = "TV Remote", description = "TV Remote")
public class TVRemote implements DeviceDriver, PowerControl, PlaybackControl, VolumeControl {

    // todo use remote IR sender

    @Property("string")
    @TypeInfo(id = "remote-name", name = "Remote Name", description = "The name of the remote you want to use")
    public String remoteName;

    @Inject
    public TVRemote(@Assisted Logger logger, @Assisted DeviceDriver.Callback driverCallback) {}

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public void turnOn() {
        irSend("KEY_POWER");
    }

    @Override
    public void turnOff() {
        irSend("KEY_POWER");
    }

    @Override
    public void play() {
        irSend("KEY_PLAY");
    }

    @Override
    public void pause() {
        irSend("KEY_PAUSE");
    }

    @Override
    public void stopPlayback() {
        irSend("KEY_STOP");
    }

    @Override
    public void rewind() {
        irSend("KEY_REWIND");
    }

    @Override
    public void forward() {
        irSend("KEY_FORWARD");
    }

    @Override
    public void mute() {
        irSend("KEY_MUTE");
    }

    @Override
    public void volumeUp() {
        irSend("KEY_VOLUMEUP");
    }

    @Override
    public void volumeDown() {
        irSend("KEY_VOLUMEDOWN");
    }

    private void irSend(String buttonName) {
        try {
            Runtime.getRuntime().exec("irsend SEND_ONCE " + remoteName + " " + buttonName);
        } catch(IOException e) {
            throw new DeviceException("Failed to perform TV remote command", e);
        }
    }
}
