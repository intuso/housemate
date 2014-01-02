package com.intuso.housemate.plugin.api;

import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;

/**
 * @param <TASK> the type of the tasks created by this factory
 */
public interface ServerTaskFactory<TASK extends ServerRealTask> {

    /**
     * Gets the id for this factory
     * @return the id for this factory
     */
    public String getTypeId();

    /**
     * Gets the name for this factory
     * @return the name for this factory
     */
    public String getTypeName();

    /**
     * Gets the description for this factory
     * @return the description for this factory
     */
    public String getTypeDescription();

    /**
     * Creates a task
     * @param resources the resources for the task
     * @param id the task's id
     * @param name the task's name
     * @param description the task's description
     * @return a new task
     * @throws HousemateException if the condition cannot be created
     */
    public TASK create(ServerRealResources resources, String id, String name, String description, ServerRealTaskOwner owner) throws HousemateException;
}
