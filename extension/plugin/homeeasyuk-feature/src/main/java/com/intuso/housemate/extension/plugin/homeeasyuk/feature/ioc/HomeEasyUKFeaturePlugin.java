package com.intuso.housemate.extension.plugin.homeeasyuk.feature.ioc;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.plugin.AnnotatedPlugin;
import com.intuso.housemate.client.v1_0.api.plugin.FeatureDrivers;

@Id(value = "com.intuso.housemate.extension.plugin.homeeasyuk.feature", name = "HomeEasy UK feature plugin", description = "Plugin for HomeEasy UK featues")
@FeatureDrivers(com.intuso.housemate.extension.plugin.homeeasyuk.feature.HomeEasyUKFeature.class)
public class HomeEasyUKFeaturePlugin extends AnnotatedPlugin {}
