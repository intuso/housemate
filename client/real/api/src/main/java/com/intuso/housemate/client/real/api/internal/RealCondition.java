package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.object.Condition;

public interface RealCondition<COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        DRIVER_PROPERTY extends RealProperty<PluginDependency<ConditionDriver.Factory<?>>, ?, ?, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        CHILD_CONDITION extends RealCondition<?, ?, ?, ?, ?, ?, ?, ?>,
        CHILD_CONDITIONS extends RealList<? extends CHILD_CONDITION, ?>,
        CONDITION extends RealCondition<COMMAND, BOOLEAN_VALUE, STRING_VALUE, DRIVER_PROPERTY, PROPERTIES, CHILD_CONDITION, CHILD_CONDITIONS, CONDITION>>
        extends Condition<COMMAND,
        COMMAND,
        STRING_VALUE,
        DRIVER_PROPERTY,
        BOOLEAN_VALUE,
        BOOLEAN_VALUE,
        PROPERTIES,
        COMMAND,
        CHILD_CONDITIONS,
        CONDITION>,
        Condition.Listener<CHILD_CONDITION>,
        ConditionDriver.Callback {

    <DRIVER extends ConditionDriver> DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isSatisfied();

    /**
     * Sets the error message for the object
     * @param error
     */
    void setError(String error);

    /**
     * Updates the satisfied value of the condition. If different, it will propagate to the parent. If It affects the
     * parent's satisfied value then it will propagate again until either it does not affect a parent condition or it
     * gets to the automation, in which case the tasks for the new value will be executed
     * @param satisfied
     */
    void conditionSatisfied(boolean satisfied);

    void start();

    void stop();

    interface Container<CONDITION extends RealCondition<?, ?, ?, ?, ?, ?, ?, ?>, CONDITIONS extends RealList<? extends CONDITION, ?>> extends Condition.Container<CONDITIONS>, RemoveCallback {
        void addCondition(CONDITION condition);
    }

    interface RemoveCallback<CONDITION extends RealCondition<?, ?, ?, ?, ?, ?, ?, ?>> {
        void removeCondition(CONDITION condition);
    }
}
