package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 */
public class ThumbsSimpleValue extends Value {

    interface StringValueUiBinder extends UiBinder<Widget, ThumbsSimpleValue> {
    }

    private static StringValueUiBinder ourUiBinder = GWT.create(StringValueUiBinder.class);

    @UiField
    Icon icon;

    public ThumbsSimpleValue() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void valueChanging(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        // do nothing
    }

    @Override
    public void valueChanged(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        if(value.getTypeInstance() != null
                && value.getTypeInstance().getValue() != null
                && value.getTypeInstance().getValue().equals(Boolean.toString(true)))
            icon.setType(IconType.THUMBS_UP);
        else
            icon.setType(IconType.THUMBS_DOWN);
    }
}
