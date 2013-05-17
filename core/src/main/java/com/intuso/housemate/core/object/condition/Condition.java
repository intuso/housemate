package com.intuso.housemate.core.object.condition;

import com.intuso.housemate.core.object.BaseObject;
import com.intuso.housemate.core.object.command.Command;
import com.intuso.housemate.core.object.list.List;
import com.intuso.housemate.core.object.property.HasProperties;
import com.intuso.housemate.core.object.property.Property;
import com.intuso.housemate.core.object.value.Value;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public interface Condition<SP extends Property<?, ?, ?>, SV extends Value<?, ?>, BV extends Value<?, ?>,
            PL extends List<? extends Property<?, ?, ?>>, AC extends Command<?, ?>,
            C extends Condition<SP, SV, BV, PL, AC, C, CL>, CL extends List<? extends C>>
        extends BaseObject<ConditionListener<? super C>>, HasProperties<PL>, HasConditions<CL> {

    public final static String SATISFIED = "satisfied";
    public final static String ERROR = "error";
    public final static String PROPERTIES = "properties";
    public final static String CONDITIONS = "conditions";
    public final static String ADD_CONDITION = "add-condition";

    public PL getProperties();
    public CL getConditions();
    public AC getAddConditionCommand();
    public SV getErrorValue();
    public String getError();
    public BV getSatisfiedValue();
    public boolean isSatisfied();
}
