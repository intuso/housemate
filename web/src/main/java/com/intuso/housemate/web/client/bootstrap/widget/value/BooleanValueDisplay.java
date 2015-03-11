package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.intuso.housemate.api.object.value.Value;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 */
public class BooleanValueDisplay extends Icon implements ValueDisplay {

    private IconType trueIcon = null;
    private IconType falseIcon = null;

    public BooleanValueDisplay() {
        addStyleName("boolean-value");
    }

    public void setTrueIcon(IconType trueIcon) {
        this.trueIcon = trueIcon;
    }

    public void setFalseIcon(IconType falseIcon) {
        this.falseIcon = falseIcon;
    }

    @Override
    public void valueChanging(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        // do nothing
    }

    @Override
    public void valueChanged(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        if(value.getTypeInstances() != null
                && value.getTypeInstances().getFirstValue() != null
                && value.getTypeInstances().getFirstValue().equals(Boolean.toString(true)))
            setIconType(trueIcon);
        else
            setIconType(falseIcon);
    }

    @Override
    public void setValue(Value<?, ?> value) {
        value.addObjectListener(this);
        valueChanged(value);
    }

    private void setIconType(IconType type) {
        setVisible(type != null);
        setType(type);
    }
}
