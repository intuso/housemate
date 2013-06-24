package com.intuso.housemate.web.client.bootstrap.widget.task;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyTask;

/**
 */
public class Task extends Composite implements TaskListener<GWTProxyTask> {

    interface TaskUiBinder extends UiBinder<Widget, Task> {
    }

    private static TaskUiBinder ourUiBinder = GWT.create(TaskUiBinder.class);

    @UiField
    public Icon executingIcon;
    @UiField
    public PropertyList propertyList;

    public Task(GWTProxyTask task) {

        initWidget(ourUiBinder.createAndBindUi(this));

        executingIcon.setType(task.isExecuting() ? IconType.THUMBS_UP : IconType.THUMBS_DOWN);
        propertyList.setList(task.getProperties());

        task.addObjectListener(this);
    }

    @Override
    public void taskExecuting(GWTProxyTask task, boolean executing) {
        executingIcon.setType(executing ? IconType.THUMBS_UP : IconType.THUMBS_DOWN);
    }

    @Override
    public void taskError(GWTProxyTask task, String error) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
