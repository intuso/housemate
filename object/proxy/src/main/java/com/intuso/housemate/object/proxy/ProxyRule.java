package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.rule.Rule;
import com.intuso.housemate.api.object.rule.RuleListener;
import com.intuso.housemate.api.object.rule.RuleWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyRule<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            P extends ProxyProperty<?, ?, ?, ?, P>, C extends ProxyCommand<?, ?, ?, ?, C>,
            V extends ProxyValue<?, ?, V>,
            Cond extends ProxyCondition<?, ?, ?, ?, ?, ?, Cond, CondL>,
            CondL extends ProxyList<?, ?, ConditionWrappable, Cond, CondL>,
            Cons extends ProxyConsequence<?, ?, ?, ?, ?, Cons>,
            ConsL extends ProxyList<?, ?, ConsequenceWrappable, Cons, ConsL>,
            Ru extends ProxyRule<R, SR, P, C, V, Cond, CondL, Cons, ConsL, Ru>>
        extends ProxyPrimaryObject<R, SR, RuleWrappable, P, C, V, Ru, RuleListener<? super Ru>>
        implements Rule<P, C, C, C, V, V, Cond, CondL, Cons, ConsL, Ru> {

    private CondL conditions;
    private ConsL satisfiedConsequences;
    private ConsL unsatisfiedConsequences;
    private C addConditionCommand;
    private C addSatisifedConsequenceCommand;
    private C addUnsatisifedConsequenceCommand;

    public ProxyRule(R resources, SR subResources, RuleWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected final void getChildObjects() {
        super.getChildObjects();
        conditions = (CondL)getWrapper(CONDITIONS);
        satisfiedConsequences = (ConsL)getWrapper(SATISFIED_CONSEQUENCES);
        unsatisfiedConsequences = (ConsL)getWrapper(UNSATISFIED_CONSEQUENCES);
        addConditionCommand = (C)getWrapper(ADD_CONDITION);
        addSatisifedConsequenceCommand = (C)getWrapper(ADD_SATISFIED_CONSEQUENCE);
        addUnsatisifedConsequenceCommand = (C)getWrapper(ADD_UNSATISFIED_CONSEQUENCE);
    }

    @Override
    public CondL getConditions() {
        return conditions;
    }

    public ConsL getSatisfiedConsequences() {
        return satisfiedConsequences;
    }

    public ConsL getUnsatisfiedConsequences() {
        return unsatisfiedConsequences;
    }

    public C getAddConditionCommand() {
        return addConditionCommand;
    }

    public C getAddSatisifedConsequenceCommand() {
        return addSatisifedConsequenceCommand;
    }

    public C getAddUnsatisifedConsequenceCommand() {
        return addUnsatisifedConsequenceCommand;
    }
}
