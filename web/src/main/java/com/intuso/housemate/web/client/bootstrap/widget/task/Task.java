package com.intuso.housemate.web.client.bootstrap.widget.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.ConfigurableObject;
import com.intuso.housemate.web.client.object.GWTProxyTask;

/**
 */
public class Task extends ConfigurableObject {

    interface TaskUiBinder extends UiBinder<Widget, Task> {}

    private static TaskUiBinder ourUiBinder = GWT.create(TaskUiBinder.class);

    private final GWTProxyTask task;

    public Task(GWTProxyTask task) {
        this.task = task;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected Widget createSettingsWidget() {
        return new TaskSettings(task);
    }
}
