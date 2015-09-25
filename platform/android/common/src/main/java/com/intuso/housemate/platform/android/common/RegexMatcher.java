package com.intuso.housemate.platform.android.common;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 18/10/13
 * Time: 09:12
 * To change this template use File | Settings | File Templates.
 */
public class RegexMatcher implements com.intuso.housemate.object.v1_0.api.RegexMatcher {

    private final Pattern pattern;

    private RegexMatcher(String regexPattern) {
        pattern = Pattern.compile(regexPattern);
    }

    @Override
    public boolean matches(String value) {
        return pattern.matcher(value).matches();
    }

    public static class Factory implements com.intuso.housemate.object.v1_0.api.RegexMatcher.Factory {

        @Override
        public com.intuso.housemate.object.v1_0.api.RegexMatcher createRegexMatcher(String pattern) {
            return new RegexMatcher(pattern);
        }
    }
}
