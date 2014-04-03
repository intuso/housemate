package com.intuso.housemate.api.object.application;

import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.application.instance.HasApplicationInstances;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <APPLICATION_INSTANCE> the type of the application instances
 * @param <APPLICATION_INSTANCES> the type of the application instances list
 * @param <A> the type of the application
 */
public interface Application<
            VALUE extends Value<?, ?>,
            COMMAND extends Command<?, ?, ?>,
            APPLICATION_INSTANCE extends ApplicationInstance<?, ?, ?>,
            APPLICATION_INSTANCES extends List<? extends ApplicationInstance<?, ?, ?>>,
            A extends Application<VALUE, COMMAND, APPLICATION_INSTANCE, APPLICATION_INSTANCES, A>>
        extends BaseHousemateObject<ApplicationListener<? super A>>, HasApplicationInstances<APPLICATION_INSTANCES> {

    public final static String APPLICATION_INSTANCES_ID = "instances";
    public final static String ALLOW_COMMAND_ID = "allow";
    public final static String SOME_COMMAND_ID = "some";
    public final static String REJECT_COMMAND_ID = "reject";
    public final static String STATUS_VALUE_ID = "status";

    public COMMAND getAllowCommand();
    public COMMAND getSomeCommand();
    public COMMAND getRejectCommand();
    public ApplicationStatus getStatus();
    public VALUE getStatusValue();
}
