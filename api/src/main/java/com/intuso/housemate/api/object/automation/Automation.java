package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.HasConditions;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.value.Value;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public interface Automation<SP extends Property<?, ?, ?>,
            RC extends Command<?, ?>,
            SC extends Command<?, ?>,
            AC extends Command<?, ?>,
            CV extends Value<?, ?>,
            RV extends Value<?, ?>,
            SV extends Value<?, ?>,
            C extends Condition<?, ?, ?, ?, ?, ?>,
            CL extends List<? extends C>,
            T extends Task<?, ?, ?, ?>,
            TL extends List<? extends T>,
            A extends Automation<SP, RC, SC, AC, CV, RV, SV, C, CL, T, TL, A>>
        extends PrimaryObject<SP, RC, SC, CV, RV, SV, A, AutomationListener<? super A>>, HasConditions<CL> {

    public final static String CONDITIONS = "conditions";
    public final static String SATISFIED_TASKS = "satisfied-tasks";
    public final static String UNSATISFIED_TASKS = "unsatisfied-tasks";
    public final static String ADD_CONDITION = "add-condition";
    public final static String ADD_SATISFIED_TASK = "add-satisfied-task";
    public final static String ADD_UNSATISFIED_TASK = "add-unsatisfied-task";

    public TL getSatisfiedTasks();
    public TL getUnsatisfiedTasks();
    public AC getAddConditionCommand();
    public AC getAddSatisifedTaskCommand();
    public AC getAddUnsatisifedTaskCommand();
}
