package com.intuso.housemate.webserver.api.server.v1_0.ioc;

import com.intuso.housemate.webserver.api.server.v1_0.ListenerSocketImpl;
import com.intuso.housemate.webserver.api.server.v1_0.json.GsonFeature;
import com.intuso.utilities.webserver.ioc.GuiceHK2BridgedResourceConfig;

/**
 * Created by tomc on 08/05/14.
 */
public class AtmosphereResourceConfig extends GuiceHK2BridgedResourceConfig {
    public AtmosphereResourceConfig() {
        register(ListenerSocketImpl.class);
        register(GsonFeature.class);
    }
}