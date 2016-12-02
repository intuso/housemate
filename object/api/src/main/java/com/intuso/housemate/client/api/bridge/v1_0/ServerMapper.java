package com.intuso.housemate.client.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.api.object.Server;

/**
 * Created by tomc on 02/12/16.
 */
public class ServerMapper  implements ObjectMapper<Server.Data, com.intuso.housemate.client.api.internal.object.Server.Data>{

    @Override
    public Server.Data map(com.intuso.housemate.client.api.internal.object.Server.Data data) {
        if(data == null)
            return null;
        return new Server.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Server.Data map(Server.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Server.Data(data.getId(), data.getName(), data.getDescription());
    }
}
