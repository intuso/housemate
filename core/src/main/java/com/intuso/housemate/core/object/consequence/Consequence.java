package com.intuso.housemate.core.object.consequence;

import com.intuso.housemate.core.object.BaseObject;
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
public interface Consequence<SP extends Property<?, ?, ?>, BV extends Value<?, ?>, SV extends Value<?, ?>,
        PL extends List<? extends Property<?, ?, ?>>, C extends Consequence<SP, BV, SV, PL, C>>
        extends BaseObject<ConsequenceListener<? super C>>, HasProperties<PL> {

    public final static String EXECUTING = "executing";
    public final static String ERROR = "error";
    public final static String PROPERTIES = "properties";

    public SV getErrorValue();
    public String getError();
    public BV getExecutingValue();
    public boolean isExecuting();
}
