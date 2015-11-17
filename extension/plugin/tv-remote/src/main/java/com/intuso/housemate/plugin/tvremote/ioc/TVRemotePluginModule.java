package com.intuso.housemate.plugin.tvremote.ioc;

import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.plugin.tvremote.TVRemote;
import com.intuso.housemate.plugin.v1_0.api.AnnotatedPluginModule;
import com.intuso.housemate.plugin.v1_0.api.DeviceDrivers;

@TypeInfo(id = "com.intuso.housemate.plugin.tv-remote", name = "TV Remote Plugin", description = "Plugin for TV remotes using lirc")
@DeviceDrivers(TVRemote.class)
public class TVRemotePluginModule extends AnnotatedPluginModule {}
