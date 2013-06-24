package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectNavs;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.object.GWTProxyTask;
import com.intuso.housemate.web.client.place.ConditionPlace;
import com.intuso.housemate.web.client.place.SatisfiedTaskPlace;
import com.intuso.housemate.web.client.place.UnsatisfiedTaskPlace;

/**
 */
public class Automation extends Composite {

    interface AutomationUiBinder extends UiBinder<Widget, Automation> {
    }

    private static AutomationUiBinder ourUiBinder = GWT.create(AutomationUiBinder.class);

    @UiField
    public PerformButton startButton;
    @UiField
    public PerformButton stopButton;
    @UiField
    public PerformButton removeButton;
    @UiField
    public ObjectNavs<GWTProxyCondition> conditionList;
    @UiField
    public ObjectNavs<GWTProxyTask> satisfiedTaskList;
    @UiField
    public ObjectNavs<GWTProxyTask> unsatisfiedTaskList;

    public Automation(final GWTProxyAutomation automation) {

        initWidget(ourUiBinder.createAndBindUi(this));

        startButton.setCommand(automation.getStartCommand());
        stopButton.setCommand(automation.getStopCommand());
        removeButton.setCommand(automation.getRemoveCommand());
        conditionList.setList(automation.getConditions(), automation.getAddConditionCommand());
        conditionList.addObjectSelectedHandler(new ObjectSelectedHandler<GWTProxyCondition>() {
            @Override
            public void objectSelected(ObjectSelectedEvent<GWTProxyCondition> event) {
                Housemate.FACTORY.getPlaceController().goTo(
                        new ConditionPlace(automation.getId(), 0, Lists.newArrayList(event.getObject().getId())));
            }
        });
        satisfiedTaskList.setList(automation.getSatisfiedTasks(), automation.getAddSatisifedTaskCommand());
        satisfiedTaskList.addObjectSelectedHandler(new ObjectSelectedHandler<GWTProxyTask>() {
            @Override
            public void objectSelected(ObjectSelectedEvent<GWTProxyTask> event) {
                Housemate.FACTORY.getPlaceController().goTo(
                        new SatisfiedTaskPlace(automation.getId(), event.getObject().getId()));
            }
        });
        unsatisfiedTaskList.setList(automation.getUnsatisfiedTasks(), automation.getAddUnsatisifedTaskCommand());
        unsatisfiedTaskList.addObjectSelectedHandler(new ObjectSelectedHandler<GWTProxyTask>() {
            @Override
            public void objectSelected(ObjectSelectedEvent<GWTProxyTask> event) {
                Housemate.FACTORY.getPlaceController().goTo(
                        new UnsatisfiedTaskPlace(automation.getId(), event.getObject().getId()));
            }
        });
    }
}
