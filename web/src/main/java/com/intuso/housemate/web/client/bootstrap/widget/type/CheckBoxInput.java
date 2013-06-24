package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;

/**
 */
public class CheckBoxInput extends CheckBox implements TypeInput {

    public CheckBoxInput() {
        addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                fireEvent(new TypeInputEditedEvent(new TypeInstance(Boolean.toString(event.getValue()))));
            }
        });
    }

    @Override
    public HandlerRegistration addTypeInputEditedHandler(TypeInputEditedHandler handler) {
        return addHandler(handler, TypeInputEditedEvent.TYPE);
    }

    @Override
    public void setTypeInstance(TypeInstance typeInstance) {
        if(typeInstance == null || typeInstance.getValue() == null)
            setValue(Boolean.FALSE);
        else
            setValue(Boolean.parseBoolean(typeInstance.getValue()), false);
    }
}
