package com.intuso.housemate.plugin.tvremote;

import com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor;
import com.intuso.housemate.annotations.plugin.Devices;
import com.intuso.housemate.annotations.plugin.PluginInformation;

@PluginInformation(id = "com.intuso.housemate.plugin.tvremote.TVRemotePlugin",
        name = "TV Remote plugin",
        description = "Plugin for TV remotes using lirc",
        author = "Intuso")
@Devices({TVRemote.class})
public class TVRemotePlugin extends AnnotatedPluginDescriptor {}
