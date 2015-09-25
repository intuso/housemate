package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.proxy.api.AvailableChildrenListener;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.web.client.bootstrap.widget.NestedTable;
import com.intuso.housemate.web.client.event.SelectedIdsChangedEvent;
import com.intuso.housemate.web.client.handler.HasSelectedIdsChangedHandlers;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/09/13
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class NestedList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<DATA, ?, ?, ?, ?>>
        extends NestedTable implements AvailableChildrenListener<GWTProxyList<DATA, OBJECT>>, HasSelectedIdsChangedHandlers {

    private boolean headerAdded = false;
    private GWTProxyList<DATA, OBJECT> list;
    private Set<String> filteredIds = null;
    private boolean includeFiltered = true;

    private final Map<String, RowNum> rowNums = Maps.newHashMap();
    private boolean showIfEmpty = true;

    public void filter(Set<String> filteredIds, boolean includeFiltered) {
        this.filteredIds = filteredIds;
        this.includeFiltered = includeFiltered;
    }

    public void setShowIfEmpty(boolean showIfEmpty) {
        this.showIfEmpty = showIfEmpty;
        if(grid.getRowCount() == 0 && !showIfEmpty)
            setVisible(false);
    }

    public void setList(GWTProxyList<DATA, OBJECT> list) {
        this.list = list;
        this.list.addAvailableChildrenListener(this, true);
    }

    @Override
    public void childAdded(GWTProxyList<DATA, OBJECT> list, ChildOverview childOverview) {
        if(filteredIds == null || includeFiltered == filteredIds.contains(childOverview.getId())) {
            setVisible(true);
            addEntry(childOverview);
        }
    }

    @Override
    public void childRemoved(GWTProxyList<DATA, OBJECT> list, ChildOverview childOverview) {
        RowNum rowNum = rowNums.get(childOverview.getId());
        if(rowNum != null)
            grid.removeRow(rowNum.getRow());
        for(RowNum rn : rowNums.values())
            if(rn.getRow() > rowNum.getRow())
                rn.decrement();
        if(grid.getRowCount() == 0 && !showIfEmpty)
            setVisible(false);
    }

    private void addEntry(final ChildOverview childOverview) {
        if(childOverview != null) {
            OBJECT object = list.get(childOverview.getId());
            RowNum rowNum = addRow(childOverview.getName(), getWidget(childOverview, object));
            rowNums.put(childOverview.getId(), rowNum);
        }
    }

    @Override
    public HandlerRegistration addSelectedIdsChangedHandler(SelectedIdsChangedHandler handler) {
        return addHandler(handler, SelectedIdsChangedEvent.TYPE);
    }

    public void setSelected(Set<String> ids) {
        if(ids == null)
            ids = Sets.newHashSet();
//        for(Map.Entry<String, WidgetPanel> entry : widgetPanels.entrySet())
//            setVisible(entry.getValue(), ids.contains(entry.getKey()));
    }

    protected abstract IsWidget getWidget(ChildOverview childOverview, OBJECT object);
}
