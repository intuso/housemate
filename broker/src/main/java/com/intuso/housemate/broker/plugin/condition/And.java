package com.intuso.housemate.broker.plugin.condition;

import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.condition.BrokerNonLeafCondition;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;

import java.util.Collection;
import java.util.Map;

/**
 * Part of a condition tree whose node is true iff all child nodes are true
 * @author tclabon
 *
 */
@FactoryInformation(id = "and", name = "And", description = "True only when all child conditions are true")
public class And extends BrokerNonLeafCondition {

	/**
	 * Create a new And condition
     * @param resources
	 * @param name
	 * @throws HousemateException
	 */
	public And(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
		super(resources, id, name, description);
    }
	
	/**
	 * Check if all of the sub-conditions are satisfied or not
	 * @return true iff all of the sub-conditions are satisfied
	 */
    @Override
	protected final boolean checkIfSatisfied(Map<BrokerRealCondition, Boolean> satisfiedMap) {
		// get all the satisfied values
		Collection<Boolean> satisfied = satisfiedMap.values();
		
		// go through all of them
		for(Boolean b : satisfied) {
			// if it's false then we're false
			if(!b) {
				getLog().d("One sub-condition is unsatisfied");
				return false;
			}
		}
		
		getLog().d("All sub-conditions are satisfied");
		// we only get here if all sub-conditions are satisfied
		return true;
	}
}