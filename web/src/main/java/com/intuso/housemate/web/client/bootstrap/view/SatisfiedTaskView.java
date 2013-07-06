package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.task.Task;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyTask;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.place.SatisfiedTaskPlace;

/**
 */
public class SatisfiedTaskView extends ObjectListView<GWTProxyTask, SatisfiedTaskPlace>
        implements com.intuso.housemate.web.client.ui.view.SatisfiedTaskView {

    private GWTProxyAutomation automation;

    public SatisfiedTaskView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    public void newPlace(SatisfiedTaskPlace place) {

        automation = null;

        if(place.getAutomationName() != null) {
            GWTProxyList<AutomationData, GWTProxyAutomation> automations = resources.getRoot().getAutomations();
            if(automations.get(place.getAutomationName()) != null)
                automation = automations.get(place.getAutomationName());
        }

        super.newPlace(place);
    }

    @Override
    protected GWTProxyList<TaskData, GWTProxyTask> getList(SatisfiedTaskPlace place) {
        return automation.getSatisfiedTasks();
    }

    @Override
    protected GWTProxyCommand getAddCommand(SatisfiedTaskPlace place) {
        return automation.getAddSatisifedTaskCommand();
    }

    @Override
    protected String getSelectedObjectName(SatisfiedTaskPlace place) {
        return place.getTaskName();
    }

    @Override
    protected Widget getObjectWidget(SatisfiedTaskPlace place, GWTProxyTask task) {
        return new Task(task);
    }

    @Override
    protected SatisfiedTaskPlace getPlace(SatisfiedTaskPlace place, GWTProxyTask object) {
        if(object == null)
            return new SatisfiedTaskPlace(place.getAutomationName());
        else
            return new SatisfiedTaskPlace(place.getAutomationName(), object.getId());
    }
}