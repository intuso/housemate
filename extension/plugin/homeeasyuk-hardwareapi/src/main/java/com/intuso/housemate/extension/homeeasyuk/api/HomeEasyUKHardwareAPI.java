package com.intuso.housemate.extension.homeeasyuk.api;

import com.intuso.housemate.client.v1_0.real.api.annotations.HardwareAPI;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 08/12/16.
 */
@HardwareAPI("homeeasyuk-1.0")
public interface HomeEasyUKHardwareAPI {

    Appliance appliance(int houseId, byte unitId);
    
    interface Appliance {

        boolean isOn();

        void turnOn();

        void turnOff();
        ListenerRegistration listen(Listener listener);

        interface Listener extends com.intuso.utilities.listener.Listener {
            void on(boolean isOn);
        }
    }
}
