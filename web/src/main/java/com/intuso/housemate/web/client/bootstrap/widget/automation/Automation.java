package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.condition.ConditionList;
import com.intuso.housemate.web.client.bootstrap.widget.object.ConfigurableObject;
import com.intuso.housemate.web.client.bootstrap.widget.task.TaskList;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;

/**
 */
public class Automation extends ConfigurableObject {

    interface AutomationUiBinder extends UiBinder<Widget, Automation> {}

    private static AutomationUiBinder ourUiBinder = GWT.create(AutomationUiBinder.class);

    @UiField(provided = true)
    ConditionList conditions;
    @UiField(provided = true)
    TaskList satisfiedTasks;
    @UiField(provided = true)
    TaskList unsatisfiedTasks;

    private final GWTProxyAutomation automation;

    public Automation(final GWTProxyAutomation automation) {

        this.automation = automation;

        conditions = new ConditionList(automation.getConditions(), "conditions", null, true);
        satisfiedTasks = new TaskList(automation.getSatisfiedTasks(), "satisfied tasks", null, true);
        unsatisfiedTasks = new TaskList(automation.getUnsatisfiedTasks(), "unsatisfied tasks", null, false);

        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected Widget createSettingsWidget() {
        return new AutomationSettings(automation);
    }
}
