package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.comms.api.internal.payload.OptionData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.comms.v1_0.api.ObjectFactory;
import com.intuso.housemate.object.api.internal.Option;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

public class ServerProxyOption
        extends ServerProxyObject<OptionData, ListData<SubTypeData>,
            ServerProxyList<SubTypeData, ServerProxySubType>,
            ServerProxyOption,
            Option.Listener<? super ServerProxyOption>>
        implements Option<ServerProxyList<SubTypeData, ServerProxySubType>, ServerProxyOption> {

    private ServerProxyList<SubTypeData, ServerProxySubType> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyOption(ListenersFactory listenersFactory,
                             ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory,
                             @Assisted Logger logger,
                             @Assisted OptionData data) {
        super(listenersFactory, objectFactory, logger, data);
    }

    @Override
    protected void getChildObjects() {
        subTypes = getChild(OptionData.SUB_TYPES_ID);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // nothing to do
    }

    @Override
    public ServerProxyList<SubTypeData, ServerProxySubType> getSubTypes() {
        return subTypes;
    }
}
