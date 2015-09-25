package com.intuso.housemate.plugin.tvremote;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.device.feature.RealPlaybackControl;
import com.intuso.housemate.client.v1_0.real.api.device.feature.RealPowerControl;
import com.intuso.housemate.client.v1_0.real.api.device.feature.RealVolumeControl;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.io.IOException;

@TypeInfo(id = "tv-remote", name = "TV Remote", description = "TV Remote")
public class TVRemote extends RealDevice implements RealPowerControl, RealPlaybackControl, RealVolumeControl {

    @Property(id = "remote-name", name = "Remote Name", description = "The name of the remote you want to use", typeId = "string")
    public String remoteName;

    @Inject
    public TVRemote(Log log,
                    ListenersFactory listenersFactory,
                    @Assisted DeviceData data) {
        super(log, listenersFactory, "tv-remote", data);
        getData().setCustomPropertyIds(Lists.newArrayList("remote-name"));
    }

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
            throw new HousemateCommsException("Failed to perform TV remote command", e);
        }
    }
}
