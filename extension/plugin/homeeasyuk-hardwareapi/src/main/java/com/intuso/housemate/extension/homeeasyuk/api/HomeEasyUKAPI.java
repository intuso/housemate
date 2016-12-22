package com.intuso.housemate.extension.homeeasyuk.api;

import com.intuso.housemate.client.v1_0.api.annotation.*;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 08/12/16.
 */
@HardwareAPI("homeeasyuk-1.0")
public interface HomeEasyUKAPI {

    void initAppliance(int houseId, byte unitCode);
    void uninitAppliance(int houseId, byte unitCode);

    interface Appliance {

        @Value
        @Id("on")
        boolean isOn();

        @Command
        @Id("on")
        void turnOn();

        @Command()
        @Id("off")
        void turnOff();

        @AddListener
        ListenerRegistration addCallback(Listener listener);

        interface Listener {

            @Value
            @Id("on")
            void on(boolean on);
        }
    }
}
