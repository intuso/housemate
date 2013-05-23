package com.intuso.housemate.api.resources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 31/03/12
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public interface RegexMatcherFactory {
    public RegexMatcher createRegexMatcher(String pattern);
}
