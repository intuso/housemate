package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.WidgetRow;
import com.intuso.housemate.web.client.bootstrap.widget.object.ConfigurableObject;
import com.intuso.housemate.web.client.bootstrap.widget.value.Value;
import com.intuso.housemate.web.client.object.GWTProxyCondition;

/**
 */
public class Condition extends ConfigurableObject {

    interface ConditionUiBinder extends UiBinder<Widget, Condition> {}

    private static ConditionUiBinder ourUiBinder = GWT.create(ConditionUiBinder.class);

    @UiField(provided = true)
    WidgetRow satisfied;
    @UiField(provided = true)
    ConditionList conditionList;

    private final GWTProxyCondition condition;

    public Condition(GWTProxyCondition condition) {
        this.condition = condition;
        satisfied = new WidgetRow("satisfied", Value.getWidget(condition.getSatisfiedValue()));
        conditionList = new ConditionList(condition.getConditions(), "children", null, false);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected Widget createSettingsWidget() {
        return new ConditionSettings(condition);
    }
}
