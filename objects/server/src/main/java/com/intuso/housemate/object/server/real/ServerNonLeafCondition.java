package com.intuso.housemate.object.server.real;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ServerNonLeafCondition extends ServerRealCondition
        implements ConditionListener<ServerRealCondition> {

    private final Map<ServerRealCondition, Boolean> satisfied = new HashMap<ServerRealCondition, Boolean>();
    private final Map<ServerRealCondition, ListenerRegistration> conditionListenerRegistrations = Maps.newHashMap();

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public ServerNonLeafCondition(ServerRealResources resources, String id, String name, String description,
                                  ServerRealConditionOwner owner) {
        this(resources, id, name, description, owner, new ArrayList<ServerRealProperty<?>>());
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param properties the properties of the condition
     */
    public ServerNonLeafCondition(ServerRealResources resources, String id, String name, String description,
                                  ServerRealConditionOwner owner, java.util.List<ServerRealProperty<?>> properties) {
        super(resources, id, name, description, owner, properties);
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
