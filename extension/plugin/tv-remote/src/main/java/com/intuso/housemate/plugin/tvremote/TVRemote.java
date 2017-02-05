package com.intuso.housemate.plugin.tvremote;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.api.Playback;
import com.intuso.housemate.client.v1_0.api.api.Power;
import com.intuso.housemate.client.v1_0.api.api.Volume;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

import java.io.IOException;

@Id(value = "tv-remote", name = "TV Remote", description = "TV Remote")
public class TVRemote implements Power, Playback, Volume {

    private final ManagedCollection<Power.Listener> powerListeners;
    private final ManagedCollection<Playback.Listener> playbackListeners;
    private final ManagedCollection<Volume.Listener> volumeListeners;

    public TVRemote(ManagedCollectionFactory managedCollectionFactory) {
        this.powerListeners = managedCollectionFactory.create();
        this.playbackListeners = managedCollectionFactory.create();
        this.volumeListeners = managedCollectionFactory.create();
    }

    @Property
    @Id(value = "remote-name", name = "Remote Name", description = "The name of the remote you want to use")
    public String remoteName;

    @Override
    public ManagedCollection.Registration addListener(Power.Listener listener) {
        listener.on(null);
        return powerListeners.add(listener);
    }

    @Override
    public ManagedCollection.Registration addListener(Playback.Listener listener) {
        listener.speed(null);
        return playbackListeners.add(listener);
    }

    @Override
    public ManagedCollection.Registration addListener(Volume.Listener listener) {
        listener.muted(null);
        listener.volume(null);
        return volumeListeners.add(listener);
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
    public void unmute() {
        irSend("KEY_MUTE");
    }

    @Override
    public void volume(int volume) {
        throw new UnsupportedOperationException("Infra-red remote cannot support setting the volume. Use volume up/down instead");
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
            throw new HardwareDriver.HardwareException("Failed to perform TV remote command", e);
        }
    }
}
