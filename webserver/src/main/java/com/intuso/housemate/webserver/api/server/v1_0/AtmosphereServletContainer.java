package com.intuso.housemate.webserver.api.server.v1_0;

import com.intuso.housemate.webserver.api.server.v1_0.ioc.AtmosphereResourceConfig;
import com.intuso.housemate.webserver.api.server.v1_0.ioc.ServerV1_0ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Created by tomc on 08/05/14.
 */
public class AtmosphereServletContainer extends ServletContainer {
    public AtmosphereServletContainer() {
        super(ServerV1_0ResourceConfig.forApplicationClass(AtmosphereResourceConfig.class));
    }
}
