package com.intuso.housemate.object.broker.real;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BrokerNonLeafCondition extends BrokerRealCondition
        implements ConditionListener<BrokerRealCondition> {

    private final Map<BrokerRealCondition, Boolean> satisfied = new HashMap<BrokerRealCondition, Boolean>();
    private final Map<BrokerRealCondition, ListenerRegistration> conditionListenerRegistrations = Maps.newHashMap();

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public BrokerNonLeafCondition(BrokerRealResources resources, String id, String name, String description) {
        this(resources, id, name, description, new ArrayList<BrokerRealProperty<?>>());
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param properties the properties of the condition
     */
    public BrokerNonLeafCondition(BrokerRealResources resources, String id, String name, String description, java.util.List<BrokerRealProperty<?>> properties) {
        super(resources, id, name, description, properties);
    }

    @Override
    public final void start() {
        for(BrokerRealCondition child : getConditions()) {
            child.start();
            conditionListenerRegistrations.put(child, child.addObjectListener(this));
            satisfied.put(child, child.isSatisfied());
        }
        conditionSatisfied(checkIfSatisfied(satisfied));
    }

    @Override
    public final void stop() {
        for(BrokerRealCondition child : satisfied.keySet()) {
            conditionListenerRegistrations.get(child).removeListener();
            child.stop();
        }
    }

    @Override
    public final void conditionError(BrokerRealCondition condition, String error) {
        setError("Sub condition \"" + condition.getId() + "\" is in error");
    }

    @Override
    public final void conditionSatisfied(BrokerRealCondition condition, boolean satisfied) {
        getLog().d("Sub-condition is " + (satisfied ? "" : "un") + "satisfied");
        this.satisfied.put((BrokerRealCondition) condition, satisfied);
        conditionSatisfied(checkIfSatisfied(this.satisfied));
    }

    /**
     * Checks if this condition is satisfied, given the satisfied state of the children
     * @param satisfiedMap the map of children to their satisfied value
     * @return true if this condition is currently satisfied
     */
    protected abstract boolean checkIfSatisfied(Map<BrokerRealCondition, Boolean> satisfiedMap);
}
