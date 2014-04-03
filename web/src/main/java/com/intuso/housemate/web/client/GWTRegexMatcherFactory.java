package com.intuso.housemate.web.client;

import com.google.gwt.regexp.shared.RegExp;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.api.resources.RegexMatcherFactory;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 15/01/14
* Time: 07:29
* To change this template use File | Settings | File Templates.
*/
public class GWTRegexMatcherFactory implements RegexMatcherFactory {

    @Override
    public RegexMatcher createRegexMatcher(String pattern) {
        return new RM(pattern);
    }

    private static class RM implements RegexMatcher {

        RegExp pattern;

        public RM(String regexPattern) {
            pattern = RegExp.compile(regexPattern);
        }

        @Override
        public boolean matches(String value) {
            return pattern.test(value);
        }
    }
}
