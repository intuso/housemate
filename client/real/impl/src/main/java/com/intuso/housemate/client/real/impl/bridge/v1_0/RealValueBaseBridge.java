package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.object.ObjectMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.api.internal.object.view.ValueBaseView;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public abstract class RealValueBaseBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.ValueBase.Data,
        INTERNAL_DATA extends ValueBase.Data,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VIEW extends ValueBaseView,
        VALUE extends RealValueBaseBridge<VERSION_DATA, INTERNAL_DATA, LISTENER, VIEW, VALUE>>
        extends RealObjectBridge<VERSION_DATA, INTERNAL_DATA, LISTENER, VIEW>
        implements ValueBase<INTERNAL_DATA, Type.Instances, RealTypeBridge, LISTENER, VIEW, VALUE> {

    private final TypeInstancesMapper typeInstancesMapper;

    private Type.Instances value;

    private com.intuso.housemate.client.messaging.api.internal.Sender valueSender;
    private Receiver<com.intuso.housemate.client.v1_0.api.object.Type.Instances> valueReceiver;

    protected RealValueBaseBridge(Logger logger,
                                  Class<VERSION_DATA> versionDataClass,
                                  ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper,
                                  TypeInstancesMapper typeInstancesMapper,
                                  ManagedCollectionFactory managedCollectionFactory) {
        super(logger, versionDataClass, dataMapper, managedCollectionFactory);
        this.typeInstancesMapper = typeInstancesMapper;
    }

    @Override
    protected void initChildren(String versionName, String internalName, Sender.Factory internalSenderFactory, com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory, com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory, Receiver.Factory v1_0ReceiverFactory) {
        super.initChildren(versionName, internalName, internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        valueSender = internalSenderFactory.create(logger, ChildUtil.name(internalName, Value.VALUE_ID));
        valueReceiver = v1_0ReceiverFactory.create(logger, com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, Value.VALUE_ID), com.intuso.housemate.client.v1_0.api.object.Type.Instances.class);
        valueReceiver.listen(new Receiver.Listener<com.intuso.housemate.client.v1_0.api.object.Type.Instances>() {
            @Override
            public void onMessage(com.intuso.housemate.client.v1_0.api.object.Type.Instances instances, boolean persistent) {
                value = typeInstancesMapper.map(instances);
                try {
                    valueSender.send(value, persistent);
                } catch (Throwable t) {
                    logger.error("Failed to send new values onto internal topic", t);
                }
                // todo call object listeners
            }
        });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(valueSender != null) {
            valueSender.close();
            valueSender = null;
        }
        if(valueReceiver != null) {
            valueReceiver.close();
            valueReceiver = null;
        }
    }

    @Override
    public RealTypeBridge getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public Type.Instances getValues() {
        return value;
    }
}
