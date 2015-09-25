package com.intuso.housemate.persistence.flatfile.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.housemate.persistence.flatfile.FlatFilePersistence;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/11/13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class FlatFilePersistenceModule extends AbstractModule {

    public FlatFilePersistenceModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(FlatFilePersistence.PATH_PROPERTY_KEY, "./database");
    }

    @Override
    protected void configure() {
        bind(FlatFilePersistence.class).in(Scopes.SINGLETON);
        bind(Persistence.class).to(FlatFilePersistence.class);
    }
}
