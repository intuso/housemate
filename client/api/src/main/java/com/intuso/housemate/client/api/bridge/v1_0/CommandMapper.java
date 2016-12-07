package com.intuso.housemate.client.api.bridge.v1_0;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Command;

/**
 * Created by tomc on 02/12/16.
 */
public class CommandMapper implements ObjectMapper<Command.Data, com.intuso.housemate.client.api.internal.object.Command.Data> {

    private final TypeInstanceMapMapper typeInstanceMapMapper;

    @Inject
    public CommandMapper(TypeInstanceMapMapper typeInstanceMapMapper) {
        this.typeInstanceMapMapper = typeInstanceMapMapper;
    }

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

    public Command.PerformData map(com.intuso.housemate.client.api.internal.object.Command.PerformData performData) {
        if(performData == null)
            return null;
        return new Command.PerformData(performData.getOpId(), typeInstanceMapMapper.map(performData.getInstanceMap()));
    }

    public com.intuso.housemate.client.api.internal.object.Command.PerformData map(Command.PerformData performData) {
        if(performData == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Command.PerformData(performData.getOpId(), typeInstanceMapMapper.map(performData.getInstanceMap()));
    }

    public Command.PerformStatusData map(com.intuso.housemate.client.api.internal.object.Command.PerformStatusData performStatusData) {
        if(performStatusData == null)
            return null;
        return new Command.PerformStatusData(performStatusData.getOpId(), performStatusData.isFinished(), performStatusData.getError());
    }

    public com.intuso.housemate.client.api.internal.object.Command.PerformStatusData map(Command.PerformStatusData performStatusData) {
        if(performStatusData == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Command.PerformStatusData(performStatusData.getOpId(), performStatusData.isFinished(), performStatusData.getError());
    }
}
