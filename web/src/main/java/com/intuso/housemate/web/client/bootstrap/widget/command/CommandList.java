package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class CommandList extends NestedList<CommandData, GWTProxyCommand> {
    @Override
    protected Widget getWidget(ChildOverview childOverview, GWTProxyCommand command) {
        return new Command(command);
    }
}
