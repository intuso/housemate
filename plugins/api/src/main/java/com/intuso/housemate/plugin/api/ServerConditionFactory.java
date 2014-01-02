package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.server.real.ServerRealResources;

/**
 * @param <CONDITION> the type of the conditions created by this factory
 */
public interface ServerConditionFactory<CONDITION extends ServerRealCondition> {

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
     * Creates a condition
     * @param resources the resources for the condition
     * @param id the condition's id
     * @param name the condition's name
     * @param description the condition's description
     * @return a new condition
     * @throws HousemateException if the condition cannot be created
     */
    public CONDITION create(ServerRealResources resources, String id, String name, String description,
                            ServerRealConditionOwner owner) throws HousemateException;
}
