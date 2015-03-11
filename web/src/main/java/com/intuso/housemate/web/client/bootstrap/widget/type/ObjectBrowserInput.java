package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.common.base.Joiner;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.ObjectTypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;

/**
 */
public class ObjectBrowserInput extends FlowPanel implements TypeInput, ObjectSelectedHandler<ProxyObject<?, ?, ?, ?, ?>> {

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
        if(typeInstances.getFirstValue() != null) {
            HousemateObject<?, ?, ?, ?> object =
                    HousemateObject.getChild(Housemate.INJECTOR.getProxyRoot(),
                    typeInstances.getFirstValue().split("/"), 1);
            if(object instanceof ProxyObject)
                rootNode.selectObject((ProxyObject<?, ?, ?, ?, ?>) object);
        }
    }

    @Override
     public void objectSelected(ProxyObject<?, ?, ?, ?, ?> object) {
        rootNode.selectObject(object);
        typeInstances.getElements().get(0).setValue(Joiner.on("/").join(object.getPath()));
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
