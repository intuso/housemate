package com.intuso.housemate.web.client.bootstrap.widget.task;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.bootstrap.widget.list.AddButton;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyTask;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class TaskList extends NestedList<com.intuso.housemate.client.v1_0.api.object.Task.Data, GWTProxyTask> {

    private GWTProxyList<Type.Data<?>, GWTProxyType> types;

    public void setTypes(GWTProxyList<Type.Data<?>, GWTProxyType> types) {
        this.types = types;
    }

    public void setAddCommand(GWTProxyCommand command) {
        setHeaderWidget(new AddButton(types, command));
    }

    @Override
    protected Widget getWidget(ChildOverview childOverview, GWTProxyTask task) {
        return new Task(types, task);
    }
}
