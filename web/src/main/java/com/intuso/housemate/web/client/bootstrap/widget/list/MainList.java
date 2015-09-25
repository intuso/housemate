package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.proxy.api.AvailableChildrenListener;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.event.SelectedIdsChangedEvent;
import com.intuso.housemate.web.client.handler.HasSelectedIdsChangedHandlers;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

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
    private final Map<String, IsWidget> widgets = Maps.newHashMap();

    public MainList(String title, GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand addCommand) {

        addStyleName("main");
        add(new Heading(HeadingSize.H4, title));
        add(row);
        if(addCommand != null)
            add(new AddButton(types, addCommand));
    }

    public void setList(GWTProxyList<DATA, OBJECT> list) {
        list.addAvailableChildrenListener(this, true);
    }

    @Override
    public void childAdded(GWTProxyList<DATA, OBJECT> list, final ChildOverview childOverview) {
        getWidget(childOverview, new LazyLoadedWidgetCallback() {
            @Override
            public void widgetReady(IsWidget widget) {
                widgets.put(childOverview.getId(), widget);
                row.add(widget);
            }
        });
    }

    @Override
    public void childRemoved(GWTProxyList<DATA, OBJECT> list, ChildOverview childOverview) {
        IsWidget widget = widgets.get(childOverview.getId());
        if(widget != null)
            remove(widget);
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
