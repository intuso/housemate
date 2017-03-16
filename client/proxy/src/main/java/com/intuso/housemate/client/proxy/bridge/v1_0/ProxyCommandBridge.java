package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.CommandMapper;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyCommandBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Command.Data, Command.Data, Command.Listener<? super ProxyCommandBridge>>
        implements Command<Type.InstanceMap, ProxyValueBridge, ProxyListBridge<ProxyParameterBridge>, ProxyCommandBridge> {

    private final CommandMapper commandMapper;

    private final ProxyValueBridge enabledValue;
    private final ProxyListBridge<ProxyParameterBridge> parameters;

    private com.intuso.housemate.client.messaging.api.internal.Sender performSender;
    private Receiver<com.intuso.housemate.client.v1_0.api.object.Command.PerformData> performReceiver;
    private Sender performStatusSender;
    private com.intuso.housemate.client.messaging.api.internal.Receiver<PerformStatusData> performStatusReceiver;

    private int nextId;
    private final Map<String, PerformListener<? super ProxyCommandBridge>> listenerMap = Maps.newHashMap();
    private final com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory;
    private final Receiver.Factory v1_0ReceiverFactory;

    @Inject
    protected ProxyCommandBridge(@Assisted Logger logger,
                                 CommandMapper commandMapper,
                                 ManagedCollectionFactory managedCollectionFactory,
                                 com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                 com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory,
                                 Receiver.Factory v1_0ReceiverFactory,
                                 Sender.Factory v1_0SenderFactory,
                                 Factory<ProxyValueBridge> valueFactory,
                                 Factory<ProxyListBridge<ProxyParameterBridge>> parametersFactory) {
        super(logger, Command.Data.class, commandMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        this.commandMapper = commandMapper;
        this.internalSenderFactory = internalSenderFactory;
        this.v1_0ReceiverFactory = v1_0ReceiverFactory;
        enabledValue = valueFactory.create(ChildUtil.logger(logger, Command.ENABLED_ID));
        parameters = parametersFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        enabledValue.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Command.ENABLED_ID),
                ChildUtil.name(internalName, Command.ENABLED_ID)
        );
        parameters.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Command.PARAMETERS_ID),
                ChildUtil.name(internalName, Command.PARAMETERS_ID)
        );
        performSender = internalSenderFactory.create(logger, ChildUtil.name(internalName, Command.PERFORM_ID));
        performReceiver = v1_0ReceiverFactory.create(logger, com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, Command.PERFORM_ID), com.intuso.housemate.client.v1_0.api.object.Command.PerformData.class);
        performStatusSender = v1_0SenderFactory.create(logger, com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, Command.PERFORM_STATUS_ID));
        performStatusReceiver = internalReceiverFactory.create(logger, ChildUtil.name(internalName, Command.PERFORM_STATUS_ID), PerformStatusData.class);
        performStatusReceiver.listen(new com.intuso.housemate.client.messaging.api.internal.Receiver.Listener<PerformStatusData>() {
            @Override
            public void onMessage(PerformStatusData performStatusData, boolean wasPersisted) {
                if (listenerMap.containsKey(performStatusData.getOpId())) {
                    if (performStatusData.isFinished()) {
                        if (performStatusData.getError() == null)
                            listenerMap.remove(performStatusData.getOpId()).commandFinished(ProxyCommandBridge.this);
                        else
                            listenerMap.remove(performStatusData.getOpId()).commandFailed(ProxyCommandBridge.this, performStatusData.getError());
                    } else
                        listenerMap.get(performStatusData.getOpId()).commandStarted(ProxyCommandBridge.this);
                }
                try {
                    performStatusSender.send(commandMapper.map(performStatusData), wasPersisted);
                } catch (Throwable t) {
                    logger.error("Failed to broadcast perform status message", t);
                }
                // todo call object listeners
            }
        });
        performReceiver.listen(new Receiver.Listener<com.intuso.housemate.client.v1_0.api.object.Command.PerformData>() {
            @Override
            public void onMessage(com.intuso.housemate.client.v1_0.api.object.Command.PerformData performData, boolean persistent) {
                try {
                    performSender.send(commandMapper.map(performData), persistent);
                } catch (Throwable t) {
                    logger.error("Failed to send perform message to command. Telling client it failed", t);
                    try {
                        performStatusSender.send(new PerformStatusData(performData.getOpId(), true, "Failed to send perform message to command"), false);
                    } catch (Throwable t1) {
                        logger.error("Failed to broadcast perform status message", t);
                    }
                }
            }
        });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        enabledValue.uninit();
        parameters.uninit();
        if(performSender != null) {
            performSender.close();
            performSender = null;
        }
        if(performReceiver != null) {
            performReceiver.close();
            performReceiver = null;
        }
        if(performStatusSender != null) {
            performStatusSender.close();
            performStatusSender = null;
        }
        if(performStatusReceiver != null) {
            performStatusReceiver.close();
            performStatusReceiver = null;
        }
    }

    public boolean isEnabled() {
        return enabledValue != null
                && enabledValue.getValue() != null
                && enabledValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(enabledValue.getValue().getFirstValue());
    }

    @Override
    public ProxyValueBridge getEnabledValue() {
        return enabledValue;
    }

    @Override
    public ProxyListBridge<ProxyParameterBridge> getParameters() {
        return parameters;
    }

    /**
     * Performs the command without any type values. It is not correct to use this method on a command that has parameters
     * @param listener the listener for progress of the command
     */
    public final void perform(PerformListener<? super ProxyCommandBridge> listener) {
        perform(new Type.InstanceMap(), listener);
    }

    @Override
    public final synchronized void perform(Type.InstanceMap values, PerformListener<? super ProxyCommandBridge> listener) {
        String id = null;
        if(listener != null) {
            id = "" + nextId++;
            listenerMap.put(id, listener);
        }
        try {
            performSender.send(values, true);
        } catch(Throwable t) {
            if(listener != null) {
                listenerMap.remove(id);
                listener.commandFailed(this, "Failed to send perform message: " + t.getMessage());
            }
            throw new HousemateException("Failed to send perform message", t);
        }
    }
}
