package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.web.client.bootstrap.widget.list.ComplexWidgetList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class CommandList extends ComplexWidgetList<CommandData, GWTProxyCommand> {

    public CommandList(GWTProxyList<CommandData, GWTProxyCommand> list, String title,
                       List<String> filteredIds, boolean showOnEmpty) {
        super(list, title, filteredIds, showOnEmpty);
    }

    @Override
    protected Widget getWidget(GWTProxyCommand command) {
        return new Command(command);
    }
}
