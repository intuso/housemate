package com.intuso.housemate.client.proxy.internal;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 17/12/15.
 */
public class ChildUtil {

    private final static Joiner DOT_JOINER = Joiner.on(".");
    private final static Joiner SLASH_JOINER = Joiner.on("/");

    private ChildUtil() {}

    public static String name(String parent, String... childNames) {
        return name(SLASH_JOINER, parent, childNames);
    }

    private static String name(Joiner joiner, String parent, String... childNames) {
        if(parent == null && childNames.length == 0)
            return "";
        else if(parent == null)
            return joiner.join(childNames);
        else if(childNames.length == 0)
            return parent;
        else
            return joiner.join(parent, joiner.join(childNames));
    }

    public static Logger logger(Logger parent, String... childNames) {
        return LoggerFactory.getLogger(name(DOT_JOINER, parent.getName(), childNames));
    }
}