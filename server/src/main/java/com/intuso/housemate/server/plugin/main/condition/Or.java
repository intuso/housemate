package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerNonLeafCondition;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.utilities.log.Log;

import java.util.Collection;
import java.util.Map;

/**
 * Class which is true iff at least one of the sub-conditions is true
 *
 */
@FactoryInformation(id = "or", name = "Or", description = "True if any child condition is true")
public class Or extends ServerNonLeafCondition {

    /**
	 * Create a new Or condition
     * @param log
	 * @param name
	 * @throws HousemateException
	 */
    @Inject
	public Or(Log log, String id, String name, String description, ServerRealConditionOwner owner,
              LifecycleHandler lifecycleHandler) throws HousemateException {
		super(log, id, name, description, owner, lifecycleHandler);
    }

    /**
     * Check if at least one of the sub-conditions is satisfied
     * @return true iff at least one of the sub-conditions is satisfied
     */
    @Override
    protected final boolean checkIfSatisfied(Map<ServerRealCondition, Boolean> satisfiedMap) {
        // get all the satisfied values
        Collection<Boolean> satisfied = satisfiedMap.values();

        // go through them
        for(Boolean b : satisfied) {
            // if one is true, we're true
            if(b) {
                getLog().d("One sub-condition is satisfied");
                return true;
            }
        }

        // we only get here if they were all false
        getLog().d("All sub-conditions are unsatisfied");
        return false;
    }
}