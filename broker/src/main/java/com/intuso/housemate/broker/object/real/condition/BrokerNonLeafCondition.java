package com.intuso.housemate.broker.object.real.condition;

import com.intuso.housemate.broker.object.real.BrokerRealProperty;
import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.core.object.condition.ConditionListener;
import com.intuso.listeners.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 28/05/12
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class BrokerNonLeafCondition extends BrokerRealCondition
        implements ConditionListener<BrokerRealCondition> {

    private final Map<BrokerRealCondition, Boolean> satisfied = new HashMap<BrokerRealCondition, Boolean>();
    private final Map<BrokerRealCondition, ListenerRegistration<? super ConditionListener<? super BrokerRealCondition>>> conditionListenerRegistrations = new HashMap<BrokerRealCondition, ListenerRegistration<? super ConditionListener<? super BrokerRealCondition>>>();

    public BrokerNonLeafCondition(BrokerRealResources resources, String id, String name, String description) {
        this(resources, id, name, description, new ArrayList<BrokerRealProperty<?>>());
    }

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
    
    protected abstract boolean checkIfSatisfied(Map<BrokerRealCondition, Boolean> satisfiedMap);
}
