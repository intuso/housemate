package com.intuso.housemate.platform.pc.ioc;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import javax.jms.Connection;
import java.io.File;
import java.util.Set;

import static com.intuso.housemate.platform.pc.Properties.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/01/14
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class PCClientModule extends AbstractModule {

    private final PropertyRepository properties;

    public PCClientModule(WriteableMapPropertyRepository defaultProperties, PropertyRepository properties) {
        this.properties = properties;

        // set the defaults
        defaultProperties.set(HOUSEMATE_CONFIG_DIR, System.getProperty("user.home") + File.separator + ".housemate");
        defaultProperties.set(HOUSEMATE_PROPS_FILE, "housemate.props");
        defaultProperties.set(APPLICATION_CONFIG_DIR, "./");
        defaultProperties.set(APPLICATION_PROPS_FILE, "housemate.props");
        defaultProperties.set(ConnectionProvider.HOST, "localhost");
        defaultProperties.set(ConnectionProvider.PORT, "46873");
    }

    @Override
    protected void configure() {
        install(new PCModule());
        bind(PropertyRepository.class).toInstance(properties);
        bind(Connection.class).toProvider(ConnectionProvider.class).in(Scopes.SINGLETON);

        // create the set binder. Prevents a guice error when getting set of services
        Multibinder.newSetBinder(binder(), Service.class);
    }

    @Provides
    public ServiceManager getServiceManager(Set<Service> services) {
        return new ServiceManager(services);
    }
}
