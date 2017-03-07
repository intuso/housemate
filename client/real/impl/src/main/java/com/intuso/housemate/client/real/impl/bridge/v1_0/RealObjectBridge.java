package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.object.ObjectMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public abstract class RealObjectBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.Object.Data,
        INTERNAL_DATA extends Object.Data,
        LISTENER extends Object.Listener>
        implements Object<LISTENER> {

    protected final Logger logger;
    protected VERSION_DATA data;
    protected final Class<VERSION_DATA> versionDataClass;
    protected final ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper;
    protected final ManagedCollection<LISTENER> listeners;
    protected final Receiver.Factory v1_0ReceiverFactory;
    protected final com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory;

    private com.intuso.housemate.client.messaging.api.internal.Sender sender;
    private Receiver<VERSION_DATA> receiver;

    protected RealObjectBridge(Logger logger,
                               Class<VERSION_DATA> versionDataClass,
                               ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper,
                               ManagedCollectionFactory managedCollectionFactory,
                               Receiver.Factory v1_0ReceiverFactory,
                               com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory) {
        this.v1_0ReceiverFactory = v1_0ReceiverFactory;
        this.internalSenderFactory = internalSenderFactory;
        logger.debug("Creating");
        this.logger = logger;
        this.versionDataClass = versionDataClass;
        this.dataMapper = dataMapper;
        this.listeners = managedCollectionFactory.create();
    }

    public final void init(String versionName, String internalName) {
        logger.debug("Init {} -> {}", versionName, internalName);
        sender = internalSenderFactory.create(logger, internalName);
        receiver = v1_0ReceiverFactory.create(logger, versionName, versionDataClass);
        receiver.listen(new Receiver.Listener<VERSION_DATA>() {
                    @Override
                    public void onMessage(VERSION_DATA data, boolean wasPersisted) {
                        try {
                            sender.send(dataMapper.map(data), wasPersisted);
                        } catch (Throwable t) {
                            logger.error("Failed to send data object", t);
                        }
                    }
                });
        initChildren(versionName, internalName);
    }

    protected void initChildren(String versionName, String internalName) {}

    public final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(sender != null) {
            sender.close();
            sender = null;
        }
        if(receiver != null) {
            receiver.close();
            receiver = null;
        }
    }

    protected void uninitChildren() {}

    @Override
    public String getObjectClass() {
        return data.getObjectClass();
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public final String getName() {
        return data.getName();
    }

    @Override
    public final String getDescription() {
        return data.getDescription();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(LISTENER listener) {
        return listeners.add(listener);
    }

    public interface Factory<OBJECT extends RealObjectBridge<?, ?, ?>> {
        OBJECT create(Logger logger);
    }
}
