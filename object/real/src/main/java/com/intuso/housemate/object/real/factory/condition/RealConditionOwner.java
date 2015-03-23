package com.intuso.housemate.object.real.factory.condition;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.object.real.RealCondition;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealConditionOwner {
    public ChildOverview getAddConditionCommandDetails();
    public void addCondition(RealCondition condition);
    public void removeCondition(RealCondition condition);
}
