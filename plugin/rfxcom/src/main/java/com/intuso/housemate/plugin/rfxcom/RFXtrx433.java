package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
import com.intuso.housemate.object.real.RealHardware;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 13/03/15.
 */
public class RFXtrx433 extends RealHardware {

    public final static String AUTO_CREATE_ID = "auto-create";
    public final static String PATTERN_ID = "pattern";

    /**
     * @param log              {@inheritDoc}
     * @param listenersFactory
     * @param id               the hardware's id
     * @param name             the hardware's name
     * @param description      the hardware's description
     */
    public RFXtrx433(Log log, ListenersFactory listenersFactory, String id, String name, String description) {
        super(log, listenersFactory, id, name, description,
                BooleanType.createProperty(log, listenersFactory, AUTO_CREATE_ID, "Auto create unknown devices", "True if devices should be automatically created for previously unknown house/unit ids ", Lists.newArrayList(true)),
                StringType.createProperty(log, listenersFactory, PATTERN_ID, "Socket pattern", "Pattern to match the socket that the device is connected to", Lists.newArrayList(".*ttyUSB.*")));
    }
}
