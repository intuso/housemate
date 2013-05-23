package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/11/12
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public class LabelSimpleValue extends Value {

    interface StringValueUiBinder extends UiBinder<Widget, LabelSimpleValue> {
    }

    private static StringValueUiBinder ourUiBinder = GWT.create(StringValueUiBinder.class);

    @UiField
    Label label;

    public LabelSimpleValue() {
        super();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void valueChanged(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        label.setText(value.getValue());
    }
}
