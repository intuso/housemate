package com.intuso.housemate.broker.object.real;

import com.intuso.housemate.broker.object.real.condition.BrokerRealCondition;
import com.intuso.housemate.broker.object.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.core.object.condition.ConditionListener;
import com.intuso.housemate.core.object.condition.ConditionWrappable;
import com.intuso.housemate.core.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.core.object.rule.Rule;
import com.intuso.housemate.core.object.rule.RuleListener;
import com.intuso.housemate.core.object.rule.RuleWrappable;
import com.intuso.listeners.ListenerRegistration;

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
            BrokerRealValue<Boolean>, BrokerRealValue<String>, BrokerRealCondition, BrokerRealList<ConditionWrappable, BrokerRealCondition>,
            BrokerRealConsequence, BrokerRealList<ConsequenceWrappable, BrokerRealConsequence>, BrokerRealRule>,
            ConditionListener<BrokerRealCondition> {

    private BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions;
    private BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> satisfiedConsequences;
    private BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> unsatisfiedConsequences;
    private BrokerRealCommand addConditionCommand;
    private BrokerRealCommand addSatisfiedConsequenceCommand;
    private BrokerRealCommand addUnsatisfiedConsequenceCommand;

    private ListenerRegistration<? super ConditionListener<? super BrokerRealCondition>> conditionListenerRegistration;

    public BrokerRealRule(final BrokerRealResources resources, String id, String name, String description) {
        super(resources, new RuleWrappable(id, name, description), "rule");
        this.conditions = new BrokerRealList<ConditionWrappable, BrokerRealCondition>(resources, CONDITIONS, CONDITIONS, "The rule's conditions");
        this.satisfiedConsequences = new BrokerRealList<ConsequenceWrappable, BrokerRealConsequence>(resources, SATISFIED_CONSEQUENCES, SATISFIED_CONSEQUENCES, "The consequences to run when the rule is satisfied");
        this.unsatisfiedConsequences = new BrokerRealList<ConsequenceWrappable, BrokerRealConsequence>(resources, UNSATISFIED_CONSEQUENCES, UNSATISFIED_CONSEQUENCES, "The consequences to run when the rule is satisfied");
        addConditionCommand = getResources().getGeneralResources().getConditionFactory().createAddConditionCommand(ADD_CONDITION, ADD_CONDITION, "Add a new condition", conditions);
        addSatisfiedConsequenceCommand = getResources().getGeneralResources().getConsequenceFactory().createAddConsequenceCommand(ADD_SATISFIED_CONSEQUENCE, ADD_SATISFIED_CONSEQUENCE, "Add a new satisfied consequence", satisfiedConsequences);
        addUnsatisfiedConsequenceCommand = getResources().getGeneralResources().getConsequenceFactory().createAddConsequenceCommand(ADD_UNSATISFIED_CONSEQUENCE, ADD_UNSATISFIED_CONSEQUENCE, "Add a new unsatisfied consequence", unsatisfiedConsequences);
        getRunningValue().addObjectListener(new ValueListener<BrokerRealValue<Boolean>>() {
            @Override
            public void valueChanged(BrokerRealValue<Boolean> value) {
                try {
                    resources.getGeneralResources().getStorage().saveValue(value.getPath(), value.getValue());
                } catch(HousemateException e) {
                    getLog().e("Failed to save running value of rule");
                    getLog().st(e);
                }
            }
        });
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
        try {
            getResources().getGeneralResources().getStorage().removeDetails(getPath());
        } catch(HousemateException e) {
            getResources().getLog().e("Failed to remove stored details for rule " + getId());
        }
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
            getRunningValue().setTypedValue(Boolean.TRUE);
        }
    }

    public void stop() {
        for(BrokerRealCondition condition : conditions)
            condition.stop();
        if(conditionListenerRegistration != null) {
            conditionListenerRegistration.removeListener();
            conditionListenerRegistration = null;
        }
        getRunningValue().setTypedValue(Boolean.FALSE);
    }
}
