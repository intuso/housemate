package com.intuso.housemate.object.api.internal;

/**
 * @param <ERROR_VALUE> the type of the error value
 * @param <SATISFIED_VALUE> the type of the satisfied value
 * @param <PROPERTIES> the type of the properties list
 * @param <ADD_COMMAND> the type of the add command
 * @param <CONDITION> the type of the condition
 * @param <CHILD_CONDITIONS> the type of the conditions list
 */
public interface Condition<REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        ERROR_VALUE extends Value<?, ?>,
        DRIVER_PROPERTY extends Property<?, ?, ?>,
        DRIVER_LOADED_VALUE extends Value<?, ?>,
        SATISFIED_VALUE extends Value<?, ?>,
        PROPERTIES extends List<? extends Property<?, ?, ?>>,
        ADD_COMMAND extends Command<?, ?, ?, ?>,
        CHILD_CONDITION extends Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
        CHILD_CONDITIONS extends List<? extends CHILD_CONDITION>,
        CONDITION extends Condition<REMOVE_COMMAND, ERROR_VALUE, DRIVER_PROPERTY, DRIVER_LOADED_VALUE, SATISFIED_VALUE, PROPERTIES, ADD_COMMAND, CHILD_CONDITION, CHILD_CONDITIONS, CONDITION>>
        extends BaseHousemateObject<Condition.Listener<? super CONDITION>>,
        Property.Container<PROPERTIES>,
        Removeable<REMOVE_COMMAND>,
        Failable<ERROR_VALUE>,
        UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE> {

    CHILD_CONDITIONS getConditions();

    /**
     * Gets the add condition command
     * @return the add condition command
     */
    ADD_COMMAND getAddConditionCommand();

    /**
     * Gets the satisfied value object
     * @return the satisfied value object
     */
    SATISFIED_VALUE getSatisfiedValue();

    /**
     *
     * Listener interface for options
     */
    interface Listener<CONDITION extends Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> extends ObjectListener,
            Failable.Listener<CONDITION>,
            UsesDriver.Listener<CONDITION> {

        /**
         * Notifies that a condition has become (un)satisfied
         * @param condition the now (un)satisfied condition
         * @param satisfied true if the condition is now satisfied
         */
        void conditionSatisfied(CONDITION condition, boolean satisfied);
    }

    /**
     *
     * Interface to show that the implementing object has a list of conditions
     */
    interface Container<CONDITIONS extends List<? extends Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the conditions list
         * @return the conditions list
         */
        CONDITIONS getConditions();
    }
}
