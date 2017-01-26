package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.CommandMapper;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.housemate.client.proxy.api.internal.object.JMSUtil;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
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

    private JMSUtil.Sender performSender;
    private com.intuso.housemate.client.v1_0.proxy.api.object.JMSUtil.Receiver<com.intuso.housemate.client.v1_0.api.object.Command.PerformData> performReceiver;
    private com.intuso.housemate.client.v1_0.proxy.api.object.JMSUtil.Sender performStatusSender;
    private JMSUtil.Receiver<PerformStatusData> performStatusReceiver;

    private int nextId;
    private final Map<String, PerformListener<? super ProxyCommandBridge>> listenerMap = Maps.newHashMap();

    @Inject
    protected ProxyCommandBridge(@Assisted Logger logger,
                                 CommandMapper commandMapper,
                                 ManagedCollectionFactory managedCollectionFactory,
                                 ProxyObjectBridge.Factory<ProxyValueBridge> valueFactory,
                                 ProxyObjectBridge.Factory<ProxyListBridge<ProxyParameterBridge>> parametersFactory) {
        super(logger, Command.Data.class, commandMapper, managedCollectionFactory);
        this.commandMapper = commandMapper;
        enabledValue = valueFactory.create(ChildUtil.logger(logger, Command.ENABLED_ID));
        parameters = parametersFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        enabledValue.init(
                com.intuso.housemate.client.proxy.api.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Command.ENABLED_ID),
                ChildUtil.name(internalName, Command.ENABLED_ID),
                connection);
        parameters.init(
                com.intuso.housemate.client.proxy.api.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Command.PARAMETERS_ID),
                ChildUtil.name(internalName, Command.PARAMETERS_ID),
                connection);
        performSender = new JMSUtil.Sender(logger, connection, JMSUtil.Type.Queue, ChildUtil.name(internalName, Command.PERFORM_ID));
        performReceiver = new com.intuso.housemate.client.v1_0.proxy.api.object.JMSUtil.Receiver<>(logger, connection, com.intuso.housemate.client.v1_0.proxy.api.object.JMSUtil.Type.Queue, com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, Command.PERFORM_ID), com.intuso.housemate.client.v1_0.api.object.Command.PerformData.class,
                new com.intuso.housemate.client.v1_0.proxy.api.object.JMSUtil.Receiver.Listener<com.intuso.housemate.client.v1_0.api.object.Command.PerformData>() {
                    @Override
                    public void onMessage(com.intuso.housemate.client.v1_0.api.object.Command.PerformData performData, boolean wasPersisted) {
                        try {
                            performSender.send(commandMapper.map(performData), wasPersisted);
                        } catch (JMSException e) {
                            logger.error("Failed to send perform message to command. Telling client it failed", e);
                            try {
                                performStatusSender.send(new PerformStatusData(performData.getOpId(), true, "Failed to send perform message to command"), false);
                            } catch (JMSException e1) {
                                logger.error("Failed to broadcast perform status message");
                            }
                        }
                    }
                });
        performStatusSender = new com.intuso.housemate.client.v1_0.proxy.api.object.JMSUtil.Sender(logger, connection, com.intuso.housemate.client.v1_0.proxy.api.object.JMSUtil.Type.Topic, com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, Command.PERFORM_STATUS_ID));
        performStatusReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(internalName, Command.PERFORM_STATUS_ID), PerformStatusData.class,
                new JMSUtil.Receiver.Listener<PerformStatusData>() {
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
                } catch (JMSException e) {
                    logger.error("Failed to broadcast perform status message");
                }
                // todo call object listeners
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
        } catch(JMSException e) {
            if(listener != null) {
                listenerMap.remove(id);
                listener.commandFailed(this, "Failed to send perform message: " + e.getMessage());
            }
            throw new HousemateException("Failed to send perform message", e);
        }
    }
}
