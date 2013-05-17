package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 14/12/12
 * Time: 09:36
 * To change this template use File | Settings | File Templates.
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
