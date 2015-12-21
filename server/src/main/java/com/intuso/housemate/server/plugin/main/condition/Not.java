package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.impl.internal.driver.LogicCondition;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Condition which is true iff the sub-condition is false
 *
 */
@TypeInfo(id = "not", name = "Not", description = "Negation of the child condition")
public class Not extends LogicCondition {

    private final Logger logger;

    @Inject
    public Not(@Assisted Logger logger, @Assisted ConditionDriver.Callback conditionCallback) {
        super(conditionCallback);
        this.logger = logger;
        // todo move this check somewhere else
        /*if(getConditions().size() > 1) {
            getLog().error("There are multiple sub-conditions for the Not condition \"" + getId() + "\"");
            throw new HousemateException("There are multiple sub-conditions for the Not condition \"" + getId() + "\"");
        } else if(getConditions().size() == 0) {
            getLog().error("There are no sub-conditions for the Not condition \"" + getId() + "\"");
            throw new HousemateException("There are no sub-conditions for the Not condition \"" + getId() + "\"");
        }*/
    }

    @Override
    protected boolean checkIfSatisfied(Map<String, Boolean> satisfiedMap) {
        return !satisfiedMap.values().iterator().next();
    }
}