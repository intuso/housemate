package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.OptionMapper;
import com.intuso.housemate.client.api.internal.object.Option;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyOptionBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Option.Data, Option.Data, Option.Listener<? super ProxyOptionBridge>>
        implements Option<ProxyListBridge<ProxySubTypeBridge>, ProxyOptionBridge> {

    private final ProxyListBridge<ProxySubTypeBridge> subTypes;

    @Inject
    protected ProxyOptionBridge(@Assisted Logger logger,
                                OptionMapper optionMapper,
                                ProxyObjectBridge.Factory<ProxyListBridge<ProxySubTypeBridge>> subTypesFactory,
                                ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Option.Data.class, optionMapper, listenersFactory);
        this.subTypes = subTypesFactory.create(ChildUtil.logger(logger, Option.SUB_TYPES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        subTypes.init(
                com.intuso.housemate.client.proxy.api.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Option.SUB_TYPES_ID),
                ChildUtil.name(internalName,
                        Option.SUB_TYPES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public ProxyListBridge<ProxySubTypeBridge> getSubTypes() {
        return subTypes;
    }
}
