package com.intuso.housemate.api.resources;

public interface RegexMatcher {

    /**
     * Matches the string against the regex
     * @param string the string to match against
     * @return true if the string matches the pattern
     */
    public boolean matches(String string);
}
