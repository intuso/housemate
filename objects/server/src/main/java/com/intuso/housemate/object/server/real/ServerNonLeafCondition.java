package com.intuso.housemate.object.server.real;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ServerNonLeafCondition extends ServerRealCondition
        implements ConditionListener<ServerRealCondition> {

    private final Map<ServerRealCondition, Boolean> satisfied = new HashMap<ServerRealCondition, Boolean>();
    private final Map<ServerRealCondition, ListenerRegistration> conditionListenerRegistrations = Maps.newHashMap();

    public ServerNonLeafCondition(Log log, ListenersFactory listenersFactory, ConditionData data,
                                  ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler) {
        this(log, listenersFactory, data, owner, lifecycleHandler, new ArrayList<ServerRealProperty<?>>());
    }

    public ServerNonLeafCondition(Log log, ListenersFactory listenersFactory, ConditionData data,
                                  ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler,
                                  java.util.List<ServerRealProperty<?>> properties) {
        super(log, listenersFactory, data, owner, lifecycleHandler, properties);
    }

    @Override
    public final void start() {
        for(ServerRealCondition child : getConditions()) {
            child.start();
            conditionListenerRegistrations.put(child, child.addObjectListener(this));
            satisfied.put(child, child.isSatisfied());
        }
        conditionSatisfied(checkIfSatisfied(satisfied));
    }

    @Override
    public final void stop() {
        for(ServerRealCondition child : satisfied.keySet()) {
            conditionListenerRegistrations.get(child).removeListener();
            child.stop();
        }
    }

    @Override
    public final void conditionError(ServerRealCondition condition, String error) {
        setError("Sub condition \"" + condition.getId() + "\" is in error");
    }

    @Override
    public final void conditionSatisfied(ServerRealCondition condition, boolean satisfied) {
        getLog().d("Sub-condition is " + (satisfied ? "" : "un") + "satisfied");
        this.satisfied.put((ServerRealCondition) condition, satisfied);
        conditionSatisfied(checkIfSatisfied(this.satisfied));
    }

    /**
     * Checks if this condition is satisfied, given the satisfied state of the children
     * @param satisfiedMap the map of children to their satisfied value
     * @return true if this condition is currently satisfied
     */
    protected abstract boolean checkIfSatisfied(Map<ServerRealCondition, Boolean> satisfiedMap);
}
