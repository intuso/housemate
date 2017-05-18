package com.intuso.housemate.webserver.api.server.v1_0.ioc;

import com.intuso.utilities.webserver.ioc.JerseyResourcesModule;

/**
 * Created by tomc on 21/01/17.
 */
public class ServerV1_0Module extends JerseyResourcesModule {
    public ServerV1_0Module() {
        super("/server/1.0/", ServerV1_0ResourceConfig.class);
    }
}
