package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.type.TypeInput;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyProperty;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class Property extends SimplePanel implements UserInputHandler {

    private final Type.InstanceMap values;

    private final GWTProxyProperty property;

    public Property(final GWTProxyList<Type.Data<?>, GWTProxyType> types, final GWTProxyProperty property) {

        this.property = property;

        values = new Type.InstanceMap();
        if(property.getValue() != null)
            values.getChildren().put(com.intuso.housemate.client.v1_0.api.object.Property.Data.VALUE_PARAM, property.getValue());
        else
            values.getChildren().put(com.intuso.housemate.client.v1_0.api.object.Property.Data.VALUE_PARAM, new Type.Instances());

        setWidget(TypeInput.FACTORY.create(types, property.getTypeId(), values.getChildren().get(com.intuso.housemate.client.v1_0.api.object.Property.Data.VALUE_PARAM), this));
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