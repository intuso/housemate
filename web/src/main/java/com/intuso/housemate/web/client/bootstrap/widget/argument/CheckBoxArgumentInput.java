package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.event.ArgumentEditedEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/12/12
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class CheckBoxArgumentInput extends CheckBox implements ArgumentInput {

    public CheckBoxArgumentInput() {
        addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                fireEvent(new ArgumentEditedEvent(new TypeInstance(Boolean.toString(event.getValue()))));
            }
        });
    }

    @Override
    public HandlerRegistration addArgumentEditedHandler(ArgumentEditedHandler handler) {
        return addHandler(handler, ArgumentEditedEvent.TYPE);
    }

    @Override
    public void setTypeInstance(TypeInstance typeInstance) {
        if(typeInstance == null || typeInstance.getValue() == null)
            setValue(Boolean.FALSE);
        else
            setValue(Boolean.parseBoolean(typeInstance.getValue()), false);
    }
}
