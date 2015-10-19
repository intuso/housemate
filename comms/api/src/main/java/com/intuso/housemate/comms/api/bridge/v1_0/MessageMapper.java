package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.comms.api.internal.*;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.bridge.v1_0.*;

/**
 * Created by tomc on 28/09/15.
 */
public class MessageMapper {

    private final ApplicationDetailsMapper applicationDetailsMapper;
    private final ServerConnectionStatusMapper serverConnectionStatusMapper;
    private final ApplicationStatusMapper applicationStatusMapper;
    private final ApplicationInstanceStatusMapper applicationInstanceStatusMapper;
    private final ChildOverviewMapper childOverviewMapper;
    private final DataMapper dataMapper;
    private final TreeDataMapper treeDataMapper;
    private final TreeLoadInfoMapper treeLoadInfoMapper;
    private final TypeInstanceMapper typeInstanceMapper;
    private final TypeInstancesMapper typeInstancesMapper;
    private final TypeInstanceMapMapper typeInstanceMapMapper;

    @Inject
    public MessageMapper(ApplicationDetailsMapper applicationDetailsMapper, ServerConnectionStatusMapper serverConnectionStatusMapper, ApplicationStatusMapper applicationStatusMapper, ApplicationInstanceStatusMapper applicationInstanceStatusMapper, ChildOverviewMapper childOverviewMapper, DataMapper dataMapper, TreeDataMapper treeDataMapper, TreeLoadInfoMapper treeLoadInfoMapper, TypeInstanceMapper typeInstanceMapper, TypeInstancesMapper typeInstancesMapper, TypeInstanceMapMapper typeInstanceMapMapper) {
        this.applicationDetailsMapper = applicationDetailsMapper;
        this.serverConnectionStatusMapper = serverConnectionStatusMapper;
        this.applicationStatusMapper = applicationStatusMapper;
        this.applicationInstanceStatusMapper = applicationInstanceStatusMapper;
        this.childOverviewMapper = childOverviewMapper;
        this.dataMapper = dataMapper;
        this.treeDataMapper = treeDataMapper;
        this.treeLoadInfoMapper = treeLoadInfoMapper;
        this.typeInstanceMapper = typeInstanceMapper;
        this.typeInstancesMapper = typeInstancesMapper;
        this.typeInstanceMapMapper = typeInstanceMapMapper;
    }

    public <PAYLOAD extends Message.Payload> Message<PAYLOAD> map(com.intuso.housemate.comms.v1_0.api.Message<?> message) {
        if(message == null)
            return null;
        return new Message<>(message.getSequenceId(), message.getPath(), message.getType(), this.<PAYLOAD>map(message.getPayload()), Lists.newArrayList(message.getRoute()));
    }

    public <PAYLOAD extends com.intuso.housemate.comms.v1_0.api.Message.Payload> com.intuso.housemate.comms.v1_0.api.Message<PAYLOAD> map(Message<?> message) {
        if(message == null)
            return null;
        return new com.intuso.housemate.comms.v1_0.api.Message<>(message.getSequenceId(), message.getPath(), message.getType(), this.<PAYLOAD>map(message.getPayload()), message.getRoute());
    }

