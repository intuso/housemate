package com.intuso.housemate.platform.android.common;

import com.intuso.housemate.api.resources.RegexMatcherFactory;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 18/10/13
 * Time: 09:12
 * To change this template use File | Settings | File Templates.
 */
public class RegexMatcher implements com.intuso.housemate.api.resources.RegexMatcher {

    private final Pattern pattern;

    private RegexMatcher(String regexPattern) {
        pattern = Pattern.compile(regexPattern);
    }

    @Override
    public boolean matches(String value) {
        return pattern.matcher(value).matches();
    }

    public static class Factory implements RegexMatcherFactory {

        @Override
        public com.intuso.housemate.api.resources.RegexMatcher createRegexMatcher(String pattern) {
            return new RegexMatcher(pattern);
        }
    }
}