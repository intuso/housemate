package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.comms.api.internal.payload.OptionData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.Option;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

public class ServerProxyOption
        extends ServerProxyObject<OptionData, ListData<SubTypeData>,
            ServerProxyList<SubTypeData, ServerProxySubType>,
            ServerProxyOption,
            Option.Listener<? super ServerProxyOption>>
        implements Option<ServerProxyList<SubTypeData, ServerProxySubType>, ServerProxyOption> {

    private ServerProxyList<SubTypeData, ServerProxySubType> subTypes;

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyOption(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted OptionData data) {
        super(log, listenersFactory, objectFactory, data);
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
