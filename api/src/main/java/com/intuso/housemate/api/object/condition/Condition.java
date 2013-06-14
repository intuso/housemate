package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.property.HasProperties;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.value.Value;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public interface Condition<SV extends Value<?, ?>, BV extends Value<?, ?>,
            PL extends List<? extends Property<?, ?, ?>>, AC extends Command<?, ?>,
            C extends Condition<SV, BV, PL, AC, C, CL>, CL extends List<? extends C>>
        extends BaseObject<ConditionListener<? super C>>, HasProperties<PL>, HasConditions<CL> {

    public final static String SATISFIED = "satisfied";
    public final static String ERROR = "error";
    public final static String PROPERTIES = "properties";
    public final static String CONDITIONS = "conditions";

    public PL getProperties();
    public CL getConditions();
    public AC getAddConditionCommand();
    public SV getErrorValue();
    public String getError();
    public BV getSatisfiedValue();
    public boolean isSatisfied();
}
