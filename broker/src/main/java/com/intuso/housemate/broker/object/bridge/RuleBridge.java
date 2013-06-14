package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.Consequence;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.rule.Rule;
import com.intuso.housemate.api.object.rule.RuleListener;
import com.intuso.housemate.api.object.rule.RuleWrappable;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/08/12
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class RuleBridge
        extends PrimaryObjectBridge<RuleWrappable, RuleBridge, RuleListener<? super RuleBridge>>
        implements Rule<PropertyBridge, CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge, ValueBridge,
                    ConditionBridge,ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge>, ConsequenceBridge,
                    ListBridge<ConsequenceWrappable, Consequence<?, ?, ?, ?>, ConsequenceBridge>, RuleBridge> {

    private ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private ListBridge<ConsequenceWrappable, Consequence<?, ?, ?, ?>, ConsequenceBridge> satisfiedConsequenceList;
    private ListBridge<ConsequenceWrappable, Consequence<?, ?, ?, ?>, ConsequenceBridge> unsatisfiedConsequenceList;
    private CommandBridge addCondition;
    private CommandBridge addSatisfiedConsequence;
    private CommandBridge addUnsatisfiedConsequence;
    
    public RuleBridge(BrokerBridgeResources resources, Rule<?, ?, ?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?>, ?, ? extends Consequence<?, ?, ?, ?>, ?, ?> rule) {
        super(resources, new RuleWrappable(rule.getId(), rule.getName(), rule.getDescription()), rule);
        conditionList = new ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge>(resources,  rule.getConditions(), new ConditionBridge.Converter(resources));
        satisfiedConsequenceList = new ListBridge<ConsequenceWrappable, Consequence<?, ?, ?, ?>, ConsequenceBridge>(resources, rule.getSatisfiedConsequences(), new ConsequenceBridge.Converter(resources));
        unsatisfiedConsequenceList = new ListBridge<ConsequenceWrappable, Consequence<?, ?, ?, ?>, ConsequenceBridge>(resources, rule.getUnsatisfiedConsequences(), new ConsequenceBridge.Converter(resources));
        addCondition = new CommandBridge(resources, rule.getAddConditionCommand());
        addSatisfiedConsequence = new CommandBridge(resources, rule.getAddSatisifedConsequenceCommand());
        addUnsatisfiedConsequence = new CommandBridge(resources, rule.getAddUnsatisifedConsequenceCommand());
        addWrapper(conditionList);
        addWrapper(satisfiedConsequenceList);
        addWrapper(unsatisfiedConsequenceList);
        addWrapper(addCondition);
        addWrapper(addSatisfiedConsequence);
        addWrapper(addUnsatisfiedConsequence);
    }

    @Override
    public CommandBridge getAddUnsatisifedConsequenceCommand() {
        return addUnsatisfiedConsequence;
    }

    @Override
    public CommandBridge getAddSatisifedConsequenceCommand() {
        return addSatisfiedConsequence;
    }

    @Override
    public CommandBridge getAddConditionCommand() {
        return addCondition;
    }

    @Override
    public ListBridge<ConsequenceWrappable, Consequence<?, ?, ?, ?>, ConsequenceBridge> getUnsatisfiedConsequences() {
        return unsatisfiedConsequenceList;
    }

    @Override
    public ListBridge<ConsequenceWrappable, Consequence<?, ?, ?, ?>, ConsequenceBridge> getSatisfiedConsequences() {
        return satisfiedConsequenceList;
    }

    @Override
    public ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
        return conditionList;
    }

    public final static class Converter implements Function<Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, RuleBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public RuleBridge apply(@Nullable Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> rule) {
            return new RuleBridge(resources, rule);
        }
    }
}
