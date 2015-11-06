package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.intuso.housemate.plugin.api.internal.OperationType;

/**
 * Created by tomc on 06/11/15.
 */
public class OperationTypeBridge implements OperationType {

    private final com.intuso.housemate.plugin.v1_0.api.OperationType operationType;

    public OperationTypeBridge(com.intuso.housemate.plugin.v1_0.api.OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public String getDescription() {
        return operationType.getDescription();
    }

    @Override
    public String getId() {
        return operationType.getId();
    }

    @Override
    public String getName() {
        return operationType.getName();
    }
}
