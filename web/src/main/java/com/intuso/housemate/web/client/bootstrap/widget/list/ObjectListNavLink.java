package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.HasObjectSelectedHandlers;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public class ObjectListNavLink<O extends ProxyObject<?, ?, ?, ?, ?, ?, ?>> extends NavLink
            implements ClickHandler, HasObjectSelectedHandlers<O> {

    private O object;

    public ObjectListNavLink(O object) {
        this.object = object;
        setText(object.getName());
        Icon icon = new Icon(IconType.CHEVRON_RIGHT);
        icon.getElement().getStyle().setFloat(Style.Float.RIGHT);
        add(icon);
        addClickHandler(this);
    }

    public ObjectListNavLink(O object, ObjectSelectedHandler<O> handler) {
        this(object);
        addObjectSelectedHandler(handler);
    }

    public HandlerRegistration addObjectSelectedHandler(ObjectSelectedHandler<O> handler) {
        return addHandler(handler, ObjectSelectedEvent.TYPE);
    }

    public O getObject() {
        return object;
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        fireEvent(new ObjectSelectedEvent<O>(object));
    }
}