    public <PAYLOAD extends Message.Payload> PAYLOAD map(com.intuso.housemate.comms.v1_0.api.Message.Payload payload) {
        if(payload == null)
            return null;
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.Message.ReceivedPayload)
            return (PAYLOAD) new Message.ReceivedPayload(((com.intuso.housemate.comms.v1_0.api.Message.ReceivedPayload) payload).getSequenceId());
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.HousemateData)
            return (PAYLOAD) dataMapper.map((com.intuso.housemate.comms.v1_0.api.payload.HousemateData<?>) payload);
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.NoPayload)
            return (PAYLOAD) NoPayload.INSTANCE;
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.StringPayload)
            return (PAYLOAD) new StringPayload(((com.intuso.housemate.comms.v1_0.api.payload.StringPayload) payload).getValue());
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration) {
            com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration applicationRegistration = (com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration) payload;
            return (PAYLOAD) new ApplicationRegistration(
                    applicationDetailsMapper.map(applicationRegistration.getApplicationDetails()),
                    applicationRegistration.getApplicationInstanceId(),
                    applicationRegistration.getComponent(),
                    ApplicationRegistration.ClientType.valueOf(applicationRegistration.getType().name()));
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.ChildOverview)
            return (PAYLOAD) childOverviewMapper.map((com.intuso.housemate.comms.v1_0.api.ChildOverview) payload);
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.ChildOverviews) {
            com.intuso.housemate.comms.v1_0.api.ChildOverviews childOverviews = (com.intuso.housemate.comms.v1_0.api.ChildOverviews) payload;
            return (PAYLOAD) new ChildOverviews(Lists.newArrayList(Lists.transform(childOverviews.getChildOverviews(), childOverviewMapper.getFromV1_0Function())),
                    childOverviews.getError());
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.LoadRequest) {
            com.intuso.housemate.comms.v1_0.api.LoadRequest loadRequest = (com.intuso.housemate.comms.v1_0.api.LoadRequest) payload;
            return (PAYLOAD) new LoadRequest(loadRequest.getLoaderId(), Lists.newArrayList(Lists.transform(loadRequest.getTreeLoadInfos(), treeLoadInfoMapper.getFromV1_0Function())));
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.LoadFinished) {
            com.intuso.housemate.comms.v1_0.api.LoadFinished loadFinished = (com.intuso.housemate.comms.v1_0.api.LoadFinished) payload;
            return (PAYLOAD) new LoadFinished(loadFinished.getLoaderId(), loadFinished.getErrors());
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.CommandData.PerformPayload) {
            com.intuso.housemate.comms.v1_0.api.payload.CommandData.PerformPayload performPayload = (com.intuso.housemate.comms.v1_0.api.payload.CommandData.PerformPayload) payload;
            return (PAYLOAD) new CommandData.PerformPayload(performPayload.getOpId(), typeInstanceMapMapper.map(performPayload.getValues()));
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.CommandData.PerformingPayload) {
            com.intuso.housemate.comms.v1_0.api.payload.CommandData.PerformingPayload performingPayload = (com.intuso.housemate.comms.v1_0.api.payload.CommandData.PerformingPayload) payload;
            return (PAYLOAD) new CommandData.PerformingPayload(performingPayload.getOpId(), performingPayload.isPerforming());
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.CommandData.FailedPayload) {
            com.intuso.housemate.comms.v1_0.api.payload.CommandData.FailedPayload failedPayload = (com.intuso.housemate.comms.v1_0.api.payload.CommandData.FailedPayload) payload;
            return (PAYLOAD) new CommandData.FailedPayload(failedPayload.getOpId(), failedPayload.getCause());
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus)
            return (PAYLOAD) serverConnectionStatusMapper.map((com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus) payload);
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.ApplicationData.StatusPayload)
            return (PAYLOAD) new ApplicationData.StatusPayload(applicationStatusMapper.map(((com.intuso.housemate.comms.v1_0.api.payload.ApplicationData.StatusPayload) payload).getStatus()));
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData.StatusPayload)
            return (PAYLOAD) new ApplicationInstanceData.StatusPayload(applicationInstanceStatusMapper.map(((com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData.StatusPayload) payload).getStatus()));
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.TreeLoadInfo)
            return (PAYLOAD) treeLoadInfoMapper.map((com.intuso.housemate.comms.v1_0.api.TreeLoadInfo) payload);
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.TreeData)
            return (PAYLOAD) treeDataMapper.map((com.intuso.housemate.comms.v1_0.api.TreeData) payload);
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstancePayload)
            return (PAYLOAD) new TypeData.TypeInstancePayload(typeInstanceMapper.map(((com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstancePayload) payload).getTypeInstance()));
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstancesPayload)
            return (PAYLOAD) new TypeData.TypeInstancesPayload(typeInstancesMapper.map(((com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstancesPayload) payload).getTypeInstances()));
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstanceMapPayload)
            return (PAYLOAD) new TypeData.TypeInstanceMapPayload(typeInstanceMapMapper.map(((com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstanceMapPayload) payload).getTypeInstanceMap()));
        throw new HousemateCommsException("Unknown message payload type " + payload.getClass().getName());
    }

    public <PAYLOAD extends com.intuso.housemate.comms.v1_0.api.Message.Payload> PAYLOAD map(Message.Payload payload) {
        if(payload == null)
            return null;
        else if(payload instanceof Message.ReceivedPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.Message.ReceivedPayload(((Message.ReceivedPayload) payload).getSequenceId());
        else if(payload instanceof HousemateData)
            return (PAYLOAD) dataMapper.map((HousemateData<?>) payload);
        else if(payload instanceof NoPayload)
            return (PAYLOAD) com.intuso.housemate.comms.v1_0.api.payload.NoPayload.INSTANCE;
        else if(payload instanceof StringPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.StringPayload(((StringPayload) payload).getValue());
        else if(payload instanceof ApplicationRegistration) {
            ApplicationRegistration applicationRegistration = (ApplicationRegistration) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration(
                    applicationDetailsMapper.map(applicationRegistration.getApplicationDetails()),
                    applicationRegistration.getApplicationInstanceId(),
                    applicationRegistration.getComponent(),
                    com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration.ClientType.valueOf(applicationRegistration.getType().name()));
        } else if(payload instanceof ChildOverview)
            return (PAYLOAD) childOverviewMapper.map((ChildOverview) payload);
        else if(payload instanceof ChildOverviews) {
            ChildOverviews childOverviews = (ChildOverviews) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.ChildOverviews(Lists.newArrayList(Lists.transform(childOverviews.getChildOverviews(), childOverviewMapper.getToV1_0Function())),
                    childOverviews.getError());
        } else if(payload instanceof LoadRequest) {
            LoadRequest loadRequest = (LoadRequest) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.LoadRequest(loadRequest.getLoaderId(), Lists.newArrayList(Lists.transform(loadRequest.getTreeLoadInfos(), treeLoadInfoMapper.getToV1_0Function())));
        } else if(payload instanceof LoadFinished) {
            LoadFinished loadFinished = (LoadFinished) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.LoadFinished(loadFinished.getLoaderId(), loadFinished.getErrors());
        } else if(payload instanceof CommandData.PerformPayload) {
            CommandData.PerformPayload performPayload = (CommandData.PerformPayload) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.CommandData.PerformPayload(performPayload.getOpId(), typeInstanceMapMapper.map(performPayload.getValues()));
        } else if(payload instanceof CommandData.PerformingPayload) {
            CommandData.PerformingPayload performingPayload = (CommandData.PerformingPayload) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.CommandData.PerformingPayload(performingPayload.getOpId(), performingPayload.isPerforming());
        } else if(payload instanceof CommandData.FailedPayload) {
            CommandData.FailedPayload failedPayload = (CommandData.FailedPayload) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.CommandData.FailedPayload(failedPayload.getOpId(), failedPayload.getCause());
        } else if(payload instanceof ServerConnectionStatus)
            return (PAYLOAD) serverConnectionStatusMapper.map((ServerConnectionStatus) payload);
        else if(payload instanceof ApplicationData.StatusPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.ApplicationData.StatusPayload(applicationStatusMapper.map(((ApplicationData.StatusPayload) payload).getStatus()));
        else if(payload instanceof ApplicationInstanceData.StatusPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData.StatusPayload(applicationInstanceStatusMapper.map(((ApplicationInstanceData.StatusPayload) payload).getStatus()));
        else if(payload instanceof TreeLoadInfo)
            return (PAYLOAD) treeLoadInfoMapper.map((TreeLoadInfo) payload);
        else if(payload instanceof TreeData)
            return (PAYLOAD) treeDataMapper.map((TreeData) payload);
        else if(payload instanceof TypeData.TypeInstancePayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstancePayload(typeInstanceMapper.map(((TypeData.TypeInstancePayload) payload).getTypeInstance()));
        else if(payload instanceof TypeData.TypeInstancesPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstancesPayload(typeInstancesMapper.map(((TypeData.TypeInstancesPayload) payload).getTypeInstances()));
        else if(payload instanceof TypeData.TypeInstanceMapPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstanceMapPayload(typeInstanceMapMapper.map(((TypeData.TypeInstanceMapPayload) payload).getTypeInstanceMap()));
        throw new com.intuso.housemate.comms.v1_0.api.HousemateCommsException("Unknown message payload type " + payload.getClass().getName());
    }
}
