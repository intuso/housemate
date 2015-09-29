package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.comms.api.internal.ChildOverview;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.RemoteObject;
import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstanceMapMapper;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstanceMapper;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;

/**
 * Created by tomc on 28/09/15.
 */
public class MessageMapper {

    private final ChildOverviewMapper childOverviewMapper;
    private final DataMapper dataMapper;
    private final TreeDataMapper treeDataMapper;
    private final TreeLoadInfoMapper treeLoadInfoMapper;
    private final TypeInstanceMapper typeInstanceMapper;
    private final TypeInstancesMapper typeInstancesMapper;
    private final TypeInstanceMapMapper typeInstanceMapMapper;

    @Inject
    public MessageMapper(ChildOverviewMapper childOverviewMapper, DataMapper dataMapper, TreeDataMapper treeDataMapper, TreeLoadInfoMapper treeLoadInfoMapper, TypeInstanceMapper typeInstanceMapper, TypeInstancesMapper typeInstancesMapper, TypeInstanceMapMapper typeInstanceMapMapper) {
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
        return new Message<>(message.getPath(), message.getType(), this.<PAYLOAD>map(message.getPayload()), message.getRoute());
    }

    public <PAYLOAD extends com.intuso.housemate.comms.v1_0.api.Message.Payload> com.intuso.housemate.comms.v1_0.api.Message<PAYLOAD> map(Message<?> message) {
        if(message == null)
            return null;
        return new com.intuso.housemate.comms.v1_0.api.Message<PAYLOAD>(message.getPath(), message.getType(), this.<PAYLOAD>map(message.getPayload()), message.getRoute());
    }

