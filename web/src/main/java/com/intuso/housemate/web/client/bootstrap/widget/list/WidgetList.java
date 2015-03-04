package com.intuso.housemate.web.client.bootstrap.widget.list;

import org.gwtbootstrap3.client.ui.Label;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.WidgetRow;
import com.intuso.housemate.web.client.object.GWTProxyList;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/09/13
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class WidgetList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<? extends DATA, ?, ?, ?, ?>>
        extends FlowPanel implements ListListener<OBJECT> {

    private final GWTProxyList<DATA, OBJECT> list;
    private final List<String> filteredIds;

    private final Map<String, WidgetRow> rows = Maps.newHashMap();

    public WidgetList(GWTProxyList<DATA, OBJECT> list, String title, List<String> filteredIds, boolean showIfEmpty) {

        this.list = list;
        this.filteredIds = filteredIds;

        // hide when empty if showOnEmpty is false
        setVisible(showIfEmpty);

        addStyleName("widget-list");

        if(title != null)
            add(new Label(title));

    }

    protected void loadRows() {
        this.list.addObjectListener(this, true);
    }

    @Override
    public final void elementAdded(OBJECT element) {
        if(filteredIds == null || filteredIds.contains(element.getId())) {
            setVisible(true);
            addRow(element);
        }
    }

    @Override
    public final void elementRemoved(OBJECT element) {
        WidgetRow row = rows.get(element.getId());
        if(row != null)
            remove(row);
    }

    private void addRow(final OBJECT object) {
        if(object != null) {
            WidgetRow row = new WidgetRow(object.getName(), getWidget(object));
            rows.put(object.getId(), row);
            add(row);
        }
    }

    protected abstract IsWidget getWidget(OBJECT object);
}
