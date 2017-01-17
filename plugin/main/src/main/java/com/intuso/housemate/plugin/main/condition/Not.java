package com.intuso.housemate.plugin.main.condition;

import com.intuso.housemate.client.api.internal.annotation.Id;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Condition which is true iff the sub-condition is false
 *
 */
@Id(value = "not", name = "Not", description = "Negation of the child condition")
public class Not extends LogicCondition {

    private Logger logger;

    @Override
    public void init(Logger logger, Callback callback) {
        this.logger = logger;
        super.init(logger, callback);
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