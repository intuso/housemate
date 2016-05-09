package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.bootstrap.widget.object.SettingsModal;
import com.intuso.housemate.web.client.bootstrap.widget.value.BooleanValueDisplay;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class Condition extends Composite {

    interface ConditionUiBinder extends UiBinder<Widget, Condition> {}

    private static ConditionUiBinder ourUiBinder = GWT.create(ConditionUiBinder.class);

    @UiField
    BooleanValueDisplay value;

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;
    private final GWTProxyCondition condition;

    public Condition(GWTProxyList<Type.Data<?>, GWTProxyType> types, GWTProxyCondition condition) {
        this.types = types;
        this.condition = condition;
        initWidget(ourUiBinder.createAndBindUi(this));
        value.setValue(condition.getSatisfiedValue());
    }

    @UiHandler("settings")
    public void onEdit(ClickEvent event) {
        new SettingsModal(condition.getName(), new ConditionSettings(types, condition));
    }
}
