package com.intuso.housemate.api.object.consequence;

import com.intuso.housemate.api.object.BaseObject;
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
public interface Consequence<BV extends Value<?, ?>, SV extends Value<?, ?>,
        PL extends List<? extends Property<?, ?, ?>>, C extends Consequence<BV, SV, PL, C>>
        extends BaseObject<ConsequenceListener<? super C>>, HasProperties<PL> {

    public final static String EXECUTING = "executing";
    public final static String ERROR = "error";
    public final static String PROPERTIES = "properties";

    public SV getErrorValue();
    public String getError();
    public BV getExecutingValue();
    public boolean isExecuting();
}
