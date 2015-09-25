package com.intuso.housemate.client.real.api.internal.factory.condition;

import com.intuso.housemate.client.real.api.internal.RealCondition;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealConditionFactory<CONDITION extends RealCondition> {
    public CONDITION create(ConditionData data, RealConditionOwner owner);
}
