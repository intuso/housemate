package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.google.common.base.Joiner;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.ObjectTypeWrappable;
import com.intuso.housemate.api.object.type.TypeValue;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.ArgumentEditedEvent;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/04/13
 * Time: 09:07
 * To change this template use File | Settings | File Templates.
 */
public class ObjectBrowserInput extends FlowPanel implements ArgumentInput {

    private final ObjectNode rootNode;

    public ObjectBrowserInput(ObjectTypeWrappable typeWrappable) {
        super();
        rootNode = new ObjectNode(Housemate.ENVIRONMENT.getResources().getRoot());
        rootNode.addObjectSelectedHandler(new ObjectSelectedHandler<ProxyObject<?, ?, ?, ?, ?, ?, ?>>() {
            @Override
            public void objectSelected(ObjectSelectedEvent<ProxyObject<?, ?, ?, ?, ?, ?, ?>> event) {
                fireEvent(new ArgumentEditedEvent(new TypeValue(Joiner.on("/").join(event.getObject().getPath()))));
            }
        });
        add(rootNode);
    }

    @Override
    public void setValue(Value<?, ?> value) {
        if(value.getValue() != null) {
            HousemateObject<?, ?, ?, ?, ?> object = Housemate.ENVIRONMENT.getResources().getRoot().getWrapper(value.getValue().split("/"));
            if(object instanceof ProxyObject)
                rootNode.showObject((ProxyObject<?, ?, ?, ?, ?, ?, ?>) object);
        }
    }

    @Override
    public HandlerRegistration addArgumentEditedHandler(ArgumentEditedHandler handler) {
        return addHandler(handler, ArgumentEditedEvent.TYPE);
    }

}
