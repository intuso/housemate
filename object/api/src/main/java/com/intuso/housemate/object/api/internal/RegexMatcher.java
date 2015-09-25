package com.intuso.housemate.object.api.internal;

/**
 * Interface for matching regexes
 */
public interface RegexMatcher {

    /**
     * Matches the string against the regex
     * @param string the string to match against
     * @return true if the string matches the pattern
     */
    boolean matches(String string);

    /**
     * Factory for {@link RegexMatcher} instances
     */
    interface Factory {

        /**
         * Creates a regex matcher for the pattern
         * @param pattern the pattern to match
         * @return a regex matcher for the pattern
         */
        RegexMatcher createRegexMatcher(String pattern);
    }
}
