package com.intuso.housemate.client.api.internal.driver;

import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by tomc on 30/09/15.
 */
public interface ConditionDriver {

    boolean hasChildConditions();
    void startCondition();
    void stopCondition();

    interface Callback {
        void setError(String error);
        void conditionSatisfied(boolean satisfied);
        Map<String, Boolean> getChildSatisfied();
    }

    interface Factory<DRIVER extends ConditionDriver> {
        DRIVER create(Logger logger, Callback callback);
    }

    /**
     * Created by tomc on 30/08/16.
     */
    class ConditionException extends RuntimeException {
        private static final long serialVersionUID = -1L;

        public ConditionException() {}

        public ConditionException(String s) {
            super(s);
        }

        public ConditionException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public ConditionException(Throwable throwable) {
            super(throwable);
        }
    }
}
