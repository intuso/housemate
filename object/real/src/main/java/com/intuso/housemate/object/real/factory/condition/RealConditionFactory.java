package com.intuso.housemate.object.real.factory.condition;

import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.object.real.RealCondition;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealConditionFactory<CONDITION extends RealCondition> {
    public CONDITION create(ConditionData data, RealConditionOwner owner);
}
