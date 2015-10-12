package com.intuso.housemate.server;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.persistence.api.internal.NoPersistence;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.housemate.plugin.api.internal.ExternalClientRouter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.writer.StdOutWriter;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 27/03/14
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class TestModule extends AbstractModule {

    private final ListenersFactory listenersFactory;
    private final PropertyRepository defaultProperties;

    public TestModule(ListenersFactory listenersFactory, PropertyRepository defaultProperties) {
        this.listenersFactory = listenersFactory;
        this.defaultProperties = defaultProperties;
    }

    @Override
    protected void configure() {
        bind(ListenersFactory.class).toInstance(listenersFactory);
        bind(PropertyRepository.class).toInstance(WriteableMapPropertyRepository.newEmptyRepository(listenersFactory, defaultProperties));
        bind(Persistence.class).to(NoPersistence.class);
        install(new FactoryModuleBuilder().build(new Key<DeviceDriver.Factory<TestDeviceDriver>>() {}));
        Multibinder.newSetBinder(binder(), ExternalClientRouter.class);
    }

    @Provides
    @Singleton
    public Log getLog() {
        return new Log(new StdOutWriter(LogLevel.DEBUG));
    }
}
