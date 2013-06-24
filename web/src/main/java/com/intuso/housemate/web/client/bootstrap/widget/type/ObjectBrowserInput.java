package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.common.base.Joiner;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.ObjectTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;

/**
 */
public class ObjectBrowserInput extends FlowPanel implements TypeInput {

    private final ObjectNode rootNode;

    public ObjectBrowserInput(ObjectTypeWrappable typeWrappable) {
        super();
        rootNode = new ObjectNode(Housemate.ENVIRONMENT.getResources().getRoot());
        rootNode.addObjectSelectedHandler(new ObjectSelectedHandler<ProxyObject<?, ?, ?, ?, ?, ?, ?>>() {
            @Override
            public void objectSelected(ObjectSelectedEvent<ProxyObject<?, ?, ?, ?, ?, ?, ?>> event) {
                fireEvent(new TypeInputEditedEvent(new TypeInstance(Joiner.on("/").join(event.getObject().getPath()))));
            }
        });
        add(rootNode);
    }

    @Override
    public void setTypeInstance(TypeInstance typeInstance) {
        if(typeInstance != null && typeInstance.getValue() != null) {
            HousemateObject<?, ?, ?, ?, ?> object = Housemate.ENVIRONMENT.getResources().getRoot().getObject(
                    typeInstance.getValue().split("/"));
            if(object instanceof ProxyObject)
                rootNode.showObject((ProxyObject<?, ?, ?, ?, ?, ?, ?>) object);
        }
    }

    @Override
    public HandlerRegistration addTypeInputEditedHandler(TypeInputEditedHandler handler) {
        return addHandler(handler, TypeInputEditedEvent.TYPE);
    }

}
