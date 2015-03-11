package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.condition.ConditionList;
import com.intuso.housemate.web.client.bootstrap.widget.task.TaskList;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;

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

    public AutomationBody(final GWTProxyAutomation automation) {
        initWidget(ourUiBinder.createAndBindUi(this));
        conditions.setList(automation.getConditions());
        conditions.setAddCommand(automation.getAddConditionCommand());
        satisfiedTasks.setList(automation.getSatisfiedTasks());
        satisfiedTasks.setAddCommand(automation.getAddSatisifedTaskCommand());
        unsatisfiedTasks.setList(automation.getUnsatisfiedTasks());
        unsatisfiedTasks.setAddCommand(automation.getAddUnsatisifedTaskCommand());
    }
}
