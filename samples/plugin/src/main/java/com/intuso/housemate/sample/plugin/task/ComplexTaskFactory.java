package com.intuso.housemate.sample.plugin.task;

import com.google.inject.Inject;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.plugin.api.ServerTaskFactory;
import com.intuso.housemate.sample.plugin.CustomArg;
import com.intuso.utilities.log.Log;

/**
 * Example factory for tasks with a non-standard constructor.
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginModule
 * @see ComplexTask
 */
public class ComplexTaskFactory implements ServerTaskFactory<ComplexTask> {

    private final CustomArg customArg;

    @Inject
    public ComplexTaskFactory(CustomArg customArg) {
        this.customArg = customArg;
    }

    @Override
    public String getTypeId() {
        return "really-complex-task-factory";
    }

    @Override
    public String getTypeName() {
        return "Really Complex Task Factory";
    }

    @Override
    public String getTypeDescription() {
        return "A complex factory for complex tasks";
    }

    @Override
    public ComplexTask create(Log log, String id, String name, String description,
                              ServerRealTaskOwner owner) {
        return new ComplexTask(log, id, name, description, owner, customArg);
    }
}
