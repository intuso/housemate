package com.intuso.housemate.core.object.primary;

import com.intuso.housemate.core.object.BaseObject;
import com.intuso.housemate.core.object.command.Command;
import com.intuso.housemate.core.object.property.Property;
import com.intuso.housemate.core.object.value.Value;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 28/05/12
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */
public interface PrimaryObject<P extends Property<?, ?, ?>, RC extends Command<?, ?>, SC extends Command<?, ?>,
            BV extends Value<?, ?>, SV extends Value<?, ?>, PO extends PrimaryObject<P, RC, SC, BV, SV, PO, L>,
            L extends PrimaryListener<? super PO>>
        extends BaseObject<L> {

    public final static String REMOVE_COMMAND = "remove";
    public final static String RUNNING_VALUE = "running";
    public final static String START_COMMAND = "start";
    public final static String STOP_COMMAND = "stop";
    public final static String ERROR_VALUE = "error";

    public RC getRemoveCommand();
    public Boolean isRunning();
    public BV getRunningValue();
    public SC getStartCommand();
    public SC getStopCommand();
    public String getError();
    public SV getErrorValue();
}
