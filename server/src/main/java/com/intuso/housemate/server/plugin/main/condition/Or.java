package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.impl.internal.driver.LogicCondition;
import com.intuso.utilities.log.Log;

import java.util.Collection;
import java.util.Map;

/**
 * Class which is true iff at least one of the sub-conditions is true
 *
 */
@TypeInfo(id = "or", name = "Or", description = "True if any child condition is true")
public class Or extends LogicCondition {

    private final Log log;

    @Inject
    public Or(Log log, @Assisted Callback conditionCallback) {
        super(conditionCallback);
        this.log = log;
    }

    /**
     * Check if at least one of the sub-conditions is satisfied
     * @return true iff at least one of the sub-conditions is satisfied
     */
    @Override
    protected final boolean checkIfSatisfied(Map<String, Boolean> satisfiedMap) {
        // get all the satisfied values
        Collection<Boolean> satisfied = satisfiedMap.values();

        // go through them
        for(Boolean b : satisfied) {
            // if one is true, we're true
            if(b) {
                log.d("One sub-condition is satisfied");
                return true;
            }
        }

        // we only get here if they were all false
        log.d("All sub-conditions are unsatisfied");
        return false;
    }
}