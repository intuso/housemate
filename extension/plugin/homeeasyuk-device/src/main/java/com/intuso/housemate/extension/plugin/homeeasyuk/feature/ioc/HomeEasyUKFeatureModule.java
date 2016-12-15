package com.intuso.housemate.extension.plugin.homeeasyuk.feature.ioc;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.real.api.annotations.Id;
import com.intuso.housemate.client.v1_0.real.api.module.AnnotatedPluginModule;
import com.intuso.housemate.client.v1_0.real.api.module.FeatureDrivers;
import com.intuso.housemate.extension.plugin.homeeasyuk.feature.HomeEasyUKFeature;
import com.intuso.housemate.extension.plugin.homeeasyuk.feature.HomeEasyUKHardwareProxy;

@Id(value = "com.intuso.housemate.extension.plugin.homeeasyuk.feature", name = "HomeEasy UK feature plugin", description = "Plugin for HomeEasy UK featues")
@FeatureDrivers(HomeEasyUKFeature.class)
public class HomeEasyUKFeatureModule extends AnnotatedPluginModule {

    @Override
    public void configure() {
        super.configure();
        install(new FactoryModuleBuilder().build(HomeEasyUKHardwareProxy.Factory.class));
    }
}
