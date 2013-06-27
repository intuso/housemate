package com.intuso.housemate.plugin.api;

import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.api.HousemateException;

/**
 * @param <CONDITION> the type of the conditions created by this factory
 */
public interface BrokerConditionFactory<CONDITION extends BrokerRealCondition> {

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
    public CONDITION create(BrokerRealResources resources, String id, String name, String description) throws HousemateException;
}
