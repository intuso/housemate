package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Automation;

/**
 * Created by tomc on 02/12/16.
 */
public class AutomationMapper implements ObjectMapper<Automation.Data, com.intuso.housemate.client.api.internal.object.Automation.Data> {

    @Override
    public Automation.Data map(com.intuso.housemate.client.api.internal.object.Automation.Data data) {
        if(data == null)
            return null;
        return new Automation.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Automation.Data map(Automation.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Automation.Data(data.getId(), data.getName(), data.getDescription());
    }
}
