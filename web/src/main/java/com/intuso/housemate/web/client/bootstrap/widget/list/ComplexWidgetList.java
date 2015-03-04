package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.google.common.collect.Maps;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.object.GWTProxyList;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.PanelGroup;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/09/13
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class ComplexWidgetList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<DATA, ?, ?, ?, ?>>
        extends PanelGroup implements ListListener<OBJECT> {

    private final GWTProxyList<DATA, OBJECT> list;
    private final List<String> filteredIds;

    private final Map<String, WidgetPanel> widgetPanels = Maps.newHashMap();

    public ComplexWidgetList(GWTProxyList<DATA, OBJECT> list, String title, List<String> filteredIds, boolean showIfEmpty) {

        this.list = list;
        this.filteredIds = filteredIds;

        // hide when empty if showOnEmpty is false
        setVisible(showIfEmpty);

        addStyleName("widget-list");

        add(new Label(title));

        this.list.addObjectListener(this, true);
    }

    @Override
    public void elementAdded(OBJECT element) {
        if(filteredIds == null || filteredIds.contains(element.getId())) {
            setVisible(true);
            addRow(element);
        }
    }

    @Override
    public void elementRemoved(OBJECT element) {
        WidgetPanel row = widgetPanels.get(element.getId());
        if(row != null)
            remove(row);
    }

    private void addRow(final OBJECT object) {
        if(object != null) {
            WidgetPanel widgetPanel = new WidgetPanel();
            widgetPanel.setHeading(object.getName());
            widgetPanel.addToBody(getWidget(object));
            widgetPanels.put(object.getId(), widgetPanel);
            add(widgetPanel);
        }
    }

    protected abstract Widget getWidget(OBJECT object);
}
