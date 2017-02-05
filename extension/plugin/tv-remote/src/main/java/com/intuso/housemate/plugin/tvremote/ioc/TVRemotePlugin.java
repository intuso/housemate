package com.intuso.housemate.plugin.tvremote.ioc;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.plugin.AnnotatedPlugin;
import com.intuso.housemate.plugin.tvremote.TVRemote;

@Id(value = "com.intuso.housemate.plugin.tv-remote", name = "TV Remote Plugin", description = "Plugin for TV remotes using lirc")
public class TVRemotePlugin extends AnnotatedPlugin {}
