package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.ConfigurableObject;
import com.intuso.housemate.web.client.object.GWTProxyCondition;

/**
 */
public class Condition extends ConfigurableObject {

    interface ConditionUiBinder extends UiBinder<Widget, Condition> {}

    private static ConditionUiBinder ourUiBinder = GWT.create(ConditionUiBinder.class);

    private final GWTProxyCondition condition;

    public Condition(GWTProxyCondition condition) {
        this.condition = condition;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected Widget createSettingsWidget() {
        return new ConditionSettings(condition);
    }
}
