package com.intuso.housemate.webserver.database.inmemory.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.webserver.database.Database;
import com.intuso.housemate.webserver.database.inmemory.InMemoryDatabase;

/**
 * Created by tomc on 21/01/17.
 */
public class InMemoryDatabaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Database.class).to(InMemoryDatabase.class);
        bind(InMemoryDatabase.class).in(Scopes.SINGLETON);
    }
}
