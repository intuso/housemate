package com.intuso.housemate.client.serialisation.javabin.internal.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.serialisation.api.internal.Serialiser;
import com.intuso.housemate.client.serialisation.javabin.internal.JavabinSerialiser;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:32
 * To change this template use File | Settings | File Templates.
 */
public class JavabinSerialiserClientModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Serialiser.class).to(JavabinSerialiser.class);
    }
}
