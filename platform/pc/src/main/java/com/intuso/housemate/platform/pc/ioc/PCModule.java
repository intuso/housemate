package com.intuso.housemate.platform.pc.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.platform.pc.CopyOnWriteListenersFactory;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        bind(Logger.class).toInstance(LoggerFactory.getLogger("com.intuso.housemate"));
        install(new PCRegexMatcherModule());
        bind(ListenersFactory.class).to(CopyOnWriteListenersFactory.class);
    }
}
