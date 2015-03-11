package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.SettingsModal;
import com.intuso.housemate.web.client.bootstrap.widget.value.BooleanValueDisplay;
import com.intuso.housemate.web.client.object.GWTProxyCondition;

/**
 */
public class Condition extends Composite {

    interface ConditionUiBinder extends UiBinder<Widget, Condition> {}

    private static ConditionUiBinder ourUiBinder = GWT.create(ConditionUiBinder.class);

    @UiField
    BooleanValueDisplay value;
    @UiField
    ConditionList conditionList;

    private final GWTProxyCondition condition;

    public Condition(GWTProxyCondition condition) {
        this.condition = condition;
        initWidget(ourUiBinder.createAndBindUi(this));
        value.setValue(condition.getSatisfiedValue());
        conditionList.setList(condition.getConditions());
        conditionList.setAddCommand(condition.getAddConditionCommand());
    }

    @UiHandler("settings")
    public void onEdit(ClickEvent event) {
        new SettingsModal(condition.getName(), new ConditionSettings(condition));
    }
}
