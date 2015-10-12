package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class ConditionSettings extends Composite {

    interface ConditionUiBinder extends UiBinder<Widget, ConditionSettings> {}

    private static ConditionUiBinder ourUiBinder = GWT.create(ConditionUiBinder.class);

    @UiField(provided = true)
    Control control;
    @UiField
    ConditionList childConditions;
    @UiField
    PropertyList propertyList;

    public ConditionSettings(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCondition condition) {
        control = new Control(types, condition);
        initWidget(ourUiBinder.createAndBindUi(this));
        /*if(condition.getConditions() != null) {
            childConditions.setList(condition.getConditions());
            if(condition.getAddConditionCommand() != null)
                childConditions.setHeaderWidget(new AddButton(types, condition.getAddConditionCommand()));
        } else*/
            childConditions.setVisible(false);
        propertyList.setTypes(types);
        propertyList.setList(condition.getProperties());
    }
}
