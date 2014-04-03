package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerNonLeafCondition;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
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
public class Or extends ServerNonLeafCondition {

    @Inject
	public Or(Log log,
              ListenersFactory listenersFactory,
              @Assisted ConditionData data,
              @Assisted ServerRealConditionOwner owner,
              LifecycleHandler lifecycleHandler) throws HousemateException {
		super(log, listenersFactory, data, owner, lifecycleHandler);
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