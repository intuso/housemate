package com.intuso.housemate.extension.homeeasyuk.api;

import com.intuso.housemate.client.v1_0.api.annotation.*;
import com.intuso.utilities.listener.MemberRegistration;

/**
 * Created by tomc on 08/12/16.
 */
public interface HomeEasyUKAPI {

    @Command
    @Id("init-homeeasy-appliance")
    void initAppliance(@Parameter @Id("house-id") int houseId, @Parameter @Id("unit-code") byte unitCode);

    @Command
    @Id("uninit-homeeasy-appliance")
    void uninitAppliance(@Parameter @Id("house-id") int houseId, @Parameter @Id("unit-code") byte unitCode);

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
        MemberRegistration addCallback(Listener listener);

        interface Listener {

            @Value
            @Id("on")
            void on(boolean on);
        }
    }
}
