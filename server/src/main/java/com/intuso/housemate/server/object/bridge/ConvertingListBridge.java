package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class ConvertingListBridge<
            WBL extends HousemateData<?>,
            OWR extends BaseHousemateObject<?>,
            WR extends BridgeObject<? extends WBL, ?, ?, ?, ?>>
        extends ListBridge<WBL, WR>
        implements List<WR> {

    private final List<? extends OWR> list;
    private final Function<? super OWR, ? extends WR> converter;

    public ConvertingListBridge(Log log, ListenersFactory listenersFactory, List<? extends OWR> list, final Function<? super OWR, ? extends WR> converter) {
        super(log, listenersFactory, new ListData<WBL>(list.getId(), list.getName(), list.getDescription()));
        this.list = list;
        this.converter = converter;
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(list.addObjectListener(new ListListener<OWR>() {
            @Override
            public void elementAdded(OWR element) {
                addChild(converter.apply(element));
            }

            @Override
            public void elementRemoved(OWR element) {
                removeChild(element.getId());
            }
        }, true));
        return result;
    }
}
