package com.intuso.housemate.broker.storage.impl;

import com.google.inject.AbstractModule;
import com.intuso.housemate.broker.storage.Storage;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/11/13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class SjoerdDBModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Storage.class).to(SjoerdDB.class);
    }
}
