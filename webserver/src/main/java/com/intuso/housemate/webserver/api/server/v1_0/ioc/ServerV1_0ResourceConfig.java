package com.intuso.housemate.webserver.api.server.v1_0.ioc;

import com.intuso.housemate.webserver.api.server.v1_0.ObjectResourceImpl;
import com.intuso.housemate.webserver.api.server.v1_0.PowerResourceImpl;
import com.intuso.housemate.webserver.api.server.v1_0.SessionResource;
import com.intuso.utilities.webserver.ioc.GuiceHK2BridgedResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * Created by tomc on 21/01/17.
 */
public class ServerV1_0ResourceConfig extends GuiceHK2BridgedResourceConfig {
    public ServerV1_0ResourceConfig() {
        super(ObjectResourceImpl.class,
                PowerResourceImpl.class,
                SessionResource.class);
        register(JacksonFeature.class);
    }
}
