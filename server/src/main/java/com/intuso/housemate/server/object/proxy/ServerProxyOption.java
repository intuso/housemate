package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyOption
        extends ServerProxyObject<OptionData, ListData<SubTypeData>,
            ServerProxyList<SubTypeData, ServerProxySubType>,
            ServerProxyOption,
            OptionListener>
        implements Option<ServerProxyList<SubTypeData, ServerProxySubType>> {

    private ServerProxyList<SubTypeData, ServerProxySubType> subTypes;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyOption(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted OptionData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        subTypes = getChild(SUB_TYPES_ID);
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
