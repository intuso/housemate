package com.intuso.housemate.server;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
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

    private final ManagedCollectionFactory managedCollectionFactory;
    private final PropertyRepository defaultProperties;

    public TestModule(ManagedCollectionFactory managedCollectionFactory, PropertyRepository defaultProperties) {
        this.managedCollectionFactory = managedCollectionFactory;
        this.defaultProperties = defaultProperties;
    }

    @Override
    protected void configure() {
        bind(ManagedCollectionFactory.class).toInstance(managedCollectionFactory);
        bind(PropertyRepository.class).toInstance(WriteableMapPropertyRepository.newEmptyRepository(managedCollectionFactory, defaultProperties));
        install(new FactoryModuleBuilder().build(new Key<FeatureDriver.Factory<TestFeatureDriver>>() {}));
    }

    @Provides
    @Singleton
    public Logger getLogger() {
        return LoggerFactory.getLogger(TestModule.class);
    }
}
