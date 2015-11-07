package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.intuso.housemate.plugin.api.internal.Operator;

/**
 * Created by tomc on 06/11/15.
 */
public class OperatorBridge<I, O> implements Operator<I, O> {

    private final com.intuso.housemate.plugin.v1_0.api.Operator<I, O> operator;

    public OperatorBridge(com.intuso.housemate.plugin.v1_0.api.Operator<I, O> operator) {
        this.operator = operator;
    }

    @Override
    public String getInputTypeId() {
        return operator.getInputTypeId();
    }

    @Override
    public String getOutputTypeId() {
        return operator.getOutputTypeId();
    }

    @Override
    public O apply(I first, I second) {
        return operator.apply(first, second);
    }
}
