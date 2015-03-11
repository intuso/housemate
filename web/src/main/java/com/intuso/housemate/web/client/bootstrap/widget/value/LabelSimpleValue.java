package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.Label;
import com.intuso.housemate.api.object.value.Value;

/**
 */
public class LabelSimpleValue extends Label implements ValueDisplay {

    @Override
    public void valueChanging(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        // do nothing
    }

    @Override
    public void valueChanged(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        if(value.getTypeInstances() == null || value.getTypeInstances().getFirstValue() == null)
            setText("");
        else
            setText(value.getTypeInstances().getFirstValue());
    }

    @Override
    public void setValue(Value<?, ?> value) {
        value.addObjectListener(this);
        valueChanged(value);
    }
}
