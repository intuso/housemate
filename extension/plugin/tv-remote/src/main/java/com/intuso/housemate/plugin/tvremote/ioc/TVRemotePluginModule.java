package com.intuso.housemate.plugin.tvremote.ioc;

import com.intuso.housemate.plugin.tvremote.TVRemote;
import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.module.AnnotatedPluginModule;
import com.intuso.housemate.plugin.v1_0.api.module.FeatureDrivers;

@Id(value = "com.intuso.housemate.plugin.tv-remote", name = "TV Remote Plugin", description = "Plugin for TV remotes using lirc")
@FeatureDrivers(TVRemote.class)
public class TVRemotePluginModule extends AnnotatedPluginModule {}
