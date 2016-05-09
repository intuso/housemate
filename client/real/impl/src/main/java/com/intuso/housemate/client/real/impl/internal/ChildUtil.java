package com.intuso.housemate.client.real.impl.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 17/12/15.
 */
public class ChildUtil {

    private ChildUtil() {}

    public static String name(String parent, String... childNames) {
        StringBuilder loggerName = new StringBuilder(parent);
        for(String childName : childNames)
            loggerName.append(".").append(childName);
        return loggerName.toString();
    }

    public static Logger logger(Logger parent, String... childNames) {
        return LoggerFactory.getLogger(name(parent.getName(), childNames));
    }
}
