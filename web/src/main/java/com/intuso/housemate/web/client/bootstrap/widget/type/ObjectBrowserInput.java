package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.common.base.Joiner;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.ObjectTypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;

/**
 */
public class ObjectBrowserInput extends FlowPanel implements TypeInput {

    private final Node rootNode;

    public ObjectBrowserInput(ObjectTypeData typeData, final TypeInstances typeInstances) {

        if(typeInstances.size() == 0)
            typeInstances.add(new TypeInstance());

        rootNode = new Node(Housemate.ENVIRONMENT.getResources().getRoot());
        rootNode.addObjectSelectedHandler(new ObjectSelectedHandler<ProxyObject<?, ?, ?, ?, ?, ?, ?>>() {
            @Override
            public void objectSelected(ProxyObject<?, ?, ?, ?, ?, ?, ?> object) {
                typeInstances.get(0).setValue(Joiner.on("/").join(object.getPath()));
            }
        });
        add(rootNode);
        if(typeInstances.getFirstValue() != null) {
            HousemateObject<?, ?, ?, ?, ?> object =
                    HousemateObject.getChild(Housemate.ENVIRONMENT.getResources().getRoot(),
                    typeInstances.getFirstValue().split("/"), 1);
            if(object instanceof ProxyObject)
                rootNode.showObject((ProxyObject<?, ?, ?, ?, ?, ?, ?>) object);
        }
    }
}
