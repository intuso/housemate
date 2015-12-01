package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.ObjectTypeData;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;

import java.util.List;

/**
 */
public class ObjectBrowserInput extends FlowPanel implements TypeInput, ObjectSelectedHandler {

    private final TypeInstances typeInstances;
    private final Node rootNode;

    public ObjectBrowserInput(ObjectTypeData typeData, final TypeInstances typeInstances) {

        this.typeInstances = typeInstances;

        addStyleName("object-browser");

        if(typeInstances.getElements().size() == 0)
            typeInstances.getElements().add(new TypeInstance());

        rootNode = new Node(Housemate.INJECTOR.getProxyRoot());
        rootNode.addObjectSelectedHandler(this);
        add(rootNode);
        if(typeInstances.getFirstValue() != null)
            rootNode.selectObject(Lists.newArrayList(typeInstances.getFirstValue().split("/")), 1);
    }

    @Override
     public void objectSelected(ProxyObject<?, ?, ?, ?, ?> object, ProxyObject<?, ?, ?, ?, ?> parent, ChildOverview childOverview) {
        if(object != null) {
            rootNode.selectObject(Lists.newArrayList(object.getPath()), 0);
            typeInstances.getElements().get(0).setValue(Joiner.on("/").join(object.getPath()));
        } else {
            List<String> path = Lists.newArrayList(parent.getPath());
            path.add(childOverview.getId());
            rootNode.selectObject(path, 0);
            typeInstances.getElements().get(0).setValue(Joiner.on("/").join(parent.getPath()) + "/" + childOverview.getId());
        }
        fireEvent(new UserInputEvent());
    }

    @Override
    public TypeInstances getTypeInstances() {
        return typeInstances;
    }

    @Override
    public HandlerRegistration addUserInputHandler(UserInputHandler handler) {
        return addHandler(handler, UserInputEvent.TYPE);
    }
}
