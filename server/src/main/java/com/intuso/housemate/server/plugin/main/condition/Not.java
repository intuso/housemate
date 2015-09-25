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

import java.util.Map;

/**
 * Condition which is true iff the sub-condition is false
 *
 */
@TypeInfo(id = "not", name = "Not", description = "Negation of the child condition")
public class Not extends RealConditionNonLeaf {

    @Inject
	public Not(Log log,
               ListenersFactory listenersFactory,
               AddConditionCommand.Factory addConditionCommandFactory,
               @Assisted ConditionData data,
               @Assisted RealConditionOwner owner) {
		super(log, listenersFactory, "not", addConditionCommandFactory, data, owner);

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
    protected boolean checkIfSatisfied(Map<RealCondition, Boolean> satisfiedMap) {
        return !satisfiedMap.values().iterator().next();
    }
}