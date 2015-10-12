package com.intuso.housemate.client.real.api.internal.driver;

import java.util.Map;

/**
 * Created by tomc on 30/09/15.
 */
public interface ConditionDriver {

    boolean hasChildConditions();
    void start();
    void stop();

    interface Callback {
        void setError(String error);
        void conditionSatisfied(boolean satisfied);
        Map<String, Boolean> getChildSatisfied();
    }

    interface Factory<DRIVER extends ConditionDriver> {
        DRIVER create(Callback callback);
    }
}
