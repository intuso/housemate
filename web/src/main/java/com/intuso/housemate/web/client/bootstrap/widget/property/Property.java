package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.type.TypeInput;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyProperty;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class Property extends SimplePanel implements UserInputHandler {

    private final TypeInstanceMap values;

    private final GWTProxyProperty property;

    public Property(final GWTProxyProperty property) {

        this.property = property;

        values = new TypeInstanceMap();
        if(property.getTypeInstances() != null)
            values.getChildren().put(com.intuso.housemate.api.object.property.Property.VALUE_PARAM, property.getTypeInstances());
        else
            values.getChildren().put(com.intuso.housemate.api.object.property.Property.VALUE_PARAM, new TypeInstances());

        final GWTProxyType type = property.getType();
        if(type != null)
            setWidget(TypeInput.FACTORY.create(type, values.getChildren().get(com.intuso.housemate.api.object.property.Property.VALUE_PARAM), this));
        else {
            Housemate.INJECTOR.getProxyRoot().getTypes().load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    // todo show error
                }

                @Override
                public void allLoaded() {
                    GWTProxyType loadedType = property.getType();
                    if(loadedType != null)
                        setWidget(TypeInput.FACTORY.create(loadedType, values.getChildren().get(com.intuso.housemate.api.object.property.Property.VALUE_PARAM), Property.this));
                }
            }, "loadPropertyType-" + property.getId(), new HousemateObject.TreeLoadInfo(property.getTypeId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
    }

    @UiFactory
    protected Property createDialog() {
        return this;
    }

    @Override
    public void onUserInput(UserInputEvent event) {
        Housemate.INJECTOR.getEventBus().fireEvent(new PerformCommandEvent(property.getSetCommand(), values));
    }
}