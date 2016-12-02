package com.intuso.housemate.client.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.api.object.Node;

/**
 * Created by tomc on 02/12/16.
 */
public class NodeMapper implements ObjectMapper<Node.Data, com.intuso.housemate.client.api.internal.object.Node.Data> {

    @Override
    public Node.Data map(com.intuso.housemate.client.api.internal.object.Node.Data data) {
        if(data == null)
            return null;
        return new Node.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Node.Data map(Node.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Node.Data(data.getId(), data.getName(), data.getDescription());
    }
}
