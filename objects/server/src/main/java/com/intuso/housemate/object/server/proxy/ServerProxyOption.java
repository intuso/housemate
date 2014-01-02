package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubTypeData;

public class ServerProxyOption
        extends ServerProxyObject<OptionData, ListData<SubTypeData>,
        ServerProxyList<SubTypeData, ServerProxySubType>,
        ServerProxyOption,
                    OptionListener>
        implements Option<ServerProxyList<SubTypeData, ServerProxySubType>> {

    private ServerProxyList<SubTypeData, ServerProxySubType> subTypes;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ServerProxyOption(ServerProxyResources<ServerProxyFactory.List<SubTypeData, ServerProxySubType>> resources,
                             OptionData data) {
        super(resources, data);
    }

    @Override
    protected void getChildObjects() {
        subTypes = getChild(SUB_TYPES_ID);
    }

    @Override
    public ServerProxyList<SubTypeData, ServerProxySubType> getSubTypes() {
        return subTypes;
    }
}
