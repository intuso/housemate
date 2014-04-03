package com.intuso.housemate.pkg.server.pc.storage;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/11/13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class SjoerdDBModule extends AbstractModule {

    public SjoerdDBModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(SjoerdDB.PATH_PROPERTY_KEY, defaultProperties.get(Properties.HOUSEMATE_CONFIG_DIR) + File.separator + "database");
    }

    @Override
    protected void configure() {
        bind(SjoerdDB.class).in(Scopes.SINGLETON);
        bind(Storage.class).to(SjoerdDB.class);
    }
}
