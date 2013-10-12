package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandPopup;
import com.intuso.housemate.web.client.bootstrap.widget.condition.ConditionList;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.bootstrap.widget.task.TaskList;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;

/**
 */
public class Automation extends Composite {

    interface AutomationUiBinder extends UiBinder<Widget, Automation> {
    }

    private static AutomationUiBinder ourUiBinder = GWT.create(AutomationUiBinder.class);

    @UiField(provided = true)
    ConditionList conditions;
    @UiField(provided = true)
    TaskList satisfiedTasks;
    @UiField(provided = true)
    TaskList unsatisfiedTasks;

    @UiField
    Button settings;
    @UiField
    Collapse settingsPanel;
    @UiField(provided = true)
    Control control;

    private final GWTProxyAutomation automation;

    public Automation(final GWTProxyAutomation automation) {
        this.automation = automation;

        conditions = new ConditionList(automation.getConditions(), "conditions", null, true);
        satisfiedTasks = new TaskList(automation.getSatisfiedTasks(), "satisfied tasks", null, true);
        unsatisfiedTasks = new TaskList(automation.getUnsatisfiedTasks(), "unsatisfied tasks", null, false);
        control = new Control(automation);

        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("settings")
    public void settingsClicked(ClickEvent event) {
        settings.setActive(!settings.isActive());
        if(settings.isActive())
            settingsPanel.show();
        else
            settingsPanel.hide();
    }

    @UiHandler("addCondition")
    public void addCondition(ClickEvent event) {
        new CommandPopup(automation.getAddConditionCommand());
    }

    @UiHandler("addSatisfiedTask")
    public void addSatisfiedTask(ClickEvent event) {
        new CommandPopup(automation.getAddSatisifedTaskCommand());
    }

    @UiHandler("addUnsatisfiedTask")
    public void addUnsatisfiedTask(ClickEvent event) {
        new CommandPopup(automation.getAddUnsatisifedTaskCommand());
    }
}
