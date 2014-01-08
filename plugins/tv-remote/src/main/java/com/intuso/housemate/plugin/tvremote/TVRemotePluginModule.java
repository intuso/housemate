package com.intuso.housemate.plugin.tvremote;

import com.google.inject.Inject;
import com.intuso.housemate.annotations.plugin.AnnotatedPluginModule;
import com.intuso.housemate.annotations.plugin.Devices;
import com.intuso.housemate.annotations.plugin.PluginInformation;
import com.intuso.utilities.log.Log;

@PluginInformation(id = "com.intuso.housemate.plugin.tv-remote", name = "TV Remote Plugin", description = "Plugin for TV remotes using lirc")
@Devices(TVRemote.class)
public class TVRemotePluginModule extends AnnotatedPluginModule {
    @Inject
    public TVRemotePluginModule(Log log) {
        super(log);
    }
}
