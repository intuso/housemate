package com.intuso.housemate.client.api.bridge.v1_0.plugin;

import com.intuso.housemate.client.api.bridge.v1_0.annotation.IdBridge;
import com.intuso.housemate.client.v1_0.api.plugin.ChoiceType;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 06/11/15.
 */
public class ChoiceTypeBridge implements com.intuso.housemate.client.api.internal.plugin.ChoiceType {

    private final ChoiceType choiceType;

    public ChoiceTypeBridge(ChoiceType choiceType) {
        this.choiceType = choiceType;
    }

    @Override
    public com.intuso.housemate.client.api.internal.annotation.Id id() {
        return new IdBridge(choiceType.id());
    }

    @Override
    public Class<? extends Enum> options() {
        return choiceType.options();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return com.intuso.housemate.client.api.internal.plugin.ChoiceType.class;
    }
}
