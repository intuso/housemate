package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.object.proxy.AvailableChildrenListener;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.event.SelectedIdsChangedEvent;
import com.intuso.housemate.web.client.handler.HasSelectedIdsChangedHandlers;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;

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
public abstract class ObjectList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<DATA, ?, ?, ?, ?>>
        extends PanelGroup implements AvailableChildrenListener<GWTProxyList<DATA, OBJECT>>, HasSelectedIdsChangedHandlers {

    private final GWTProxyList<DATA, OBJECT> list;
    private final List<String> filteredIds;
    private final boolean includeFiltered;

    private final Map<String, WidgetPanel> widgetPanels = Maps.newHashMap();
    private final Map<WidgetPanel, ShowHandler> showHandlers = Maps.newHashMap();
    private final Set<String> selectedIds = Sets.newHashSet();

    public ObjectList(GWTProxyList<DATA, OBJECT> list, String title, List<String> filteredIds, boolean includeFiltered) {

        this.list = list;
        this.filteredIds = filteredIds;
        this.includeFiltered = includeFiltered;

        // only show when there is something in the list
        setVisible(false);

        // add the widget style name
        addStyleName("object-list");

        add(new Heading(HeadingSize.H4, title));

        this.list.addAvailableChildrenListener(this, true);
    }

    @Override
    public void childAdded(GWTProxyList<DATA, OBJECT> list, ChildOverview childOverview) {
        if(includeFiltered == filteredIds.contains(childOverview.getId())) {
            setVisible(true);
            addEntry(childOverview);
        }
    }

    @Override
    public void childRemoved(GWTProxyList<DATA, OBJECT> list, ChildOverview childOverview) {
        WidgetPanel widgetPanel = widgetPanels.get(childOverview.getId());
        if(widgetPanel != null)
            remove(widgetPanel);
    }

    public void childRenamed(String id, String newName) {
        WidgetPanel widgetPanel = widgetPanels.get(id);
        if(widgetPanel != null) {
            widgetPanel.setHeading(newName);
        }
    }

    private void addEntry(final ChildOverview childOverview) {
        if(childOverview != null) {
            final WidgetPanel widgetPanel = new WidgetPanel();
            widgetPanel.setHeading(childOverview.getName());
            // lazy load the widget
            ShowHandler showHandler = new ShowObjectHandler(widgetPanel, childOverview.getId());
            showHandlers.put(widgetPanel, showHandler);
            widgetPanel.addShowHandler(showHandler);
            widgetPanel.addShowHandler(new ShowHandler() {
                @Override
                public void onShow(ShowEvent showEvent) {
                    if (selectedIds.add(childOverview.getId()))
                        ObjectList.this.fireEvent(new SelectedIdsChangedEvent(selectedIds));
                }
            });
            widgetPanel.addHideHandler(new HideHandler() {
                @Override
                public void onHide(HideEvent hideEvent) {
                    if (selectedIds.remove(childOverview.getId()))
                        ObjectList.this.fireEvent(new SelectedIdsChangedEvent(selectedIds));
                }
            });
            widgetPanels.put(childOverview.getId(), widgetPanel);
            add(widgetPanel);
        }
    }

    @Override
    public HandlerRegistration addSelectedIdsChangedHandler(SelectedIdsChangedHandler handler) {
        return addHandler(handler, SelectedIdsChangedEvent.TYPE);
    }

    public void setSelected(Set<String> ids) {
        if(ids == null)
            ids = Sets.newHashSet();
        for(Map.Entry<String, WidgetPanel> entry : widgetPanels.entrySet())
            setVisible(entry.getValue(), ids.contains(entry.getKey()));
    }

    private void setVisible(WidgetPanel widgetPanel, boolean visible) {
        if(visible)
            showHandlers.get(widgetPanel).onShow(null);
        widgetPanel.setIn(visible);
    }

    public void setSelected(String id, boolean selected) {
        if(widgetPanels.get(id) != null) {
            if(selected)
                widgetPanels.get(id).setIn(true);
            else
                widgetPanels.get(id).setIn(false);
        }
    }

    protected abstract Widget getWidget(String id, OBJECT object);

    private class ShowObjectHandler implements ShowHandler {

        private final WidgetPanel widgetPanel;
        private final String id;
        private boolean shown = false;

        private ShowObjectHandler(WidgetPanel widgetPanel, String id) {
            this.widgetPanel = widgetPanel;
            this.id = id;
        }

        @Override
        public void onShow(ShowEvent showEvent) {
            if(!shown) {
                widgetPanel.addToBody(getWidget(id, list.get(id)));
                shown = true;
            }
        }
    }
}
