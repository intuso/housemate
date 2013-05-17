package com.intuso.housemate.core.resources;

import com.intuso.utils.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 06/02/13
 * Time: 16:11
 * To change this template use File | Settings | File Templates.
 */
public interface Resources {
    public Log getLog();
    public Map<String, String> getProperties();
}
