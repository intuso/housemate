package com.intuso.housemate.platform.pc.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.platform.pc.CopyOnWriteListenersFactory;
import com.intuso.utilities.listener.ListenersFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class PCModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new PCRegexMatcherModule());
        bind(ListenersFactory.class).to(CopyOnWriteListenersFactory.class);
    }
}
