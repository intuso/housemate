package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 */
public class CheckBoxInput extends CheckBox implements TypeInput {

    public CheckBoxInput(final TypeInstances typeInstances) {
        if(typeInstances.getFirstValue() == null)
            setValue(Boolean.FALSE);
        else
            setValue(Boolean.parseBoolean(typeInstances.getFirstValue()), false);
        addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                typeInstances.getElements().set(0, new TypeInstance(event.getValue().toString()));
            }
        });
    }
}
