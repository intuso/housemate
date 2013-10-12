package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyCondition;

/**
 */
public class ConditionSettings extends Composite {

    interface ConditionUiBinder extends UiBinder<Widget, ConditionSettings> {}

    private static ConditionUiBinder ourUiBinder = GWT.create(ConditionUiBinder.class);

    @UiField(provided = true)
    Control control;
    @UiField(provided = true)
    PropertyList propertyList;
    @UiField(provided = true)
    PerformButton addChildCondition;

    public ConditionSettings(GWTProxyCondition condition) {
        control = new Control(condition);
        propertyList = new PropertyList(condition.getProperties(), "properties", null, true);
        addChildCondition = new PerformButton(condition.getAddConditionCommand());
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
