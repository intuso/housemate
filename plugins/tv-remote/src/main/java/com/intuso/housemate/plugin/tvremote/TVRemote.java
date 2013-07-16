package com.intuso.housemate.plugin.tvremote;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.basic.Property;
import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;

import java.io.IOException;

@FactoryInformation(id = "tv-remote", name = "TV Remote", description = "TV Remote")
public class TVRemote extends RealDevice {

    @Property(id = "remote-name", name = "Remote Name", description = "The name of the remote you want to use", typeId = "string")
    public String remoteName;

    public TVRemote(RealResources resources, String id, String name, String description) {
        super(resources, id, name, description);
    }

    @Command(id = "power", name = "Power", description = "Turn the TV on or off")
    public void power() throws HousemateException {
        irSend("KEY_POWER");
    }

    @Command(id = "volume-up", name = "Volume Up", description = "Volume up")
    public void volumeUp() throws HousemateException {
        irSend("KEY_VOLUMEUP");
    }

    @Command(id = "volume-down", name = "Volume Down", description = "Volume down")
    public void volumeDown() throws HousemateException {
        irSend("KEY_VOLUMEDOWN");
    }

    @Command(id = "play", name = "Play", description = "Play")
    public void play() throws HousemateException {
        irSend("KEY_PLAY");
    }

    @Command(id = "pause", name = "Pause", description = "Pause")
    public void pause() throws HousemateException {
        irSend("KEY_PAUSE");
    }

    @Command(id = "Rewind", name = "Rewind", description = "Rewind")
    public void rewind() throws HousemateException {
        irSend("KEY_REWIND");
    }

    @Command(id = "forward", name = "Forward", description = "Forward")
    public void forward() throws HousemateException {
        irSend("KEY_FORWARD");
    }

    private void irSend(String buttonName) throws HousemateException {
        try {
            Runtime.getRuntime().exec("irsend SEND_ONCE " + remoteName + " " + buttonName);
        } catch(IOException e) {
            throw new HousemateException("Failed to perform TV remote command", e);
        }
    }
}
