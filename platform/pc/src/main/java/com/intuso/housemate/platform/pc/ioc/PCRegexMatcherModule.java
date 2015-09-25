package com.intuso.housemate.platform.pc.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.object.api.internal.RegexMatcher;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:11
 * To change this template use File | Settings | File Templates.
 */
public class PCRegexMatcherModule extends AbstractModule {

    @Override
    protected void configure() {
//        bind(RMF.class).in(Scopes.SINGLETON);
//        bind(RegexMatcher.Factory.class).to(RMF.class);
    }

    public static class RMF implements RegexMatcher.Factory {

        @Override
        public RegexMatcher createRegexMatcher(String pattern) {
            return new RM(pattern);
        }
    }

    public static class RM implements RegexMatcher {

        private final Pattern pattern;

        public RM(String regexPattern) {
            pattern = Pattern.compile(regexPattern);
        }

        @Override
        public boolean matches(String value) {
            return pattern.matcher(value).matches();
        }
    }
}
