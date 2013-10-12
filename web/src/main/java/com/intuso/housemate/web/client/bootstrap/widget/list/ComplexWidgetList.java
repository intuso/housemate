package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.object.proxy.ProxyObject;
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
public abstract class ComplexWidgetList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<?, ?, DATA, ?, ?, ?, ?>>
        extends Accordion implements ListListener<OBJECT> {

    private final GWTProxyList<DATA, OBJECT> list;
    private final List<String> filteredIds;

    private final Map<String, AccordionGroup> accordionGroups = Maps.newHashMap();

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
        AccordionGroup row = accordionGroups.get(element.getId());
        if(row != null)
            remove(row);
    }

    private void addRow(final OBJECT object) {
        if(object != null) {
            AccordionGroup accordionGroup = new AccordionGroup();
            accordionGroup.setHeading(object.getName());
            accordionGroup.add(getWidget(object));
            accordionGroups.put(object.getId(), accordionGroup);
            add(accordionGroup);
        }
    }

    protected abstract Widget getWidget(OBJECT object);
}
