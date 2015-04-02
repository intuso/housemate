package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public abstract class TypeInputList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<DATA, ?, ?, ?, ?>>
        extends NestedList<DATA, OBJECT>
        implements TypeInput, UserInputHandler {

    private GWTProxyList<TypeData<?>, GWTProxyType> types;
    private TypeInstances typeInstances;
    private TypeInstanceMap typeInstanceMap;

    public void setTypes(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        this.types = types;
    }

    public void setTypeInstances(TypeInstances typeInstances) {
        this.typeInstances = typeInstances;
        while(typeInstances.getElements().size() > 0 && typeInstances.getElements().get(0) == null)
            typeInstances.getElements().remove(0);
        if(typeInstances.getElements().size() == 0)
            typeInstances.getElements().add(new TypeInstance());
        typeInstanceMap = typeInstances.getElements().get(0).getChildValues();
    }

    protected IsWidget getWidget(String typeId, String key) {
        TypeInstances typeInstances = typeInstanceMap.getChildren().get(key);
        if(typeInstances == null) {
            typeInstances = new TypeInstances();
            typeInstanceMap.getChildren().put(key, typeInstances);
        }
        return TypeInput.FACTORY.create(types, typeId, typeInstances, this);
    }

    @Override
    public HandlerRegistration addUserInputHandler(UserInputHandler handler) {
        return addHandler(handler, UserInputEvent.TYPE);
    }

    @Override
    public TypeInstances getTypeInstances() {
        return typeInstances;
    }

    @Override
    public void onUserInput(UserInputEvent event) {
        fireEvent(new UserInputEvent());
    }
}
