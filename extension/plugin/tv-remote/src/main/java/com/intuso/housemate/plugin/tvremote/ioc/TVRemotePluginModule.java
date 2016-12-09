package com.intuso.housemate.plugin.tvremote.ioc;

import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.module.AnnotatedPluginModule;
import com.intuso.housemate.client.v1_0.real.api.module.FeatureDrivers;
import com.intuso.housemate.plugin.tvremote.TVRemote;

@TypeInfo(id = "com.intuso.housemate.plugin.tv-remote", name = "TV Remote Plugin", description = "Plugin for TV remotes using lirc")
@FeatureDrivers(TVRemote.class)
public class TVRemotePluginModule extends AnnotatedPluginModule {}
