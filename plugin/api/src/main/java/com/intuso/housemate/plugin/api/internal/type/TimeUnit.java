package com.intuso.housemate.plugin.api.internal.type;

import com.intuso.housemate.plugin.api.internal.annotations.TypeInfo;

/**
 * Enum of time units
 */@TypeInfo(id = "time-unit", name = "Time Unit", description = "Unit of time")
public enum TimeUnit {

    /**
     * Time unit that has a value of one hour
     */
    HOURS {
        @Override
        public long getFactor() {
            return 1000 * 60 * 60;
        }
    },

    /**
     * Time unit that has a value of one minute
     */
    MINUTES {
        @Override
        public long getFactor() {
            return 1000 * 60;
        }
    },

    /**
     * Time unit that has a value of one second
     */
    SECONDS {
        @Override
        public long getFactor() {
            return 1000;
        }
    },

    /**
     * Time unit that has a value of one millisecond
     */
    MILLIS {
        @Override
        public long getFactor() {
            return 1;
        }
    };

    /**
     * Get the factor to times the amount by to get the number of milliseconds to wait
     * @return the factor to times the amount by to get the number of milliseconds to wait
     */
    public abstract long getFactor();
}
