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

import java.util.Map;

/**
 * Condition which is true iff the sub-condition is false
 *
 */
@TypeInfo(id = "not", name = "Not", description = "Negation of the child condition")
public class Not extends ServerNonLeafCondition {

    @Inject
	public Not(Log log,
               ListenersFactory listenersFactory,
               @Assisted ConditionData data,
               @Assisted ServerRealConditionOwner owner,
               LifecycleHandler lifecycleHandler) throws HousemateException {
		super(log, listenersFactory, data, owner, lifecycleHandler);

        // todo move this check somewhere else
        /*if(getConditions().size() > 1) {
            getLog().e("There are multiple sub-conditions for the Not condition \"" + getId() + "\"");
            throw new HousemateException("There are multiple sub-conditions for the Not condition \"" + getId() + "\"");
        } else if(getConditions().size() == 0) {
            getLog().e("There are no sub-conditions for the Not condition \"" + getId() + "\"");
            throw new HousemateException("There are no sub-conditions for the Not condition \"" + getId() + "\"");
        }*/
    }

    @Override
    protected boolean checkIfSatisfied(Map<ServerRealCondition, Boolean> satisfiedMap) {
        return !satisfiedMap.values().iterator().next();
    }
}