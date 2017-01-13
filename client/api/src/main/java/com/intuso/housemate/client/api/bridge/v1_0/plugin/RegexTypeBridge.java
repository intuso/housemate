package com.intuso.housemate.client.api.bridge.v1_0.plugin;

import com.intuso.housemate.client.api.bridge.v1_0.annotation.IdBridge;
import com.intuso.housemate.client.v1_0.api.plugin.RegexType;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 06/11/15.
 */
public class RegexTypeBridge implements com.intuso.housemate.client.api.internal.plugin.RegexType {

    private final RegexType regexType;

    public RegexTypeBridge(RegexType regexType) {
        this.regexType = regexType;
    }

    @Override
    public com.intuso.housemate.client.api.internal.annotation.Id id() {
        return new IdBridge(regexType.id());
    }

    @Override
    public String regex() {
        return regexType.regex();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return com.intuso.housemate.client.api.internal.plugin.RegexType.class;
    }
}
