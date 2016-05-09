package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import org.gwtbootstrap3.client.ui.CheckBox;

/**
 */
public class CheckBoxInput extends CheckBox implements TypeInput, ValueChangeHandler<Boolean> {

    private final Type.Instances typeInstances;

    public CheckBoxInput(Type.Instances typeInstances) {
        this.typeInstances = typeInstances;
        if(typeInstances.getFirstValue() == null)
            setValue(Boolean.FALSE);
        else
            setValue(Boolean.parseBoolean(typeInstances.getFirstValue()), false);
        addValueChangeHandler(this);
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
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        typeInstances.getElements().set(0, new Type.Instance(event.getValue().toString()));
        fireEvent(new UserInputEvent());
    }
}
