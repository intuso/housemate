package com.intuso.housemate.platform.pc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.api.resources.RegexMatcher;

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
        bind(RegexMatcher.class).to(RM.class);
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
