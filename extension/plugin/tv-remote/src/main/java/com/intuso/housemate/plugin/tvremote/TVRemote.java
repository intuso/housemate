package com.intuso.housemate.plugin.tvremote;

import com.intuso.housemate.client.v1_0.api.ability.Playback;
import com.intuso.housemate.client.v1_0.api.ability.Power;
import com.intuso.housemate.client.v1_0.api.ability.Volume;
import com.intuso.housemate.client.v1_0.api.annotation.Classes;
import com.intuso.housemate.client.v1_0.api.annotation.Component;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;

import java.io.IOException;

@Id(value = "tv-remote", name = "TV Remote", description = "TV Remote")
public class TVRemote {

    @Component
    @Id(value = "playback", name = "Playback", description = "Playback")
    public final PlaybackComponent playbackComponent = new PlaybackComponent();

    @Component
    @Id(value = "power", name = "Power", description = "Power")
    public final PowerComponent powerComponent = new PowerComponent();

    @Component
    @Id(value = "volume", name = "Volume", description = "Volume")
    public final VolumeComponent volumeComponent = new VolumeComponent();

    @Property
    @Id(value = "remote-name", name = "Remote Name", description = "The name of the remote you want to use")
    public String remoteName;

    private void irSend(String buttonName) {
        try {
            Runtime.getRuntime().exec("irsend SEND_ONCE " + remoteName + " " + buttonName);
        } catch(IOException e) {
            throw new HardwareDriver.HardwareException("Failed to perform TV remote command", e);
        }
    }

    @Classes(Classes.TV)
    private final class PlaybackComponent implements Playback.Control {

        @Override
        public synchronized void play() {
            irSend("KEY_PLAY");
        }

        @Override
        public synchronized void pause() {
            irSend("KEY_PAUSE");
        }

        @Override
        public synchronized void stopPlayback() {
            irSend("KEY_STOP");
        }

        @Override
        public synchronized void rewind() {
            irSend("KEY_REWIND");
        }

        @Override
        public synchronized void forward() {
            irSend("KEY_FORWARD");
        }
    }

    @Classes(Classes.TV)
    private final class PowerComponent implements Power.Control {

        @Override
        public synchronized void turnOn() {
            irSend("KEY_POWER");
        }

        @Override
        public synchronized void turnOff() {
            irSend("KEY_POWER");
        }
    }

    @Classes(Classes.TV)
    private final class VolumeComponent implements Volume.Control {

        @Override
        public synchronized void mute() {
            irSend("KEY_MUTE");
        }

        @Override
        public synchronized void unmute() {
            irSend("KEY_MUTE");
        }

        @Override
        public synchronized void volume(int volume) {
            throw new UnsupportedOperationException("Infra-red remote cannot support setting the volume. Use volume up/down instead");
        }

        @Override
        public synchronized void volumeUp() {
            irSend("KEY_VOLUMEUP");
        }

        @Override
        public synchronized void volumeDown() {
            irSend("KEY_VOLUMEDOWN");
        }
    }
}
