package com.intuso.housemate.plugin.tvremote.ioc;

import com.google.inject.Inject;
import com.intuso.housemate.plugin.tvremote.TVRemote;
import com.intuso.housemate.plugin.v1_0.api.AnnotatedPluginModule;
import com.intuso.housemate.plugin.v1_0.api.DeviceDrivers;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.log.Log;

@TypeInfo(id = "com.intuso.housemate.plugin.tv-remote", name = "TV Remote Plugin", description = "Plugin for TV remotes using lirc")
@DeviceDrivers(TVRemote.class)
public class TVRemotePluginModule extends AnnotatedPluginModule {
    @Inject
    public TVRemotePluginModule(Log log) {
        super(log);
    }
}
