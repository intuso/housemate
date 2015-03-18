package com.intuso.housemate.device.ioc;

import com.google.inject.Inject;
import com.intuso.housemate.device.RunProgramDevice;
import com.intuso.housemate.plugin.api.Devices;
import com.intuso.housemate.plugin.api.PluginModule;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 08/01/14
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
@TypeInfo(id = "com.intuso.housemate.plugin.run-program", name = "Run Program Plugin", description = "Plugin for devices that run command line commands")
@Devices(RunProgramDevice.class)
public class RunProgramPluginModule extends PluginModule {

    @Inject
    public RunProgramPluginModule(Log log) {
        super(log);
    }
}
