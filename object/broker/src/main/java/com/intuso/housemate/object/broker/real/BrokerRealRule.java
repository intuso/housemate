package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.rule.Rule;
import com.intuso.housemate.api.object.rule.RuleListener;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealRule
        extends BrokerRealPrimaryObject<RuleWrappable, BrokerRealRule, RuleListener<? super BrokerRealRule>>
        implements Rule<BrokerRealProperty<String>, BrokerRealCommand, BrokerRealCommand, BrokerRealCommand,
            BrokerRealValue<Boolean>, BrokerRealValue<Boolean>, BrokerRealValue<String>, BrokerRealCondition, BrokerRealList<ConditionWrappable, BrokerRealCondition>,
            BrokerRealConsequence, BrokerRealList<ConsequenceWrappable, BrokerRealConsequence>, BrokerRealRule>,
            ConditionListener<BrokerRealCondition> {

    private BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions;
    private BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> satisfiedConsequences;
    private BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> unsatisfiedConsequences;
    private BrokerRealCommand addConditionCommand;
    private BrokerRealCommand addSatisfiedConsequenceCommand;
    private BrokerRealCommand addUnsatisfiedConsequenceCommand;

    private ListenerRegistration conditionListenerRegistration;

    public BrokerRealRule(final BrokerRealResources resources, String id, String name, String description) {
        super(resources, new RuleWrappable(id, name, description), "rule");
        this.conditions = new BrokerRealList<ConditionWrappable, BrokerRealCondition>(resources, CONDITIONS, CONDITIONS, "The rule's conditions");
        this.satisfiedConsequences = new BrokerRealList<ConsequenceWrappable, BrokerRealConsequence>(resources, SATISFIED_CONSEQUENCES, SATISFIED_CONSEQUENCES, "The consequences to run when the rule is satisfied");
        this.unsatisfiedConsequences = new BrokerRealList<ConsequenceWrappable, BrokerRealConsequence>(resources, UNSATISFIED_CONSEQUENCES, UNSATISFIED_CONSEQUENCES, "The consequences to run when the rule is satisfied");
        addConditionCommand = getResources().getLifecycleHandler().createAddConditionCommand(conditions);
        addSatisfiedConsequenceCommand = getResources().getLifecycleHandler().createAddSatisfiedConsequenceCommand(satisfiedConsequences);
        addUnsatisfiedConsequenceCommand = getResources().getLifecycleHandler().createAddUnsatisfiedConsequenceCommand(unsatisfiedConsequences);
        addWrapper(conditions);
        addWrapper(satisfiedConsequences);
        addWrapper(unsatisfiedConsequences);
        addWrapper(addConditionCommand);
        addWrapper(addSatisfiedConsequenceCommand);
        addWrapper(addUnsatisfiedConsequenceCommand);
    }

    @Override
    protected void remove() {
        getResources().getRoot().getRules().remove(getId());
        getResources().getLifecycleHandler().ruleRemoved(getPath());
    }

    @Override
    public BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> getSatisfiedConsequences() {
        return satisfiedConsequences;
    }

    @Override
    public BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> getUnsatisfiedConsequences() {
        return unsatisfiedConsequences;
    }

    @Override
    public BrokerRealCommand getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public BrokerRealCommand getAddSatisifedConsequenceCommand() {
        return addSatisfiedConsequenceCommand;
    }

    @Override
    public BrokerRealCommand getAddUnsatisifedConsequenceCommand() {
        return addUnsatisfiedConsequenceCommand;
    }

    @Override
    public BrokerRealList<ConditionWrappable, BrokerRealCondition> getConditions() {
        return conditions;
    }

    @Override
    public void conditionError(BrokerRealCondition condition, String error) {
        // do nothing for now
    }

    @Override
    public void conditionSatisfied(BrokerRealCondition condition, boolean satisfied) {
        try {
            getLog().d("Rule " + (satisfied ? "" : "un") + "satisfied, executing consequences");
            for(BrokerRealConsequence consequence : (satisfied ? satisfiedConsequences : unsatisfiedConsequences))
                consequence.execute();
        } catch (HousemateException e) {
            getErrorValue().setTypedValue("Failed to perform rule consequences: " + e.getMessage());
            getLog().e("Failed to perform rule consequences");
            getLog().st(e);
        }
    }

    public void start() throws HousemateException {
        if(conditions.size() == 0)
            throw new HousemateException("Rule has no condition. It must have exactly one");
        else if(conditions.size() > 1)
            throw new HousemateException(("Rule has multiple conditions. It can only have one"));
        else {
            BrokerRealCondition condition = conditions.iterator().next();
            condition.start();
            conditionListenerRegistration = condition.addObjectListener(this);
        }
    }

    public void stop() {
        for(BrokerRealCondition condition : conditions)
            condition.stop();
        if(conditionListenerRegistration != null) {
            conditionListenerRegistration.removeListener();
            conditionListenerRegistration = null;
        }
    }
}
