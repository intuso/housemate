package com.intuso.housemate.api.resources;

/**
 * Interface for matching regexes
 */
public interface RegexMatcher {

    /**
     * Matches the string against the regex
     * @param string the string to match against
     * @return true if the string matches the pattern
     */
    public boolean matches(String string);
}
