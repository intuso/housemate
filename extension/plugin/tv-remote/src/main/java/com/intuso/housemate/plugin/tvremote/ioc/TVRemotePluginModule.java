package com.intuso.housemate.plugin.tvremote.ioc;

import com.google.inject.Inject;
import com.intuso.housemate.plugin.tvremote.TVRemote;
import com.intuso.housemate.plugin.v1_0.api.Devices;
import com.intuso.housemate.plugin.v1_0.api.PluginModule;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.log.Log;

@TypeInfo(id = "com.intuso.housemate.plugin.tv-remote", name = "TV Remote Plugin", description = "Plugin for TV remotes using lirc")
@Devices(TVRemote.class)
public class TVRemotePluginModule extends PluginModule {
    @Inject
    public TVRemotePluginModule(Log log) {
        super(log);
    }
}
