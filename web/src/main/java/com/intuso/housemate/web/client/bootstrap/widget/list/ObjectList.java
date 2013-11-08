package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.constants.Constants;
import com.github.gwtbootstrap.client.ui.event.HideEvent;
import com.github.gwtbootstrap.client.ui.event.HideHandler;
import com.github.gwtbootstrap.client.ui.event.ShowEvent;
import com.github.gwtbootstrap.client.ui.event.ShowHandler;
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
public abstract class ObjectList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<?, ?, DATA, ?, ?, ?, ?>>
        extends Accordion implements AvailableChildrenListener<GWTProxyList<DATA, OBJECT>>, HasSelectedIdsChangedHandlers {

    private final GWTProxyList<DATA, OBJECT> list;
    private final List<String> filteredIds;
    private final boolean includeFiltered;

    private final Map<String, AccordionGroup> accordionGroups = Maps.newHashMap();
    private final Map<AccordionGroup, ShowHandler> showHandlers = Maps.newHashMap();
    private final Set<String> selectedIds = Sets.newHashSet();

    public ObjectList(GWTProxyList<DATA, OBJECT> list, String title, List<String> filteredIds, boolean includeFiltered) {

        this.list = list;
        this.filteredIds = filteredIds;
        this.includeFiltered = includeFiltered;

        // only show when there is something in the list
        setVisible(false);

        // add the widget style name
        addStyleName("object-list");

        add(new Heading(4, title));

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
        AccordionGroup accordionGroup = accordionGroups.get(childOverview.getId());
        if(accordionGroup != null)
            remove(accordionGroup);
    }

    private void addEntry(final ChildOverview childOverview) {
        if(childOverview != null) {
            final AccordionGroup accordionGroup = new AccordionGroup();
            accordionGroup.setHeading(childOverview.getName());
            // lazy load the widget
            ShowHandler showHandler = new ShowObjectHandler(accordionGroup, childOverview.getId());
            showHandlers.put(accordionGroup, showHandler);
            accordionGroup.addShowHandler(showHandler);
            accordionGroup.addShowHandler(new ShowHandler() {
                @Override
                public void onShow(ShowEvent showEvent) {
                    if (selectedIds.add(childOverview.getId()))
                        ObjectList.this.fireEvent(new SelectedIdsChangedEvent(selectedIds));
                }
            });
            accordionGroup.addHideHandler(new HideHandler() {
                @Override
                public void onHide(HideEvent hideEvent) {
                    if (selectedIds.remove(childOverview.getId()))
                        ObjectList.this.fireEvent(new SelectedIdsChangedEvent(selectedIds));
                }
            });
            accordionGroups.put(childOverview.getId(), accordionGroup);
            add(accordionGroup);
        }
    }

    @Override
    public HandlerRegistration addSelectedIdsChangedHandler(SelectedIdsChangedHandler handler) {
        return addHandler(handler, SelectedIdsChangedEvent.TYPE);
    }

    public void setSelected(Set<String> ids) {
        // TODO fix this, AccordionGroup.hide() makes the collapse never expand, even when clicked!
        // TODO The Collapse class has a hide(bool autoHidden) function but is not accessible to us :-(
        if(ids == null)
            ids = Sets.newHashSet();
        for(Map.Entry<String, AccordionGroup> entry : accordionGroups.entrySet())
            setVisible(entry.getValue(), ids.contains(entry.getKey()));
    }

    private void setVisible(AccordionGroup accordionGroup, boolean visible) {
        if(visible)
            showHandlers.get(accordionGroup).onShow(null);
        Widget bodyWidget = accordionGroup.getWidget(1);
        if(visible)
            bodyWidget.addStyleName(Constants.IN);
        else
            bodyWidget.removeStyleName(Constants.IN);
    }

    public void setSelected(String id, boolean selected) {
        if(accordionGroups.get(id) != null) {
            if(selected)
                accordionGroups.get(id).show();
            else
                accordionGroups.get(id).hide();
        }
    }

    protected abstract Widget getWidget(String id, OBJECT object);

    private class ShowObjectHandler implements ShowHandler {

        private final AccordionGroup accordionGroup;
        private final String id;
        private boolean shown = false;

        private ShowObjectHandler(AccordionGroup accordionGroup, String id) {
            this.accordionGroup = accordionGroup;
            this.id = id;
        }

        @Override
        public void onShow(ShowEvent showEvent) {
            if(!shown) {
                accordionGroup.add(getWidget(id, list.get(id)));
                shown = true;
            }
        }
    }
}
