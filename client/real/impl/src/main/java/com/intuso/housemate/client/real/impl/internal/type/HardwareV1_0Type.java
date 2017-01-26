package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyHardware;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyServer;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 25/01/17.
 */
public class HardwareV1_0Type extends RealObjectV1_0Type<SimpleProxyHardware> {

    public final static String TYPE_ID = "hardware";
    public final static String TYPE_NAME = "Hardware";
    public final static String TYPE_DESCRIPTION = "Hardware objects";

    /**
     * @param logger
     * @param managedCollectionFactory
     * @param server           the root to get the object from
     */
    @Inject
    public HardwareV1_0Type(@Type Logger logger, ManagedCollectionFactory managedCollectionFactory, SimpleProxyServer server) {
        super(ChildUtil.logger(logger, TYPE_ID), TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Sets.newHashSet(Hardware.Data.OBJECT_CLASS), managedCollectionFactory, server);
    }
}
