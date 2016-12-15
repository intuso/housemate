package com.intuso.housemate.plugin.api.internal.type;

/**
 * Created by tomc on 17/05/16.
 */
public interface RegexType {

    String getValue();

    interface Factory<TYPE extends RegexType> {
        TYPE create(String value);
    }
}
