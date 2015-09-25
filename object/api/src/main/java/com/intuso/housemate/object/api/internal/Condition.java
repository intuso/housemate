package com.intuso.housemate.object.api.internal;

/**
 * @param <ERROR_VALUE> the type of the error value
 * @param <SATISFIED_VALUE> the type of the satisfied value
 * @param <PROPERTIES> the type of the properties list
 * @param <ADD_COMMAND> the type of the add command
 * @param <CONDITION> the type of the condition
 * @param <CONDITIONS> the type of the conditions list
 */
public interface Condition<
            REMOVE_COMMAND extends Command<?, ?, ?, ?>,
            ERROR_VALUE extends Value<?, ?>,
            SATISFIED_VALUE extends Value<?, ?>,
            PROPERTIES extends List<? extends Property<?, ?, ?>>,
            ADD_COMMAND extends Command<?, ?, ?, ?>,
            CONDITION extends Condition<REMOVE_COMMAND, ERROR_VALUE, SATISFIED_VALUE, PROPERTIES, ADD_COMMAND, CONDITION, CONDITIONS>,
            CONDITIONS extends List<? extends CONDITION>>
        extends BaseHousemateObject<Condition.Listener<? super CONDITION>>,
            Property.Container<PROPERTIES>,
            Removeable<REMOVE_COMMAND>,
            Failable<ERROR_VALUE> {

    CONDITIONS getConditions();

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
    interface Listener<CONDITION extends Condition<?, ?, ?, ?, ?, ?, ?>> extends ObjectListener {

        /**
         * Notifies that a condition has become (un)satisfied
         * @param condition the now (un)satisfied condition
         * @param satisfied true if the condition is now satisfied
         */
        void conditionSatisfied(CONDITION condition, boolean satisfied);

        /**
         * Notifies that a condition is in error (or not)
         * @param condition the condition that is in error (or not)
         * @param error description of the error or null if not in error
         */
        void conditionError(CONDITION condition, String error);
    }

    /**
     *
     * Interface to show that the implementing object has a list of conditions
     */
    interface Container<CONDITIONS extends List<? extends Condition<?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the conditions list
         * @return the conditions list
         */
        CONDITIONS getConditions();
    }
}
