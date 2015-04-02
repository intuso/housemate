package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.object.real.RealCondition;
import com.intuso.housemate.object.real.RealConditionNonLeaf;
import com.intuso.housemate.object.real.factory.condition.AddConditionCommand;
import com.intuso.housemate.object.real.factory.condition.RealConditionOwner;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Collection;
import java.util.Map;

/**
 * Class which is true iff at least one of the sub-conditions is true
 *
 */
@TypeInfo(id = "or", name = "Or", description = "True if any child condition is true")
public class Or extends RealConditionNonLeaf {

    @Inject
	public Or(Log log,
              ListenersFactory listenersFactory,
              AddConditionCommand.Factory addConditionCommandFactory,
              @Assisted ConditionData data,
              @Assisted RealConditionOwner owner) throws HousemateException {
		super(log, listenersFactory, "or", addConditionCommandFactory, data, owner);
    }

    /**
     * Check if at least one of the sub-conditions is satisfied
     * @return true iff at least one of the sub-conditions is satisfied
     */
    @Override
    protected final boolean checkIfSatisfied(Map<RealCondition, Boolean> satisfiedMap) {
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