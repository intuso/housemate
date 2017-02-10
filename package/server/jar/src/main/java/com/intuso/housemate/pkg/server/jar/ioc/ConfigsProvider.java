package com.intuso.housemate.pkg.server.jar.ioc;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import com.intuso.utilities.webserver.config.HttpPortConfig;
import com.intuso.utilities.webserver.config.PortConfig;

import java.util.Set;

/**
 * Created by tomc on 10/02/17.
 */
public class ConfigsProvider implements Provider<Set<PortConfig>> {

    public final static String HOST = "webserver.host";
    public final static String PORT = "webserver.port";

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(HOST, "0.0.0.0");
        defaultProperties.set(PORT, "4601");
    }

    @Inject
    public ConfigsProvider(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    private final PropertyRepository propertyRepository;

    @Override
    public Set<PortConfig> get() {
        return Sets.<PortConfig>newHashSet(
                new HttpPortConfig(propertyRepository.get(HOST), Integer.parseInt(propertyRepository.get(PORT)))
        );
    }
}
