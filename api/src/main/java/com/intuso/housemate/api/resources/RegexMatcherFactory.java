package com.intuso.housemate.api.resources;

public interface RegexMatcherFactory {

    /**
     * Creates a regex matcher for the pattern
     * @param pattern the pattern to match
     * @returna regex matcher for the pattern
     */
    public RegexMatcher createRegexMatcher(String pattern);
}
