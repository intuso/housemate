package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public abstract class TypeInputList<DATA extends Object.Data<?>, OBJECT extends ProxyObject<DATA, ?, ?, ?, ?>>
        extends NestedList<DATA, OBJECT>
        implements TypeInput, UserInputHandler {

    private GWTProxyList<Type.Data<?>, GWTProxyType> types;
    private Type.Instances typeInstances;
    private Type.InstanceMap typeInstanceMap;

    public void setTypes(GWTProxyList<Type.Data<?>, GWTProxyType> types) {
        this.types = types;
    }

    public void setTypeInstances(Type.Instances typeInstances) {
        this.typeInstances = typeInstances;
        while(typeInstances.getElements().size() > 0 && typeInstances.getElements().get(0) == null)
            typeInstances.getElements().remove(0);
        if(typeInstances.getElements().size() == 0)
            typeInstances.getElements().add(new Type.Instance());
        typeInstanceMap = typeInstances.getElements().get(0).getChildValues();
    }

    protected IsWidget getWidget(String typeId, String key) {
        Type.Instances typeInstances = typeInstanceMap.getChildren().get(key);
        if(typeInstances == null) {
            typeInstances = new Type.Instances();
            typeInstanceMap.getChildren().put(key, typeInstances);
        }
        return TypeInput.FACTORY.create(types, typeId, typeInstances, this);
    }

    @Override
    public HandlerRegistration addUserInputHandler(UserInputHandler handler) {
        return addHandler(handler, UserInputEvent.TYPE);
    }

    @Override
    public Type.Instances getTypeInstances() {
        return typeInstances;
    }

    @Override
    public void onUserInput(UserInputEvent event) {
        fireEvent(new UserInputEvent());
    }
}
