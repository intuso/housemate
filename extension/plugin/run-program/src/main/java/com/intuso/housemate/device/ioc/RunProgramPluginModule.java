package com.intuso.housemate.device.ioc;

import com.intuso.housemate.device.RunProgramDeviceDriver;
import com.intuso.housemate.plugin.v1_0.api.AnnotatedPluginModule;
import com.intuso.housemate.plugin.v1_0.api.DeviceDrivers;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 08/01/14
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
@TypeInfo(id = "com.intuso.housemate.plugin.run-program", name = "Run Program Plugin", description = "Plugin for devices that run command line commands")
@DeviceDrivers(RunProgramDeviceDriver.class)
public class RunProgramPluginModule extends AnnotatedPluginModule {}