    public <PAYLOAD extends Message.Payload> PAYLOAD map(com.intuso.housemate.comms.v1_0.api.Message.Payload payload) {
        if(payload == null)
            return null;
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.HousemateData)
            return (PAYLOAD) dataMapper.map((com.intuso.housemate.comms.v1_0.api.payload.HousemateData<?>) payload);
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.NoPayload)
            return (PAYLOAD) NoPayload.INSTANCE;
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.StringPayload)
            return (PAYLOAD) new StringPayload(((com.intuso.housemate.comms.v1_0.api.payload.StringPayload) payload).getValue());
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration) {
            com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration applicationRegistration = (com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration) payload;
            return (PAYLOAD) new ApplicationRegistration(new ApplicationDetails(applicationRegistration.getApplicationDetails().getApplicationId(),
                        applicationRegistration.getApplicationDetails().getApplicationName(),
                        applicationRegistration.getApplicationDetails().getApplicationDescription()),
                    applicationRegistration.getApplicationInstanceId(),
                    applicationRegistration.getComponent(),
                    ApplicationRegistration.ClientType.valueOf(applicationRegistration.getType().name()));
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.ChildOverview)
            return (PAYLOAD) childOverviewMapper.map((com.intuso.housemate.comms.v1_0.api.ChildOverview) payload);
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.RemoteObject.ChildOverviews) {
            com.intuso.housemate.comms.v1_0.api.RemoteObject.ChildOverviews childOverviews = (com.intuso.housemate.comms.v1_0.api.RemoteObject.ChildOverviews) payload;
            return (PAYLOAD) new RemoteObject.ChildOverviews(Lists.transform(childOverviews.getChildOverviews(), childOverviewMapper.getFromV1_0Function()),
                    childOverviews.getError());
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.RemoteObject.LoadRequest) {
            com.intuso.housemate.comms.v1_0.api.RemoteObject.LoadRequest loadRequest = (com.intuso.housemate.comms.v1_0.api.RemoteObject.LoadRequest) payload;
            return (PAYLOAD) new RemoteObject.LoadRequest(loadRequest.getLoaderId(), Lists.transform(loadRequest.getTreeLoadInfos(), treeLoadInfoMapper.getFromV1_0Function()));
        } else if(payload instanceof com.intuso.housemate.comms.v1_0.api.RemoteObject.LoadFinished) {
            com.intuso.housemate.comms.v1_0.api.RemoteObject.LoadFinished loadFinished = (com.intuso.housemate.comms.v1_0.api.RemoteObject.LoadFinished) payload;
            return (PAYLOAD) new RemoteObject.LoadFinished(loadFinished.getLoaderId(), loadFinished.getErrors());
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
            return (PAYLOAD) ServerConnectionStatus.valueOf(((com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus) payload).name());
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.ApplicationData.StatusPayload)
            return (PAYLOAD) new ApplicationData.StatusPayload(Application.Status.valueOf(((com.intuso.housemate.comms.v1_0.api.payload.ApplicationData.StatusPayload) payload).getStatus().name()));
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData.StatusPayload)
            return (PAYLOAD) new ApplicationInstanceData.StatusPayload(ApplicationInstance.Status.valueOf(((com.intuso.housemate.comms.v1_0.api.payload.ApplicationData.StatusPayload) payload).getStatus().name()));
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo)
            return (PAYLOAD) treeLoadInfoMapper.map((com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo) payload);
        else if(payload instanceof com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeData)
            return (PAYLOAD) treeDataMapper.map((com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeData) payload);
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
        else if(payload instanceof HousemateData)
            return (PAYLOAD) dataMapper.map((HousemateData<?>) payload);
        else if(payload instanceof NoPayload)
            return (PAYLOAD) com.intuso.housemate.comms.v1_0.api.payload.NoPayload.INSTANCE;
        else if(payload instanceof StringPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.StringPayload(((com.intuso.housemate.comms.v1_0.api.payload.StringPayload) payload).getValue());
        else if(payload instanceof ApplicationRegistration) {
            ApplicationRegistration applicationRegistration = (ApplicationRegistration) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration(new com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails(applicationRegistration.getApplicationDetails().getApplicationId(),
                        applicationRegistration.getApplicationDetails().getApplicationName(),
                        applicationRegistration.getApplicationDetails().getApplicationDescription()),
                    applicationRegistration.getApplicationInstanceId(),
                    applicationRegistration.getComponent(),
                    com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration.ClientType.valueOf(applicationRegistration.getType().name()));
        } else if(payload instanceof ChildOverview)
            return (PAYLOAD) childOverviewMapper.map((ChildOverview) payload);
        else if(payload instanceof RemoteObject.ChildOverviews) {
            RemoteObject.ChildOverviews childOverviews = (RemoteObject.ChildOverviews) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.RemoteObject.ChildOverviews(Lists.transform(childOverviews.getChildOverviews(), childOverviewMapper.getToV1_0Function()),
                    childOverviews.getError());
        } else if(payload instanceof RemoteObject.LoadRequest) {
            RemoteObject.LoadRequest loadRequest = (RemoteObject.LoadRequest) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.RemoteObject.LoadRequest(loadRequest.getLoaderId(), Lists.transform(loadRequest.getTreeLoadInfos(), treeLoadInfoMapper.getToV1_0Function()));
        } else if(payload instanceof RemoteObject.LoadFinished) {
            RemoteObject.LoadFinished loadFinished = (RemoteObject.LoadFinished) payload;
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.RemoteObject.LoadFinished(loadFinished.getLoaderId(), loadFinished.getErrors());
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
            return (PAYLOAD) com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus.valueOf(((ServerConnectionStatus) payload).name());
        else if(payload instanceof ApplicationData.StatusPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.ApplicationData.StatusPayload(com.intuso.housemate.object.v1_0.api.Application.Status.valueOf(((ApplicationData.StatusPayload) payload).getStatus().name()));
        else if(payload instanceof ApplicationInstanceData.StatusPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData.StatusPayload(com.intuso.housemate.object.v1_0.api.ApplicationInstance.Status.valueOf(((ApplicationData.StatusPayload) payload).getStatus().name()));
        else if(payload instanceof RemoteObject.TreeLoadInfo)
            return (PAYLOAD) treeLoadInfoMapper.map((RemoteObject.TreeLoadInfo) payload);
        else if(payload instanceof RemoteObject.TreeData)
            return (PAYLOAD) treeDataMapper.map((RemoteObject.TreeData) payload);
        else if(payload instanceof TypeData.TypeInstancePayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstancePayload(typeInstanceMapper.map(((TypeData.TypeInstancePayload) payload).getTypeInstance()));
        else if(payload instanceof TypeData.TypeInstancesPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstancesPayload(typeInstancesMapper.map(((TypeData.TypeInstancesPayload) payload).getTypeInstances()));
        else if(payload instanceof TypeData.TypeInstanceMapPayload)
            return (PAYLOAD) new com.intuso.housemate.comms.v1_0.api.payload.TypeData.TypeInstanceMapPayload(typeInstanceMapMapper.map(((TypeData.TypeInstanceMapPayload) payload).getTypeInstanceMap()));
        throw new com.intuso.housemate.comms.v1_0.api.HousemateCommsException("Unknown message payload type " + payload.getClass().getName());
    }
}
