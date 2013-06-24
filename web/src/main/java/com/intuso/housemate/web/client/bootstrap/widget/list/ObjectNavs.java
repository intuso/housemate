package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavPills;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.ResponsiveStyle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.HasObjectSelectedHandlers;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class ObjectNavs<O extends ProxyObject<?, ?, ?, ?, ?, ?, ?>> extends Composite
            implements ListListener<O>, ObjectSelectedHandler<O>, HasObjectSelectedHandlers<O> {

    interface ObjectNavsUiBinder extends UiBinder<Widget, ObjectNavs> {
    }

    private static ObjectNavsUiBinder ourUiBinder = GWT.create(ObjectNavsUiBinder.class);

    @UiField
    NavPills navPills;
    @UiField
    PerformButton addButton;

    private O selectedObject;
    private ListenerRegistration listenerRegistration = null;
    private Map<String, NavLink> elementWidgets = new HashMap<String, NavLink>();

    public ObjectNavs() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setList(GWTProxyList<?, O> list) {
        setList(list, null);
    }

    public void setList(GWTProxyList<?, O> list, GWTProxyCommand addCommand) {

        // remove previous listener
        if(listenerRegistration != null)
            listenerRegistration.removeListener();

        // remove previous list elements
        navPills.clear();
        elementWidgets.clear();

        // add the new listener and make it call us for each existing element
        listenerRegistration = list.addObjectListener(this, true);

        if(addCommand != null) {
            addButton.setCommand(addCommand);
            addButton.setIcon(IconType.PLUS);
            addButton.setText(addCommand.getDescription());
            addButton.setVisible(true);
        } else
            addButton.setVisible(false);
    }

    @Override
    public final void elementAdded(O element) {
        ObjectListNavLink<O> listElement = new ObjectListNavLink<O>(element, this);
        elementWidgets.put(element.getId(), listElement);
        navPills.add(listElement);
    }

    @Override
    public final void elementRemoved(O element) {
        if(selectedObject != null && selectedObject.getId().equals(element.getId())) {
            selectedObject = null;
            fireEvent(new ObjectSelectedEvent<O>(null));
        }
        NavLink navLink = elementWidgets.remove(element.getId());
        if(navLink != null)
            navPills.remove(navLink);
    }

    @Override
    public HandlerRegistration addObjectSelectedHandler(ObjectSelectedHandler<O> handler) {
        return addHandler(handler, ObjectSelectedEvent.TYPE);
    }

    @Override
    public void objectSelected(ObjectSelectedEvent<O> event) {
        highlightObject(event.getObject());
        fireEvent(event);
    }

    public void highlightObject(O object) {
        // deselect previous object
        if(selectedObject != null && elementWidgets.get(selectedObject.getId()) != null)
            elementWidgets.get(selectedObject.getId()).setActive(false);

        // select new object
        selectedObject = object;
        if(selectedObject != null)
            if(elementWidgets.get(selectedObject.getId()) != null)
                elementWidgets.get(selectedObject.getId()).setActive(true);
    }

    public void addResponsiveStyle(ResponsiveStyle style) {
        navPills.addStyle(style);
    }

    public void removeResponsiveStyle(ResponsiveStyle style) {
        navPills.removeStyle(style);
    }
}
