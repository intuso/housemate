package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created by tomc on 11/03/15.
 */
public class CompoundTypeInput extends Button implements TypeInput, ClickHandler, UserInputHandler {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;
    private final GWTProxyList<SubType.Data, GWTProxySubType> list;
    private final Type.Instances typeInstances;

    public CompoundTypeInput(GWTProxyList<Type.Data<?>, GWTProxyType> types, GWTProxyList<SubType.Data, GWTProxySubType> list, Type.Instances typeInstances) {
        this.types = types;
        this.list = list;
        this.typeInstances = typeInstances;
        setIcon(IconType.EDIT);
        addClickHandler(this);
    }

    @Override
    public Type.Instances getTypeInstances() {
        return typeInstances;
    }

    @Override
    public HandlerRegistration addUserInputHandler(UserInputHandler handler) {
        return addHandler(handler, UserInputEvent.TYPE);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        new CompoundTypeInputModal(types, list, typeInstances).addUserInputHandler(this);
    }

    @Override
    public void onUserInput(UserInputEvent event) {
        fireEvent(event);
    }
}
