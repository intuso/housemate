package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.sample.plugin.CustomArg;
import com.intuso.utilities.log.Log;

/**
 * Example task with a non-standard constructor that cannot be used with the
 * {@link com.intuso.housemate.annotations.plugin.Tasks} annotation so requires a factory
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginModule
 * @see ComplexTaskFactory
 */
public class ComplexTask extends ServerRealTask {

    private final CustomArg customArg;

    public ComplexTask(Log log, String id, String name, String description,
                       ServerRealTaskOwner owner, CustomArg customArg) {
        super(log, id, name, description, owner);
        this.customArg = customArg;
    }

    @Override
    public void execute() {}
}
