package com.intuso.housemate.device;

import com.google.inject.Inject;
import com.intuso.housemate.annotations.plugin.AnnotatedPluginModule;
import com.intuso.housemate.annotations.plugin.Devices;
import com.intuso.housemate.annotations.plugin.PluginInformation;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 08/01/14
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
@PluginInformation(id = "com.intuso.housemate.plugin.run-program", name = "Run Program Plugin", description = "Plugin for devices that run command line commands")
@Devices(RunProgramDevice.class)
public class RunProgramPluginModule extends AnnotatedPluginModule {

    @Inject
    public RunProgramPluginModule(Log log) {
        super(log);
    }
}
