package com.intuso.housemate.client.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.api.object.User;

/**
 * Created by tomc on 02/12/16.
 */
public class UserMapper implements ObjectMapper<User.Data, com.intuso.housemate.client.api.internal.object.User.Data> {

    @Override
    public User.Data map(com.intuso.housemate.client.api.internal.object.User.Data data) {
        if(data == null)
            return null;
        return new User.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.User.Data map(User.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.User.Data(data.getId(), data.getName(), data.getDescription());
    }
}
