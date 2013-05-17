package com.intuso.housemate.broker.object.real.condition;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.core.HousemateException;

import java.util.Collection;
import java.util.Map;

/**
 * Class which is true iff at least one of the sub-conditions is true
 * @author tclabon
 *
 */
public class Or extends BrokerNonLeafCondition {

    public final static String TYPE = "or";

    /**
	 * Create a new Or condition
     * @param resources
	 * @param name
	 * @throws HousemateException
	 */
	public Or(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
		super(resources, id, name, description);
    }

    /**
     * Check if at least one of the sub-conditions is satisfied
     * @return true iff at least one of the sub-conditions is satisfied
     */
    @Override
    protected final boolean checkIfSatisfied(Map<BrokerRealCondition, Boolean> satisfiedMap) {
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