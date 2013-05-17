package com.intuso.housemate.core.object.rule;

import com.intuso.housemate.core.object.command.Command;
import com.intuso.housemate.core.object.condition.Condition;
import com.intuso.housemate.core.object.condition.HasConditions;
import com.intuso.housemate.core.object.consequence.Consequence;
import com.intuso.housemate.core.object.list.List;
import com.intuso.housemate.core.object.primary.PrimaryObject;
import com.intuso.housemate.core.object.property.Property;
import com.intuso.housemate.core.object.value.Value;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public interface Rule<SP extends Property<?, ?, ?>, RC extends Command<?, ?>, SC extends Command<?, ?>,
            AC extends Command<?, ?>, BV extends Value<?, ?>, SV extends Value<?, ?>,
            Cond extends Condition<?, ?, ?, ?, ?, ?, ?>, CondL extends List<? extends Cond>,
            Cons extends Consequence<?, ?, ?, ?, ?>, ConsL extends List<? extends Cons>,
            R extends Rule<SP, RC, SC, AC, BV, SV, Cond, CondL, Cons, ConsL, R>>
        extends PrimaryObject<SP, RC, SC, BV, SV, R, RuleListener<? super R>>, HasConditions<CondL> {

    public final static String CONDITIONS = "conditions";
    public final static String SATISFIED_CONSEQUENCES = "satisfied-consequences";
    public final static String UNSATISFIED_CONSEQUENCES = "unsatisfied-consequences";
    public final static String ADD_CONDITION = "add-condition";
    public final static String ADD_SATISFIED_CONSEQUENCE = "add-satisfied-consequence";
    public final static String ADD_UNSATISFIED_CONSEQUENCE = "add-unsatisfied-consequence";

    public ConsL getSatisfiedConsequences();
    public ConsL getUnsatisfiedConsequences();
    public AC getAddConditionCommand();
    public AC getAddSatisifedConsequenceCommand();
    public AC getAddUnsatisifedConsequenceCommand();
}
