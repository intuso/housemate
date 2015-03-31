package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.object.proxy.AvailableChildrenListener;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.event.SelectedIdsChangedEvent;
import com.intuso.housemate.web.client.handler.HasSelectedIdsChangedHandlers;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/09/13
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class MainList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<DATA, ?, ?, ?, ?>>
        extends FlowPanel implements AvailableChildrenListener<GWTProxyList<DATA, OBJECT>>, HasSelectedIdsChangedHandlers {

    private final Row row = new Row();
    private final List<String> filteredIds;
    private final boolean includeFiltered;

    private final Map<String, IsWidget> widgets = Maps.newHashMap();

    public MainList(String title, List<String> filteredIds, boolean includeFiltered) {

        this.filteredIds = filteredIds;
        this.includeFiltered = includeFiltered;

        // only show when there is something in the list
        setVisible(false);

        addStyleName("main");
        add(new Heading(HeadingSize.H4, title));
        add(row);
    }

    public void setList(GWTProxyList<DATA, OBJECT> list) {
        list.addAvailableChildrenListener(this, true);
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
        IsWidget widget = widgets.get(childOverview.getId());
        if(widget != null)
            remove(widget);
    }

    private void addEntry(final ChildOverview childOverview) {
        getWidget(childOverview, new LazyLoadedWidgetCallback() {
            @Override
            public void widgetReady(IsWidget widget) {
                widgets.put(childOverview.getId(), widget);
                row.add(widget);
            }
        });
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

    protected abstract void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback);

}
