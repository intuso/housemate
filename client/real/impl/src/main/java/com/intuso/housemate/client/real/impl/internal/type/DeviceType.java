package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.proxy.internal.object.ProxyServer;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 25/01/17.
 */
public class DeviceType extends RealObjectType<ProxyDevice<?, ?, ?, ?, ?, ?, ?>> {

    public final static String TYPE_ID = "device";
    public final static String TYPE_NAME = "device";
    public final static String TYPE_DESCRIPTION = "Device objects";

    /**
     * @param logger
     * @param managedCollectionFactory
     * @param server           the root to get the object from
     */
    @Inject
    public DeviceType(@Type Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      ProxyServer.Simple server) {
        super(ChildUtil.logger(logger, TYPE_ID), TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Sets.newHashSet(Device.Connected.Data.OBJECT_CLASS, Device.Group.Data.OBJECT_CLASS), managedCollectionFactory, server);
    }
}
