package com.intuso.housemate.extension.runprogram.ioc;

import com.intuso.housemate.extension.runprogram.RunProgramFeatureDriver;
import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.module.AnnotatedPluginModule;
import com.intuso.housemate.plugin.v1_0.api.module.FeatureDrivers;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 08/01/14
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
@Id(value = "com.intuso.housemate.plugin.run-program", name = "Run Program Plugin", description = "Plugin for devices that run command line commands")
@FeatureDrivers(RunProgramFeatureDriver.class)
public class RunProgramPluginModule extends AnnotatedPluginModule {}
