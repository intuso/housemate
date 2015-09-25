package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.condition.ConditionList;
import com.intuso.housemate.web.client.bootstrap.widget.task.TaskList;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class AutomationBody extends Composite {

    interface AutomationUiBinder extends UiBinder<Widget, AutomationBody> {}

    private static AutomationUiBinder ourUiBinder = GWT.create(AutomationUiBinder.class);

    @UiField
    ConditionList conditions;
    @UiField
    TaskList satisfiedTasks;
    @UiField
    TaskList unsatisfiedTasks;

    public AutomationBody(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyAutomation automation) {
        initWidget(ourUiBinder.createAndBindUi(this));
        conditions.setTypes(types);
        conditions.setList(automation.getConditions());
        conditions.setAddCommand(automation.getAddConditionCommand());
        satisfiedTasks.setTypes(types);
        satisfiedTasks.setList(automation.getSatisfiedTasks());
        satisfiedTasks.setAddCommand(automation.getAddSatisifedTaskCommand());
        unsatisfiedTasks.setTypes(types);
        unsatisfiedTasks.setList(automation.getUnsatisfiedTasks());
        unsatisfiedTasks.setAddCommand(automation.getAddUnsatisifedTaskCommand());
    }
}
