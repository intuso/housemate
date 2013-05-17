package com.intuso.housemate.core.object.device;

import com.intuso.housemate.core.object.command.Command;
import com.intuso.housemate.core.object.command.HasCommands;
import com.intuso.housemate.core.object.list.List;
import com.intuso.housemate.core.object.primary.PrimaryObject;
import com.intuso.housemate.core.object.property.HasProperties;
import com.intuso.housemate.core.object.property.Property;
import com.intuso.housemate.core.object.value.HasValues;
import com.intuso.housemate.core.object.value.Value;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public interface Device<RC extends Command<?, ?>, SC extends Command<?, ?>, C extends Command<?, ?>,
            CL extends List<? extends C>, BV extends Value<?, ?>, SV extends Value<?, ?>, V extends Value<?, ?>,
            VL extends List<? extends V>, SP extends Property<?, ?, ?>, P extends Property<?, ?, ?>,
            PL extends List<? extends P>, D extends Device<RC, SC, C, CL, BV, SV, V, VL, SP, P, PL, D>>
        extends PrimaryObject<SP, RC, SC, BV, SV, D, DeviceListener<? super D>>, HasCommands<CL>, HasValues<VL>, HasProperties<PL> {

    public final static String COMMANDS = "commands";
    public final static String VALUES = "values";
    public final static String PROPERTIES = "properties";
}
