package com.intuso.housemate.web.client.bootstrap.widget.task;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.web.client.bootstrap.widget.list.ComplexWidgetList;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyTask;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class TaskList extends ComplexWidgetList<TaskData, GWTProxyTask> {

    public TaskList(GWTProxyList<TaskData, GWTProxyTask> list, String title,
                    List<String> filteredIds, boolean showOnEmpty) {
        super(list, title, filteredIds, showOnEmpty);
    }

    @Override
    protected Widget getWidget(GWTProxyTask task) {
        return new Task(task);
    }
}
