package com.intuso.housemate.plugin.main.condition;

import com.intuso.housemate.client.api.internal.annotation.Id;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;

/**
 * Part of a condition tree whose node is true iff all child nodes are true
 *
 */
@Id(value = "and", name = "And", description = "True only when all child conditions are true")
public class And extends LogicCondition {

	private Logger logger;

	@Override
	public void init(Logger logger, Callback callback) {
		this.logger = logger;
		super.init(logger, callback);
	}

	/**
	 * Check if all of the sub-conditions are satisfied or not
	 * @return true iff all of the sub-conditions are satisfied
	 */
    @Override
	protected final boolean checkIfSatisfied(Map<String, Boolean> satisfiedMap) {
		// get all the satisfied values
		Collection<Boolean> satisfied = satisfiedMap.values();
		
		// go through all of them
		for(Boolean b : satisfied) {
			// if it's false then we're false
			if(!b) {
				logger.debug("One sub-condition is unsatisfied");
				return false;
			}
		}
		
		logger.debug("All sub-conditions are satisfied");
		// we only get here if all sub-conditions are satisfied
		return true;
	}
}