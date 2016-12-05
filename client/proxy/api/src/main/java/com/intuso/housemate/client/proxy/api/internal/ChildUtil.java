package com.intuso.housemate.client.proxy.api.internal;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 17/12/15.
 */
public class ChildUtil {

    private final static Joiner DOT_JOINER = Joiner.on(".");

    private ChildUtil() {}

    public static String name(String parent, String... childNames) {
        if(parent == null && childNames.length == 0)
            return "";
        else if(parent == null)
            return DOT_JOINER.join(childNames);
        else if(childNames.length == 0)
            return parent;
        else
            return parent + "." + DOT_JOINER.join(childNames);
    }

    public static Logger logger(Logger parent, String... childNames) {
        return LoggerFactory.getLogger(name(parent.getName(), childNames));
    }
}
