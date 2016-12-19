package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Task;

/**
 * Created by tomc on 02/12/16.
 */
public class TaskMapper implements ObjectMapper<Task.Data, com.intuso.housemate.client.api.internal.object.Task.Data> {

    @Override
    public Task.Data map(com.intuso.housemate.client.api.internal.object.Task.Data data) {
        if(data == null)
            return null;
        return new Task.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Task.Data map(Task.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Task.Data(data.getId(), data.getName(), data.getDescription());
    }
}
