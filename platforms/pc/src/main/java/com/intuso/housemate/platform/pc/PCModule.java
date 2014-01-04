package com.intuso.housemate.platform.pc;

import com.google.inject.AbstractModule;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class PCModule extends AbstractModule {

    private final String[] args;

    public PCModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() {
        install(new PCPropertiesModule(args));
        install(new PCLogModule());
        install(new PCRegexMatcherModule());
    }
}
