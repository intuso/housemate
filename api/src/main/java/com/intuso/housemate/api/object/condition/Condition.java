package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.property.HasProperties;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <ERROR_VALUE> the type of the error value
 * @param <SATISFIED_VALUE> the type of the satisfied value
 * @param <PROPERTIES> the type of the properties list
 * @param <ADD_COMMAND> the type of the add command
 * @param <CONDITION> the type of the condition
 * @param <CONDITIONS> the type of the conditions list
 */
public interface Condition<
            ERROR_VALUE extends Value<?, ?>,
            SATISFIED_VALUE extends Value<?, ?>,
            PROPERTIES extends List<? extends Property<?, ?, ?>>,
            ADD_COMMAND extends Command<?, ?>,
            CONDITION extends Condition<ERROR_VALUE, SATISFIED_VALUE, PROPERTIES, ADD_COMMAND, CONDITION, CONDITIONS>,
            CONDITIONS extends List<? extends CONDITION>>
        extends BaseHousemateObject<ConditionListener<? super CONDITION>>, HasProperties<PROPERTIES>, HasConditions<CONDITIONS> {

    public final static String SATISFIED_ID = "satisfied";
    public final static String ERROR_ID = "error";
    public final static String PROPERTIES_ID = "properties";
    public final static String CONDITIONS_ID = "conditions";

    /**
     * Gets the add condition command
     * @return the add condition command
     */
    public ADD_COMMAND getAddConditionCommand();

    /**
     * Gets the error value object
     * @return the error value object
     */
    public ERROR_VALUE getErrorValue();

    /**
     * Gets the current error
     * @return the current error
     */
    public String getError();

    /**
     * Gets the satisfied value object
     * @return the satisfied value object
     */
    public SATISFIED_VALUE getSatisfiedValue();

    /**
     * Gets the current satisfied value
     * @return the current satisfied value
     */
    public boolean isSatisfied();
}
