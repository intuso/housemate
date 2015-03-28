package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 31/01/14
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public class MultiListBridge<
            WBL extends HousemateData<?>,
            OWR extends BaseHousemateObject<?>,
            WR extends BridgeObject<? extends WBL, ?, ?, ?, ?>>
        extends ListBridge<WBL, OWR, WR> {

    private Function<? super OWR, ? extends WR> converter;

    public MultiListBridge(Log log, ListenersFactory listenersFactory, ListData<WBL> data) {
        super(log, listenersFactory, data);
    }

    public MultiListBridge(Log log, ListenersFactory listenersFactory, ListData<WBL> data, Function<? super OWR, ? extends WR> converter) {
        this(log, listenersFactory, data);
        setConverter(converter);
    }

    public void setConverter(Function<? super OWR, ? extends WR> converter) {
        this.converter = converter;
    }

    public void addList(List<OWR> list) {
        list.addObjectListener(new SourceListListener(), true);
    }

    private class SourceListListener implements ListListener<OWR> {

        private final Set<String> childIdsFromThisList = Sets.newHashSet();

        @Override
        public void elementAdded(OWR element) {
            try {
                addChild(converter.apply(element));
                childIdsFromThisList.add(element.getId());
            } catch(HousemateRuntimeException e) {
                getLog().w("Tried to add an object to multi-list " + getId() + " whose id (" + element.getId() + ") is a duplicate of one in another list");
            }
        }

        @Override
        public void elementRemoved(OWR element) {
            if(childIdsFromThisList.remove(element.getId()))
                removeChild(element.getId());
        }
    }
}
