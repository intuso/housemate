package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.ValueMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyValueBridge
        extends ProxyValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Value.Data, Value.Data, Value.Listener<? super ProxyValueBridge>, ProxyValueBridge>
        implements Value<Type.Instances, ProxyTypeBridge, ProxyValueBridge> {

    @Inject
    protected ProxyValueBridge(@Assisted Logger logger,
                               ValueMapper valueMapper,
                               TypeInstancesMapper typeInstancesMapper,
                               ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Value.Data.class, valueMapper, typeInstancesMapper, listenersFactory);
    }
}
