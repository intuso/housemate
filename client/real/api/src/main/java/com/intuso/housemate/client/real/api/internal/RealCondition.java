package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.object.api.internal.Condition;

public interface RealCondition<DRIVER extends ConditionDriver>
        extends Condition<RealCommand,
        RealValue<String>,
        RealProperty<PluginResource<ConditionDriver.Factory<DRIVER>>>,
        RealValue<Boolean>,
        RealValue<Boolean>,
        RealList<RealProperty<?>>,
        RealCommand,
        RealCondition<?>,
        RealList<RealCondition<?>>,
        RealCondition<DRIVER>>,
        Condition.Listener<RealCondition<?>>,ConditionDriver.Callback {

    DRIVER getDriver();

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

    interface Container extends Condition.Container<RealList<RealCondition<?>>>, RemoveCallback {
        <DRIVER extends ConditionDriver> RealCondition<DRIVER> createAndAddCondition(ConditionData data);
        void addCondition(RealCondition<?> condition);
    }

    interface RemoveCallback {
        void removeCondition(RealCondition<?> condition);
    }

    interface Factory {
        RealCondition<?> create(ConditionData data, RemoveCallback removeCallback);
    }
}
