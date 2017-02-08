package com.intuso.housemate.client.api.internal.driver;

import org.slf4j.Logger;

/**
 * Created by tomc on 30/09/15.
 */
public interface HardwareDriver {

    void init(Logger logger, HardwareDriver.Callback callback);
    void uninit();
    void foundDeviceId(String deviceId);

    interface Callback {
        void setError(String error);
        void addDevice(String id, String name, String description, Object object);
        void removeDevice(Object object);
    }

    interface Factory<DRIVER extends HardwareDriver> {
        DRIVER create(Logger logger, Callback callback);
    }

    /**
     * Created by tomc on 30/08/16.
     */
    class HardwareException extends RuntimeException {
        private static final long serialVersionUID = -1L;

        public HardwareException() {}

        public HardwareException(String s) {
            super(s);
        }

        public HardwareException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public HardwareException(Throwable throwable) {
            super(throwable);
        }
    }
}
