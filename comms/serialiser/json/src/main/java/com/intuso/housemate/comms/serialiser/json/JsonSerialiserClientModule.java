package com.intuso.housemate.comms.serialiser.json;

import com.google.inject.AbstractModule;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:32
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerialiserClientModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StreamSerialiserFactory.class).to(JsonSerialiser.Factory.class);
    }
}
