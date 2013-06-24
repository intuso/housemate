package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 */
public class ValueContainer extends SimplePanel {

    public ValueContainer() {
    }

    public ValueContainer(GWTProxyValue value) {
        setValue(value);
    }

    public void setValue(GWTProxyValue value) {
        clear();
        if(value != null)
            add(Value.getWidget(value));
    }
}
