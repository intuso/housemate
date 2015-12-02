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
import com.intuso.housemate.client.v1_0.proxy.api.AvailableChildrenListener;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;
import com.intuso.housemate.web.client.handler.HasObjectSelectedHandlers;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.List;
import java.util.Map;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 24/05/13
* Time: 22:16
* To change this template use File | Settings | File Templates.
*/
public class Node extends Composite implements HasObjectSelectedHandlers,
        ObjectSelectedHandler,
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
                Node.this.fireEvent(new ObjectSelectedEvent(object, parent, childOverview));
            }
        });

        if(object == null || object.getChildren().size() > 0 || object.getAvailableChildren().size() > 0) {
            icon.setType(IconType.CHEVRON_RIGHT);
            icon.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    show(!expanded, null);
                }
            }, ClickEvent.getType());
        } else
            icon.setVisible(false);

        children.setVisible(false);
    }

    private void show(boolean show, final OnShowCallback callback) {
        expanded = show;
        children.setVisible(expanded);
        icon.setType(expanded ? IconType.CHEVRON_DOWN : IconType.CHEVRON_RIGHT);
        if(show && !childrenAdded) {
            childrenAdded = true;
            if(object != null) {
                object.addAvailableChildrenListener(this, true);
                if(callback != null)
                    callback.shown();
            } else {
                parent.load(new LoadManager(new LoadManager.Callback() {
                    @Override
                    public void failed(List<String> errors) {
                        // todo show an error
                    }

                    @Override
                    public void succeeded() {
                        object = parent.getChild(childOverview.getId());
                        object.addAvailableChildrenListener(Node.this, true);
                        if(callback != null)
                            callback.shown();
                    }
                }, new TreeLoadInfo(childOverview.getId())));
            }
        }
    }

    public void selectObject(final List<String> path, final int depth) {
        if(depth >= path.size() - 1) {
            unselectAll(); // unselect all children
            addStyleName("selected");
            return;
        }
        addStyleName("selected");
        show(true, new OnShowCallback() {
            @Override
            public void shown() {
                for(Map.Entry<String, Node> entry : childNodes.entrySet()) {
                    if(entry.getKey().equals(path.get(depth + 1)))
                        entry.getValue().selectObject(path, depth + 1);
                    else
                        entry.getValue().unselectAll();
                }
            }
        });
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
    public void objectSelected(ProxyObject<?, ?, ?, ?, ?> object, ProxyObject<?, ?, ?, ?, ?> parent, ChildOverview childOverview) {
        fireEvent(new ObjectSelectedEvent(object, parent, childOverview));
    }

    @Override
    public void childAdded(ProxyObject<?, ?, ?, ?, ?> object, ChildOverview childOverview) {
        if(!childNodes.containsKey(childOverview.getId())) {
            Node childNode = object.getChild(childOverview.getId()) != null
                    ? new Node(object.getChild(childOverview.getId()))
                    : new Node(object, childOverview);
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

    public interface OnShowCallback {
        void shown();
    }
}
