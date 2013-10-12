package com.intuso.housemate.web.client.bootstrap.widget.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.WidgetRow;
import com.intuso.housemate.web.client.bootstrap.widget.object.ConfigurableObject;
import com.intuso.housemate.web.client.bootstrap.widget.value.Value;
import com.intuso.housemate.web.client.object.GWTProxyTask;

/**
 */
public class Task extends ConfigurableObject {

    interface TaskUiBinder extends UiBinder<Widget, Task> {}

    private static TaskUiBinder ourUiBinder = GWT.create(TaskUiBinder.class);

    @UiField(provided = true)
    WidgetRow executing;

    private final GWTProxyTask task;

    public Task(GWTProxyTask task) {
        this.task = task;
        executing = new WidgetRow("executing", Value.getWidget(task.getExecutingValue()));
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected Widget createSettingsWidget() {
        return new TaskSettings(task);
    }
}
