package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.HasObjectSelectedHandlers;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;

import java.util.Map;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 24/05/13
* Time: 22:16
* To change this template use File | Settings | File Templates.
*/
public class Node extends Composite
        implements HasObjectSelectedHandlers, ObjectSelectedHandler<ProxyObject<?, ?, ?, ?, ?, ?, ?>> {

    interface ObjectNodeUiBinder extends UiBinder<FlowPanel, Node> {
    }

    private static ObjectNodeUiBinder ourUiBinder = GWT.create(ObjectNodeUiBinder.class);

    interface Style extends CssResource {
        String currentSelection();
        String newSelection();
        String heading();
    }

    @UiField
    Style style;

    @UiField
    protected Icon icon;
    @UiField
    protected Anchor heading;
    @UiField
    protected FlowPanel children;

    private final Node root;
    private final ProxyObject<?, ?, ?, ?, ?, ?, ?> object;

    private final Map<String, Node> childNodes = Maps.newHashMap();
    private boolean childrenAdded = false;
    private boolean expanded = false;

    public Node(ProxyObject<?, ?, ?, ?, ?, ?, ?> object) {
        this(null, object);
    }

    private Node(Node root, final ProxyObject<?, ?, ?, ?, ?, ?, ?> object) {

        this.root = root != null ? root : this;
        this.object = object;

        initWidget(ourUiBinder.createAndBindUi(this));

        heading.setText(object.getName());
        heading.setTitle(object.getDescription());

        children.getElement().getStyle().setPaddingLeft(10, com.google.gwt.dom.client.Style.Unit.PX);

        heading.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fireEvent(new ObjectSelectedEvent<ProxyObject<?, ?, ?, ?, ?, ?, ?>>(object));
                Node.this.root.showObject(object, style.newSelection());
            }
        });

        if(object.getChildren().size() > 0) {
            icon.setType(IconType.CHEVRON_RIGHT);
            icon.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    show(!expanded);
                }
            }, ClickEvent.getType());
        } else
            icon.setVisible(false);

        children.setVisible(false);
    }

    private void show(boolean show) {
        if(!childrenAdded && show) {
            childrenAdded = true;
            for(ProxyObject<?, ?, ?, ?, ?, ?, ?> child : object.getChildren()) {
                Node childNode = new Node(root, child);
                childNodes.put(child.getId(), childNode);
                children.add(childNode);
                childNode.addObjectSelectedHandler(this);
            }
        }
        expanded = show;
        children.setVisible(expanded);
        icon.setType(expanded ? IconType.CHEVRON_DOWN : IconType.CHEVRON_RIGHT);
    }

    public void showObject(ProxyObject<?, ?, ?, ?, ?, ?, ?> object) {
        root.showObject(object, style.currentSelection());
    }

    private void showObject(ProxyObject<?, ?, ?, ?, ?, ?, ?> object, String style) {
        unhighlightAll(style);
        showObject(object.getPath(), 0, style);
    }

    private void unhighlightAll(String style) {
        heading.removeStyleName(style);
        for(Node child : childNodes.values())
            child.unhighlightAll(style);
    }

    private void showObject(String[] path, int depth, String style) {
        heading.addStyleName(style);
        if(depth >= path.length - 1)
            return;
        show(true);
        Node node = childNodes.get(path[depth + 1]);
        if(node != null)
            node.showObject(path, depth + 1, style);
    }

    @Override
    public HandlerRegistration addObjectSelectedHandler(ObjectSelectedHandler handler) {
        return addHandler(handler, ObjectSelectedEvent.TYPE);
    }

    @Override
    public void objectSelected(ProxyObject<?, ?, ?, ?, ?, ?, ?> object) {
        fireEvent(new ObjectSelectedEvent<ProxyObject<?, ?, ?, ?, ?, ?, ?>>(object));
    }
}
