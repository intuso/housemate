package com.intuso.housemate.extension.runprogram.ioc;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.plugin.AnnotatedPlugin;
import com.intuso.housemate.client.v1_0.api.plugin.FeatureDrivers;
import com.intuso.housemate.extension.runprogram.RunProgramFeatureDriver;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 08/01/14
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
@Id(value = "com.intuso.housemate.plugin.run-program", name = "Run Program Plugin", description = "Plugin for devices that run command line commands")
@FeatureDrivers(RunProgramFeatureDriver.class)
public class RunProgramPlugin extends AnnotatedPlugin {}