package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.Label;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 */
public class LabelSimpleValue extends Label implements ValueDisplay {

    @Override
    public void valueChanging(GWTProxyValue value) {
        // do nothing
    }

    @Override
    public void valueChanged(GWTProxyValue value) {
        if(value.getValue() == null || value.getValue().getFirstValue() == null)
            setText("");
        else
            setText(value.getValue().getFirstValue());
    }

    @Override
    public void setValue(GWTProxyValue value) {
        value.addObjectListener(this);
        valueChanged(value);
    }
}
