package com.intuso.housemate.client.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.api.object.Command;

/**
 * Created by tomc on 02/12/16.
 */
public class CommandMapper implements ObjectMapper<Command.Data, com.intuso.housemate.client.api.internal.object.Command.Data> {

    @Override
    public Command.Data map(com.intuso.housemate.client.api.internal.object.Command.Data data) {
        if(data == null)
            return null;
        return new Command.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Command.Data map(Command.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Command.Data(data.getId(), data.getName(), data.getDescription());
    }
}
