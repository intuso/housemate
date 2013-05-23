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
        implements Rule<PropertyBridge, CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge,
                    ConditionBridge,ListBridge<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionWrappable, ConditionBridge>, ConsequenceBridge,
                    ListBridge<Consequence<?, ?, ?, ?, ?>, ConsequenceWrappable, ConsequenceBridge>, RuleBridge> {

    private ListBridge<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionWrappable, ConditionBridge> conditionList;
    private ListBridge<Consequence<?, ?, ?, ?, ?>, ConsequenceWrappable, ConsequenceBridge> satisfiedConsequenceList;
    private ListBridge<Consequence<?, ?, ?, ?, ?>, ConsequenceWrappable, ConsequenceBridge> unsatisfiedConsequenceList;
    private CommandBridge addCondition;
    private CommandBridge addSatisfiedConsequence;
    private CommandBridge addUnsatisfiedConsequence;
    
    public RuleBridge(BrokerBridgeResources resources, Rule<?, ?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?, ?>, ?, ? extends Consequence<?, ?, ?, ?, ?>, ?, ?> rule) {
        super(resources, new RuleWrappable(rule.getId(), rule.getName(), rule.getDescription()), rule);
        conditionList = new ListBridge<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionWrappable, ConditionBridge>(resources,  rule.getConditions(), new ConditionConverter(resources));
        satisfiedConsequenceList = new ListBridge<Consequence<?, ?, ?, ?, ?>, ConsequenceWrappable, ConsequenceBridge>(resources, rule.getSatisfiedConsequences(), new ConsequenceConverter(resources));
        unsatisfiedConsequenceList = new ListBridge<Consequence<?, ?, ?, ?, ?>, ConsequenceWrappable, ConsequenceBridge>(resources, rule.getUnsatisfiedConsequences(), new ConsequenceConverter(resources));
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
    public ListBridge<Consequence<?, ?, ?, ?, ?>, ConsequenceWrappable, ConsequenceBridge> getUnsatisfiedConsequences() {
        return unsatisfiedConsequenceList;
    }

    @Override
    public ListBridge<Consequence<?, ?, ?, ?, ?>, ConsequenceWrappable, ConsequenceBridge> getSatisfiedConsequences() {
        return satisfiedConsequenceList;
    }

    @Override
    public ListBridge<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionWrappable, ConditionBridge> getConditions() {
        return conditionList;
    }

    private class ConditionConverter implements Function<Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> {

        private final BrokerBridgeResources resources;

        private ConditionConverter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ConditionBridge apply(@Nullable Condition<?, ?, ?, ?, ?, ?, ?> command) {
            return new ConditionBridge(resources, command);
        }
    }

    private class ConsequenceConverter implements Function<Consequence<?, ?, ?, ?, ?>, ConsequenceBridge> {

        private final BrokerBridgeResources resources;

        private ConsequenceConverter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ConsequenceBridge apply(@Nullable Consequence<?, ?, ?, ?, ?> command) {
            return new ConsequenceBridge(resources, command);
        }
    }
}
