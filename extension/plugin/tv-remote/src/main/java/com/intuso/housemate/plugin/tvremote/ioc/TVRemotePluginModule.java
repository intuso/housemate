package com.intuso.housemate.plugin.tvremote.ioc;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.plugin.AnnotatedPluginModule;
import com.intuso.housemate.client.v1_0.api.plugin.FeatureDrivers;
import com.intuso.housemate.plugin.tvremote.TVRemote;

@Id(value = "com.intuso.housemate.plugin.tv-remote", name = "TV Remote Plugin", description = "Plugin for TV remotes using lirc")
@FeatureDrivers(TVRemote.class)
public class TVRemotePluginModule extends AnnotatedPluginModule {}
