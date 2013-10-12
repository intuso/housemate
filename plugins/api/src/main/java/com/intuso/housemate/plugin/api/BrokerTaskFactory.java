package com.intuso.housemate.plugin.api;

import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealTaskOwner;

/**
 * @param <TASK> the type of the tasks created by this factory
 */
public interface BrokerTaskFactory<TASK extends BrokerRealTask> {

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
    public TASK create(BrokerRealResources resources, String id, String name, String description, BrokerRealTaskOwner owner) throws HousemateException;
}
