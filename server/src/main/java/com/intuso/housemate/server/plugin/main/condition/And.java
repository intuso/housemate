package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCondition;
import com.intuso.housemate.client.real.api.internal.RealConditionNonLeaf;
import com.intuso.housemate.client.real.api.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.api.internal.factory.condition.RealConditionOwner;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Collection;
import java.util.Map;

/**
 * Part of a condition tree whose node is true iff all child nodes are true
 *
 */
@TypeInfo(id = "and", name = "And", description = "True only when all child conditions are true")
public class And extends RealConditionNonLeaf {

    @Inject
	public And(Log log,
               ListenersFactory listenersFactory,
               AddConditionCommand.Factory addConditionCommandFactory,
               @Assisted ConditionData data,
               @Assisted RealConditionOwner owner) {
		super(log, listenersFactory, "and", addConditionCommandFactory, data, owner);
    }
	
	/**
	 * Check if all of the sub-conditions are satisfied or not
	 * @return true iff all of the sub-conditions are satisfied
	 */
    @Override
	protected final boolean checkIfSatisfied(Map<RealCondition, Boolean> satisfiedMap) {
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