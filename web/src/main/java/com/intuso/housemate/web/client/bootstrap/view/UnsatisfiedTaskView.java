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
import com.intuso.housemate.web.client.place.UnsatisfiedTaskPlace;

/**
 */
public class UnsatisfiedTaskView extends ObjectListView<GWTProxyTask, UnsatisfiedTaskPlace>
        implements com.intuso.housemate.web.client.ui.view.UnsatisfiedTaskView {

    private GWTProxyAutomation automation;

    public UnsatisfiedTaskView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    public void newPlace(UnsatisfiedTaskPlace place) {

        automation = null;

        if(place.getAutomationName() != null) {
            GWTProxyList<AutomationData, GWTProxyAutomation> automations = resources.getRoot().getAutomations();
            if(automations.get(place.getAutomationName()) != null)
                automation = automations.get(place.getAutomationName());
        }

        super.newPlace(place);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected GWTProxyList<TaskData, GWTProxyTask> getList(UnsatisfiedTaskPlace place) {
        return automation.getUnsatisfiedTasks();
    }

    @Override
    protected GWTProxyCommand getAddCommand(UnsatisfiedTaskPlace place) {
        return automation.getAddUnsatisifedTaskCommand();
    }

    @Override
    protected String getSelectedObjectName(UnsatisfiedTaskPlace place) {
        return place.getTaskName();
    }

    @Override
    protected Widget getObjectWidget(UnsatisfiedTaskPlace place, GWTProxyTask task) {
        return new Task(task);
    }

    @Override
    protected UnsatisfiedTaskPlace getPlace(UnsatisfiedTaskPlace place, GWTProxyTask object) {
        if(object == null)
            return new UnsatisfiedTaskPlace(place.getAutomationName());
        else
            return new UnsatisfiedTaskPlace(place.getAutomationName(), object.getId());
    }
}