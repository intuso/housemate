package com.intuso.housemate.broker.plugin.condition;

import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.condition.BrokerNonLeafCondition;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;

import java.util.Collection;
import java.util.Map;

/**
 * Class which is true iff at least one of the sub-conditions is true
 * @author tclabon
 *
 */
@FactoryInformation(id = "or", name = "Or", description = "True if any child condition is true")
public class Or extends BrokerNonLeafCondition {

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