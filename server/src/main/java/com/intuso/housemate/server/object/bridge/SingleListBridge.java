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
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 31/01/14
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public class SingleListBridge<
            WBL extends HousemateData<?>,
            OWR extends BaseHousemateObject<?>,
            WR extends BridgeObject<? extends WBL, ?, ?, ?, ?>>
        extends ListBridge<WBL, OWR, WR> {

    private final List<? extends OWR> list;
    private ListenerRegistration otherListListener;

    public SingleListBridge(Log log, ListenersFactory listenersFactory, List<? extends OWR> list) {
        super(log, listenersFactory, new ListData(list.getId(), list.getName(), list.getDescription()));
        this.list = list;
    }

    public SingleListBridge(Log log, ListenersFactory listenersFactory, List<? extends OWR> list, final Function<? super OWR, ? extends WR> converter) {
        this(log, listenersFactory, list);
        convert(converter);
    }

    public void convert(final Function<? super OWR, ? extends WR> converter) {
        otherListListener = list.addObjectListener(new ListListener<OWR>() {
            @Override
            public void elementAdded(OWR element) {
                addChild(converter.apply(element));
            }

            @Override
            public void elementRemoved(OWR element) {
                removeChild(element.getId());
            }
        }, true);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(otherListListener);
        return result;
    }
}
