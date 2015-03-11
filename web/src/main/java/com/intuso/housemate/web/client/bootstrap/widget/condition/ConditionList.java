package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.web.client.bootstrap.widget.list.AddButton;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyCondition;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class ConditionList extends NestedList<ConditionData, GWTProxyCondition> {

    public void setAddCommand(GWTProxyCommand command) {
        setHeaderWidget(new AddButton(command));
    }

    @Override
    protected Widget getWidget(ChildOverview childOverview, GWTProxyCondition condition) {
            return new Condition(condition);
    }
}
