package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.HousemateException;

@Id("playback-control")
public interface RealPlaybackControl extends RealFeature {
    @Command(id = "play", name = "Play", description = "Play")
    void play() throws HousemateException;
    @Command(id = "pause", name = "Pause", description = "Pause")
    void pause() throws HousemateException;
    @Command(id = "stop", name = "Stop", description = "Stop")
    void stopPlayback() throws HousemateException;
    @Command(id = "forward", name = "Forward", description = "Forward")
    void forward() throws HousemateException;
    @Command(id = "rewind", name = "Rewind", description = "Rewind")
    void rewind() throws HousemateException;
}
