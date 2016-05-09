package com.intuso.housemate.server;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        install(new FactoryModuleBuilder().build(new Key<DeviceDriver.Factory<TestDeviceDriver>>() {}));
    }

    @Provides
    @Singleton
    public Logger getLogger() {
        return LoggerFactory.getLogger(TestModule.class);
    }
}
