package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.proxy.AvailableChildrenListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.handler.HasObjectSelectedHandlers;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Map;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 24/05/13
* Time: 22:16
* To change this template use File | Settings | File Templates.
*/
public class Node extends Composite implements HasObjectSelectedHandlers,
        ObjectSelectedHandler<ProxyObject<?, ?, ?, ?, ?>>,
        ObjectListener<ProxyObject<?, ?, ?, ?, ?>>,
        AvailableChildrenListener<ProxyObject<?, ?, ?, ?, ?>> {

    interface ObjectNodeUiBinder extends UiBinder<FlowPanel, Node> {}

    private static ObjectNodeUiBinder ourUiBinder = GWT.create(ObjectNodeUiBinder.class);

    @UiField
    protected Icon icon;
    @UiField
    protected Anchor heading;
    @UiField
    protected FlowPanel children;

    private ProxyObject<?, ?, ?, ?, ?> object;
    private ProxyObject<?, ?, ?, ?, ?> parent;
    private ChildOverview childOverview;

    private final Map<String, Node> childNodes = Maps.newHashMap();
    private final Map<Node, HandlerRegistration> childListeners = Maps.newHashMap();
    private boolean childrenAdded = false;
    private boolean expanded = false;

    public Node(ProxyObject<?, ?, ?, ?, ?> object) {
        this.object = object;
        initView();
    }

    private Node(final ProxyObject<?, ?, ?, ?, ?> parent, final ChildOverview childOverview) {
        this.parent = parent;
        this.childOverview = childOverview;
        initView();
    }

    private void initView() {

        initWidget(ourUiBinder.createAndBindUi(this));

        heading.setText(object != null ? object.getName() : childOverview.getName());
        heading.setTitle(object != null ? object.getDescription() : childOverview.getDescription());

        children.getElement().getStyle().setPaddingLeft(10, com.google.gwt.dom.client.Style.Unit.PX);

        heading.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Node.this.fireEvent(new ObjectSelectedEvent<ProxyObject<?, ?, ?, ?, ?>>(object));
            }
        });

        if(object == null || object.getChildren().size() > 0) {
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
        if(show && !childrenAdded) {
            childrenAdded = true;
            if(object != null) {
                object.addChildListener(this, true, false);
                object.addAvailableChildrenListener(this, true);
            } else {
                parent.load(new LoadManager(new LoadManager.Callback() {
                    @Override
                    public void failed(HousemateObject.TreeLoadInfo path) {
                        // todo show an error
                    }

                    @Override
                    public void allLoaded() {
                        object = parent.getChild(childOverview.getId());
                        object.addChildListener(Node.this, true, false);
                        object.addAvailableChildrenListener(Node.this, true);
                    }
                }, "treeBrowseNodeLoader-" + childOverview.getId(), new HousemateObject.TreeLoadInfo(childOverview.getId())));
            }
        }
        expanded = show;
        children.setVisible(expanded);
        icon.setType(expanded ? IconType.CHEVRON_DOWN : IconType.CHEVRON_RIGHT);
    }

    public void selectObject(ProxyObject<?, ?, ?, ?, ?> object) {
        selectObject(object.getPath(), 0);
    }

    private void selectObject(String[] path, int depth) {
        if(depth >= path.length - 1) {
            unselectAll(); // unselect all children
            addStyleName("selected");
            return;
        }
        addStyleName("selected");
        show(true);
        for(Map.Entry<String, Node> entry : childNodes.entrySet()) {
            if(entry.getKey().equals(path[depth + 1]))
                entry.getValue().selectObject(path, depth + 1);
            else
                entry.getValue().unselectAll();
        }
    }

    private void unselectAll() {
        removeStyleName("selected");
        for(Node child : childNodes.values())
            child.unselectAll();
    }

    @Override
    public HandlerRegistration addObjectSelectedHandler(ObjectSelectedHandler handler) {
        return addHandler(handler, ObjectSelectedEvent.TYPE);
    }

    @Override
    public void objectSelected(ProxyObject<?, ?, ?, ?, ?> object) {
        fireEvent(new ObjectSelectedEvent<ProxyObject<?, ?, ?, ?, ?>>(object));
    }

    @Override
    public void childObjectAdded(String childId, ProxyObject<?, ?, ?, ?, ?> child) {
        if(!childNodes.containsKey(child.getId())) {
            Node childNode = new Node(child);
            childNodes.put(child.getId(), childNode);
            children.add(childNode);
            childListeners.put(childNode, childNode.addObjectSelectedHandler(this));
        }
    }

    @Override
    public void childObjectRemoved(String childId, ProxyObject<?, ?, ?, ?, ?> child) {
        Node childNode = childNodes.get(child.getId());
        if(childNode != null) {
            children.remove(childNode);
            childListeners.get(childNode).removeHandler();
        }
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // do nothing
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // do nothing
    }

    @Override
    public void childAdded(ProxyObject<?, ?, ?, ?, ?> object, ChildOverview childOverview) {
        if(!childNodes.containsKey(childOverview.getId())) {
            Node childNode = new Node(object, childOverview);
            childNodes.put(childOverview.getId(), childNode);
            children.add(childNode);
            childListeners.put(childNode, childNode.addObjectSelectedHandler(this));
        }
    }

    @Override
    public void childRemoved(ProxyObject<?, ?, ?, ?, ?> object, ChildOverview childOverview) {
        Node childNode = childNodes.get(childOverview.getId());
        if(childNode != null) {
            children.remove(childNode);
            childListeners.get(childNode).removeHandler();
        }
    }
}
