package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Maps;
import com.intuso.housemate.client.real.api.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.api.internal.factory.condition.RealConditionOwner;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.object.api.internal.Condition;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class RealConditionNonLeaf extends RealCondition
        implements Condition.Listener<RealCondition> {

    private final Map<RealCondition, Boolean> satisfied = new HashMap<>();
    private final Map<RealCondition, ListenerRegistration> conditionListenerRegistrations = Maps.newHashMap();
    private RealCommand addConditionCommand;

    public RealConditionNonLeaf(Log log, ListenersFactory listenersFactory, String type,
                                AddConditionCommand.Factory addConditionCommandFactory, ConditionData data,
                                RealConditionOwner owner) {
        this(log, listenersFactory, type, addConditionCommandFactory, data, owner, new ArrayList<RealProperty<?>>());
    }

    public RealConditionNonLeaf(Log log, ListenersFactory listenersFactory, String type,
                                AddConditionCommand.Factory addConditionCommandFactory, ConditionData data,
                                RealConditionOwner owner, java.util.List<RealProperty<?>> properties) {
        super(log, listenersFactory, type, data, owner, properties);
        addConditionCommand = addConditionCommandFactory.create(this);
        addChild(addConditionCommand);
    }

    @Override
    public final void start() {
        for(RealCondition child : getConditions()) {
            child.start();
            conditionListenerRegistrations.put(child, child.addObjectListener(this));
            satisfied.put(child, child.isSatisfied());
        }
        conditionSatisfied(checkIfSatisfied(satisfied));
    }

    @Override
    public final void stop() {
        for(RealCondition child : satisfied.keySet()) {
            conditionListenerRegistrations.get(child).removeListener();
            child.stop();
        }
    }

    @Override
    public final void conditionError(RealCondition condition, String error) {
        setError("Sub condition \"" + condition.getId() + "\" is in error");
    }

    @Override
    public final void conditionSatisfied(RealCondition condition, boolean satisfied) {
        getLog().d("Sub-condition is " + (satisfied ? "" : "un") + "satisfied");
        this.satisfied.put((RealCondition) condition, satisfied);
        conditionSatisfied(checkIfSatisfied(this.satisfied));
    }

    @Override
    public RealCommand getAddConditionCommand() {
        return addConditionCommand;
    }

    /**
     * Checks if this condition is satisfied, given the satisfied state of the children
     * @param satisfiedMap the map of children to their satisfied value
     * @return true if this condition is currently satisfied
     */
    protected abstract boolean checkIfSatisfied(Map<RealCondition, Boolean> satisfiedMap);
}
