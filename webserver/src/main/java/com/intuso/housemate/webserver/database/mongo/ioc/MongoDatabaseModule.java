package com.intuso.housemate.webserver.database.mongo.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.webserver.database.Database;
import com.intuso.housemate.webserver.database.mongo.MongoDatabaseImpl;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created by tomc on 21/01/17.
 */
public class MongoDatabaseModule extends AbstractModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        MongoDatabaseImpl.configureDefaults(defaultProperties);
    }

    @Override
    protected void configure() {
        bind(Database.class).to(MongoDatabaseImpl.class);
        bind(MongoDatabaseImpl.class).in(Scopes.SINGLETON);
    }
}
